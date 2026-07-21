package com.hazerfen.sifahane.alarm

import android.content.Context
import java.security.MessageDigest

/**
 * Kişisel sağlık verisi içermeyen alarm çalışma kayıtları.
 * İlaç adları ve profil bilgileri hiçbir zaman bu depoya yazılmaz.
 */
object AlarmDiagnostics {
    private const val FILE = "sifahane_alarm_diagnostics"

    data class Snapshot(
        val lastTriggeredAt: Long,
        val lastGroupHash: String,
        val lastDoseCount: Int,
        val lastNotificationId: Int,
        val lastRequestCode: Int,
        val lastSnoozedAt: Long,
        val lastSnoozeTriggerAt: Long,
        val lastSnoozeMinutes: Int,
        val nextScheduledAt: Long,
        val nextGroupHash: String,
        val nextPlanningType: String,
        val activeScreen: Boolean,
        val activeGroupHash: String,
        val lastAutomaticRescheduleAt: Long,
        val lastAutomaticRescheduleReason: String,
        val lastAutomaticRescheduleSucceeded: Boolean
    )

    fun recordTrigger(
        context: Context,
        groupKey: String,
        doseCount: Int,
        notificationId: Int,
        requestCode: Int
    ) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit()
            .putLong("last_triggered_at", System.currentTimeMillis())
            .putString("last_group_hash", hashKey(groupKey))
            .remove("last_group_key")
            .putInt("last_dose_count", doseCount.coerceAtLeast(0))
            .putInt("last_notification_id", notificationId)
            .putInt("last_request_code", requestCode)
            .apply()
    }

    fun recordScheduled(
        context: Context,
        groupKey: String,
        triggerAt: Long,
        requestCode: Int,
        planningType: String = "Bilinmiyor"
    ) {
        val prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        val currentNext = prefs.getLong("next_scheduled_at", Long.MAX_VALUE)
        if (triggerAt < currentNext || currentNext < System.currentTimeMillis()) {
            prefs.edit()
                .putLong("next_scheduled_at", triggerAt)
                .putString("next_group_hash", hashKey(groupKey))
                .remove("next_group_key")
                .putInt("next_request_code", requestCode)
                .putString("next_planning_type", planningType)
                .apply()
        }
    }

    fun recordSnooze(context: Context, minutes: Int, triggerAt: Long) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit()
            .putLong("last_snoozed_at", System.currentTimeMillis())
            .putLong("last_snooze_trigger_at", triggerAt)
            .putInt("last_snooze_minutes", minutes.coerceAtLeast(0))
            .apply()
    }

    fun setAlarmScreenActive(context: Context, groupKey: String?, active: Boolean) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit()
            .putBoolean("active_screen", active)
            .apply {
                if (active && groupKey != null) putString("active_group_hash", hashKey(groupKey))
                else remove("active_group_hash")
            }
            .apply()
    }

    fun recordAutomaticReschedule(context: Context, reason: String, succeeded: Boolean) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit()
            .putLong("last_auto_reschedule_at", System.currentTimeMillis())
            .putString("last_auto_reschedule_reason", reason.take(120))
            .putBoolean("last_auto_reschedule_succeeded", succeeded)
            .apply()
    }

    fun clearHistory(context: Context) {
        val prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        val nextAt = prefs.getLong("next_scheduled_at", 0L)
        val nextHash = prefs.getString("next_group_hash", null)
        val nextCode = prefs.getInt("next_request_code", 0)
        val nextPlanningType = prefs.getString("next_planning_type", null)
        val active = prefs.getBoolean("active_screen", false)
        val activeHash = prefs.getString("active_group_hash", null)
        prefs.edit().clear().apply()
        prefs.edit().apply {
            if (nextAt > 0L) putLong("next_scheduled_at", nextAt)
            if (!nextHash.isNullOrBlank()) putString("next_group_hash", nextHash)
            if (nextCode != 0) putInt("next_request_code", nextCode)
            if (!nextPlanningType.isNullOrBlank()) putString("next_planning_type", nextPlanningType)
            if (active) putBoolean("active_screen", true)
            if (!activeHash.isNullOrBlank()) putString("active_group_hash", activeHash)
        }.apply()
    }

    fun clearNextIfMatches(context: Context, groupKey: String) {
        val prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        if (prefs.getString("next_group_hash", "") == hashKey(groupKey)) {
            prefs.edit()
                .remove("next_scheduled_at")
                .remove("next_group_hash")
                .remove("next_request_code")
                .remove("next_planning_type")
                .apply()
        }
    }

    fun snapshot(context: Context): Snapshot {
        val prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        return Snapshot(
            lastTriggeredAt = prefs.getLong("last_triggered_at", 0L),
            lastGroupHash = prefs.getString("last_group_hash", null)
                ?: prefs.getString("last_group_key", null)?.let(::hashKey).orEmpty(),
            lastDoseCount = prefs.getInt("last_dose_count", 0),
            lastNotificationId = prefs.getInt("last_notification_id", 0),
            lastRequestCode = prefs.getInt("last_request_code", 0),
            lastSnoozedAt = prefs.getLong("last_snoozed_at", 0L),
            lastSnoozeTriggerAt = prefs.getLong("last_snooze_trigger_at", 0L),
            lastSnoozeMinutes = prefs.getInt("last_snooze_minutes", 0),
            nextScheduledAt = prefs.getLong("next_scheduled_at", 0L),
            nextGroupHash = prefs.getString("next_group_hash", null)
                ?: prefs.getString("next_group_key", null)?.let(::hashKey).orEmpty(),
            nextPlanningType = prefs.getString("next_planning_type", "Bilinmiyor").orEmpty(),
            activeScreen = prefs.getBoolean("active_screen", false),
            activeGroupHash = prefs.getString("active_group_hash", "").orEmpty(),
            lastAutomaticRescheduleAt = prefs.getLong("last_auto_reschedule_at", 0L),
            lastAutomaticRescheduleReason = prefs.getString("last_auto_reschedule_reason", "").orEmpty(),
            lastAutomaticRescheduleSucceeded = prefs.getBoolean("last_auto_reschedule_succeeded", false)
        )
    }

    internal fun hashKey(value: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(value.toByteArray(Charsets.UTF_8))
        return bytes.take(6).joinToString("") { "%02x".format(it) }
    }
}
