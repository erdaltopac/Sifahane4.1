package com.hazerfen.sifahane.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.hazerfen.sifahane.data.Medication
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class AlarmDose(
    val medicationId: Long,
    val profileId: Long,
    val medicationName: String,
    val scheduledTime: String,
    val plannedDateTime: Long
)


internal fun alarmGroupKey(kind: MedicationAlarmKind, profileId: Long, triggerAt: Long): String =
    AlarmGroupingPolicy.groupKey(kind, profileId, triggerAt)

object AlarmScheduler {
    private const val TEST_REQUEST_CODE = 7_500_001
    private const val GROUP_ACTION = "com.hazerfen.sifahane.MEDICATION_GROUP_ALARM"
    private const val REGISTRY_PREFS = "sifahane_alarm_registry"
    private const val REGISTRY_KEYS = "scheduled_group_keys"

    /** Kept for existing callers; a complete refresh is preferred after edits. */
    fun scheduleMedication(context: Context, medication: Medication) {
        cancelMedication(context, medication.id)
        if (!medication.active || medication.archived) return
        medicationTimes(medication).forEach { time ->
            val occurrence = nextValidOccurrence(medication, time, System.currentTimeMillis())
                ?: return@forEach
            scheduleGroup(
                context,
                listOf(medication.toAlarmDose(time, occurrence)),
                occurrence,
                MedicationAlarmKind.NORMAL
            )
        }
    }

    fun scheduleGroup(
        context: Context,
        doses: List<AlarmDose>,
        triggerAt: Long,
        kind: MedicationAlarmKind
    ) {
        if (doses.isEmpty()) return
        val profileId = doses.first().profileId
        require(doses.all { it.profileId == profileId }) { "Farklı kullanıcıların dozları tek alarm grubuna alınamaz." }
        val groupKey = alarmGroupKey(kind, profileId, triggerAt)
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = GROUP_ACTION
            data = Uri.parse("sifahane://medication-alarm/$groupKey")
            putExtra("groupKey", groupKey)
            putExtra("triggerAt", triggerAt)
            putExtra("alarmKind", kind.name)
            putExtra("medicationIds", doses.map(AlarmDose::medicationId).toLongArray())
            putExtra("profileIds", doses.map(AlarmDose::profileId).toLongArray())
            putExtra("plannedDateTimes", doses.map(AlarmDose::plannedDateTime).toLongArray())
            putStringArrayListExtra(
                "scheduledTimes",
                ArrayList(doses.map(AlarmDose::scheduledTime))
            )
            putStringArrayListExtra(
                "medicationNames",
                ArrayList(doses.map(AlarmDose::medicationName))
            )
        }
        val requestCode = AlarmRequestCodeRegistry.getOrAllocate(context, groupKey)
        val pending = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val planningType = setAlarm(context.getSystemService(AlarmManager::class.java), triggerAt, pending)
        rememberGroup(context, groupKey)
        AlarmDiagnostics.recordScheduled(context, groupKey, triggerAt, requestCode, planningType)
    }

    fun notificationId(context: Context, groupKey: String): Int =
        AlarmRequestCodeRegistry.getOrAllocate(context, groupKey)

    fun cancelGroup(context: Context, groupKey: String) {
        val manager = context.getSystemService(AlarmManager::class.java)
        val currentCode = AlarmRequestCodeRegistry.find(context, groupKey)
        val legacyCode = groupKey.hashCode()
        listOfNotNull(currentCode, legacyCode).distinct().forEach { code ->
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                action = GROUP_ACTION
                data = Uri.parse("sifahane://medication-alarm/$groupKey")
            }
            PendingIntent.getBroadcast(
                context,
                code,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )?.let {
                manager.cancel(it)
                it.cancel()
            }
        }
        forgetGroup(context, groupKey)
        AlarmRequestCodeRegistry.release(context, groupKey)
    }

    fun registeredGroupCount(context: Context): Int =
        context.getSharedPreferences(REGISTRY_PREFS, Context.MODE_PRIVATE)
            .getStringSet(REGISTRY_KEYS, emptySet()).orEmpty().size

    /** Cancels every medication group alarm registered by current versions of Şifahane. */
    fun cancelAllRegisteredGroups(context: Context): Int {
        val prefs = context.getSharedPreferences(REGISTRY_PREFS, Context.MODE_PRIVATE)
        val keys = prefs.getStringSet(REGISTRY_KEYS, emptySet()).orEmpty().toSet()
        keys.forEach { key ->
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                action = GROUP_ACTION
                data = Uri.parse("sifahane://medication-alarm/$key")
            }
            val manager = context.getSystemService(AlarmManager::class.java)
            listOfNotNull(AlarmRequestCodeRegistry.find(context, key), key.hashCode())
                .distinct()
                .forEach { code ->
                    PendingIntent.getBroadcast(
                        context,
                        code,
                        intent,
                        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                    )?.let {
                        manager.cancel(it)
                        it.cancel()
                    }
                }
            AlarmRequestCodeRegistry.release(context, key)
        }
        prefs.edit().remove(REGISTRY_KEYS).apply()
        return keys.size
    }

    private fun rememberGroup(context: Context, groupKey: String) {
        val prefs = context.getSharedPreferences(REGISTRY_PREFS, Context.MODE_PRIVATE)
        val keys = prefs.getStringSet(REGISTRY_KEYS, emptySet()).orEmpty().toMutableSet()
        keys += groupKey
        prefs.edit().putStringSet(REGISTRY_KEYS, keys).apply()
    }

    private fun forgetGroup(context: Context, groupKey: String) {
        val prefs = context.getSharedPreferences(REGISTRY_PREFS, Context.MODE_PRIVATE)
        val keys = prefs.getStringSet(REGISTRY_KEYS, emptySet()).orEmpty().toMutableSet()
        if (keys.remove(groupKey)) prefs.edit().putStringSet(REGISTRY_KEYS, keys).apply()
    }

    suspend fun scheduleNextGroups(context: Context, doses: List<AlarmDose>, afterMillis: Long) {
        val db = com.hazerfen.sifahane.data.AppDatabase.get(context)
        val next = doses.mapNotNull { old ->
            val medication = db.medicationDao().byId(old.medicationId) ?: return@mapNotNull null
            val occurrence = nextValidOccurrence(medication, old.scheduledTime, afterMillis)
                ?: return@mapNotNull null
            medication.toAlarmDose(old.scheduledTime, occurrence)
        }
        next.groupBy { Triple(it.profileId, it.plannedDateTime, it.scheduledTime) }
            .values
            .forEach { group ->
                scheduleGroup(context, group, group.first().plannedDateTime, MedicationAlarmKind.NORMAL)
            }
    }

    fun snoozeGroup(context: Context, doses: List<AlarmDose>, triggerAt: Long) =
        scheduleGroup(context, doses, triggerAt, MedicationAlarmKind.SNOOZE)

    fun snoozeAt(
        context: Context,
        medication: Medication,
        scheduledTime: String,
        plannedDateTime: Long,
        triggerAt: Long
    ) = snoozeGroup(
        context,
        listOf(medication.toAlarmDose(scheduledTime, plannedDateTime)),
        triggerAt
    )

    fun cancelMedication(context: Context, medicationId: Long) {
        // Old versions used medication-specific request codes. Cancel those during upgrade.
        val manager = context.getSystemService(AlarmManager::class.java)
        val stable = medicationId.hashCode().let { if (it == Int.MIN_VALUE) 0 else kotlin.math.abs(it) } % 15_000
        for (doseIndex in 0..20) {
            listOf(100_000 + stable * 32 + doseIndex, 2_000_000 + stable * 32 + doseIndex)
                .forEach { code -> cancelLegacy(manager, context, code) }
        }
        cancelLegacy(manager, context, 4_000_000 + stable)
    }

    fun cancelSnooze(context: Context, medicationId: Long) {
        // Group snoozes are closed by their group notification; legacy snoozes are removed here.
        val stable = medicationId.hashCode().let { if (it == Int.MIN_VALUE) 0 else kotlin.math.abs(it) } % 15_000
        cancelLegacy(
            context.getSystemService(AlarmManager::class.java),
            context,
            4_000_000 + stable
        )
    }

    private fun cancelLegacy(manager: AlarmManager, context: Context, code: Int) {
        PendingIntent.getBroadcast(
            context,
            code,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )?.let { manager.cancel(it); it.cancel() }
    }

    fun scheduleTest(context: Context, triggerAt: Long) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("isTest", true)
            putExtra("triggerAt", triggerAt)
            putExtra("groupKey", "TEST:$triggerAt")
            putExtra("medicationIds", longArrayOf(-9001L))
            putExtra("plannedDateTimes", longArrayOf(triggerAt))
            putStringArrayListExtra("scheduledTimes", arrayListOf(SimpleDateFormat("HH:mm", Locale.US).format(Date(triggerAt))))
            putStringArrayListExtra("medicationNames", arrayListOf("Şifahane Deneme Alarmı"))
        }
        val pending = PendingIntent.getBroadcast(
            context,
            TEST_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val planningType = setAlarm(context.getSystemService(AlarmManager::class.java), triggerAt, pending)
        AlarmDiagnostics.recordScheduled(context, "TEST:$triggerAt", triggerAt, TEST_REQUEST_CODE, planningType)
    }

    fun hasPendingTest(context: Context): Boolean = PendingIntent.getBroadcast(
        context,
        TEST_REQUEST_CODE,
        Intent(context, AlarmReceiver::class.java),
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    ) != null

    fun cancelTest(context: Context): Boolean {
        val pending = PendingIntent.getBroadcast(
            context,
            TEST_REQUEST_CODE,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) ?: return false
        context.getSystemService(AlarmManager::class.java).cancel(pending)
        pending.cancel()
        return true
    }

    fun medicationTimes(medication: Medication): List<String> = medication.timesCsv
        .split(',').map(String::trim).filter { it.matches(Regex("\\d{2}:\\d{2}")) }

    fun plannedForToday(scheduledTime: String): Long =
        AlarmTimePolicy.plannedForDate(
            java.time.LocalDate.now(),
            scheduledTime,
            java.time.ZoneId.systemDefault()
        ) ?: System.currentTimeMillis()

    fun isMedicationValidOn(medication: Medication, dateMillis: Long): Boolean =
        medication.active && !medication.archived && AlarmTimePolicy.isDateInRange(
            startDate = medication.startDate,
            endDate = medication.endDate,
            continuous = medication.continuous,
            dateMillis = dateMillis
        )

    fun nextValidOccurrence(
        medication: Medication,
        scheduledTime: String,
        afterMillis: Long
    ): Long? {
        if (!medication.active || medication.archived) return null
        return AlarmTimePolicy.nextValidOccurrence(
            startDate = medication.startDate,
            endDate = medication.endDate,
            continuous = medication.continuous,
            timeText = scheduledTime,
            afterMillis = afterMillis
        )
    }

    private fun Medication.toAlarmDose(time: String, planned: Long) =
        AlarmDose(id, profileId, name, time, planned)

    private fun setAlarm(manager: AlarmManager, triggerAt: Long, pending: PendingIntent): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !manager.canScheduleExactAlarms()) {
                manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
                "İzin olmadığı için gecikmeli yedek"
            } else {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
                "Kesin"
            }
        } catch (_: SecurityException) {
            manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
            "Kesin alarm erişimi reddedildiği için gecikmeli yedek"
        }
    }
}
