package com.hazerfen.sifahane.alarm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class AlarmGroupingTest {
    @Test
    fun sameMinuteUsesOneGroupKey() {
        val first = 1_800_000L
        assertEquals(
            alarmGroupKey(MedicationAlarmKind.NORMAL, 1L, first),
            alarmGroupKey(MedicationAlarmKind.NORMAL, 1L, first + 59_999L)
        )
    }

    @Test
    fun adjacentMinutesUseDifferentGroupKeys() {
        assertNotEquals(
            alarmGroupKey(MedicationAlarmKind.NORMAL, 1L, 1_800_000L),
            alarmGroupKey(MedicationAlarmKind.NORMAL, 1L, 1_860_000L)
        )
    }

    @Test
    fun normalAndSnoozeDoNotOverwriteEachOther() {
        assertNotEquals(
            alarmGroupKey(MedicationAlarmKind.NORMAL, 1L, 1_800_000L),
            alarmGroupKey(MedicationAlarmKind.SNOOZE, 1L, 1_800_000L)
        )
    }
}
