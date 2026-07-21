package com.hazerfen.sifahane.validation

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class HealthDataValidatorTest {
    @Test fun validBloodPressurePasses() {
        assertNull(HealthDataValidator.bloodPressureError(120, 80, 70))
    }

    @Test fun impossibleBloodPressureIsRejected() {
        assertNotNull(HealthDataValidator.bloodPressureError(70, 90, 70))
        assertNotNull(HealthDataValidator.bloodPressureError(500, 80, null))
    }

    @Test fun glucoseBoundsAreApplied() {
        assertNull(HealthDataValidator.glucoseError(100))
        assertNotNull(HealthDataValidator.glucoseError(0))
    }

    @Test fun medicationDatesAndStockAreValidated() {
        assertNull(
            HealthDataValidator.medicationError(
                name = "İlaç", dose = "1 tablet", times = listOf("08:00"),
                stock = 10, lowStockLimit = 2, startDate = "2026-07-21",
                endDate = "2026-07-30", continuous = false
            )
        )
        assertNotNull(
            HealthDataValidator.medicationError(
                name = "İlaç", dose = "1 tablet", times = listOf("25:00"),
                stock = -1, lowStockLimit = 2, startDate = "2026-07-30",
                endDate = "2026-07-21", continuous = false
            )
        )
    }
}
