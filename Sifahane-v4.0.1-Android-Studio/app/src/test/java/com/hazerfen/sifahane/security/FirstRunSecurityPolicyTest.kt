package com.hazerfen.sifahane.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FirstRunSecurityPolicyTest {
    @Test
    fun validFirstAdminSetupIsAccepted() {
        assertNull(
            FirstRunSecurityPolicy.validationError(
                name = "Erdal",
                pin = "2468",
                confirmation = "2468",
                pattern = listOf(1, 2, 5, 8)
            )
        )
    }

    @Test
    fun emptyNameIsRejected() {
        assertEquals(
            "Ad alanı zorunludur.",
            FirstRunSecurityPolicy.validationError(" ", "2468", "2468", listOf(1, 2, 5, 8))
        )
    }

    @Test
    fun invalidPinAndPatternAreRejected() {
        assertEquals(
            "Yönetici şifresi 4–12 haneli olmalıdır.",
            FirstRunSecurityPolicy.validationError("Erdal", "12a", "12a", listOf(1, 2, 5, 8))
        )
        assertEquals(
            "Kilit deseni belirlemek zorunludur.",
            FirstRunSecurityPolicy.validationError("Erdal", "2468", "2468", listOf(1, 2, 3))
        )
    }

    @Test
    fun mismatchedPinIsRejected() {
        assertEquals(
            "Yönetici şifreleri eşleşmiyor.",
            FirstRunSecurityPolicy.validationError("Erdal", "2468", "8642", listOf(1, 2, 5, 8))
        )
    }
}
