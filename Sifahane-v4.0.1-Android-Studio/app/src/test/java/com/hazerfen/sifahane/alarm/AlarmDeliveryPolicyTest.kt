package com.hazerfen.sifahane.alarm

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AlarmDeliveryPolicyTest {
    @Test fun duplicateWindowSuppressesSecondDelivery() {
        assertTrue(AlarmDeliveryPolicy.isDuplicate(false, 1000L, 2000L))
        assertFalse(AlarmDeliveryPolicy.isDuplicate(false, 1000L, 31_001L))
    }

    @Test fun handledAlarmIsAlwaysSuppressed() {
        assertTrue(AlarmDeliveryPolicy.isDuplicate(true, 0L, 1L))
    }
}
