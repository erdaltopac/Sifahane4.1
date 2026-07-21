package com.hazerfen.sifahane.alarm

/** Android bağımlılığı olmayan, test edilebilir alarm gruplama kuralları. */
object AlarmGroupingPolicy {
    fun groupKey(kind: MedicationAlarmKind, profileId: Long, triggerAt: Long): String =
        "${kind.name}:$profileId:${triggerAt / 60_000L}"

    fun uniqueMedicationCount(medicationIds: LongArray): Int =
        medicationIds.asSequence().filter { it > 0L }.distinct().count()
}
