package com.hazerfen.sifahane.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DoseActionPolicyTest {
    @Test fun repeatedTakenDoesNotDecreaseAgain() {
        assertFalse(DoseActionPolicy.shouldDecreaseStock("ALINDI"))
        assertTrue(DoseActionPolicy.shouldDecreaseStock(null))
    }

    @Test fun undoIsIdempotent() {
        assertTrue(DoseActionPolicy.canUndo(0))
        assertFalse(DoseActionPolicy.canUndo(1))
    }

    @Test fun legacyStockChoiceIsExplicit() {
        assertTrue(DoseActionPolicy.shouldRestoreStock(null, true))
        assertFalse(DoseActionPolicy.shouldRestoreStock(null, false))
    }
}
