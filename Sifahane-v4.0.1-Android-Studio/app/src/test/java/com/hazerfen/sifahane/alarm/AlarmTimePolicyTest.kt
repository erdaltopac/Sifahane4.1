package com.hazerfen.sifahane.alarm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId

class AlarmTimePolicyTest {
    private val istanbul = ZoneId.of("Europe/Istanbul")

    @Test fun nextDayIsSelectedAfterTodaysDose() {
        val after = LocalDateTime.of(2026, 7, 21, 9, 0).atZone(istanbul).toInstant().toEpochMilli()
        val result = AlarmTimePolicy.nextValidOccurrence(
            "2026-07-01", null, true, "08:00", after, istanbul
        )
        val expected = LocalDateTime.of(2026, 7, 22, 8, 0).atZone(istanbul).toInstant().toEpochMilli()
        assertEquals(expected, result)
    }

    @Test fun expiredMedicationHasNoOccurrence() {
        val after = LocalDateTime.of(2026, 7, 21, 9, 0).atZone(istanbul).toInstant().toEpochMilli()
        assertNull(AlarmTimePolicy.nextValidOccurrence(
            "2026-07-01", "2026-07-20", false, "08:00", after, istanbul
        ))
    }

    @Test fun dstGapProducesAValidInstant() {
        val berlin = ZoneId.of("Europe/Berlin")
        val after = LocalDateTime.of(2026, 3, 28, 23, 0).atZone(berlin).toInstant().toEpochMilli()
        assertNotNull(AlarmTimePolicy.nextValidOccurrence(
            "2026-03-29", "2026-03-29", false, "02:30", after, berlin
        ))
    }
}
