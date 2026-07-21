package com.hazerfen.sifahane.security

import com.hazerfen.sifahane.data.UserProfile
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LegacyPlaceholderPolicyTest {
    private val placeholder = UserProfile(
        id = 1,
        name = "Kendim",
        role = UserRoles.ADMIN,
        permissionsCsv = UserRoles.ADMIN_PERMISSIONS
    )

    @Test
    fun emptyLegacyPlaceholderCanBeReplaced() {
        assertTrue(
            LegacyPlaceholderPolicy.canReplaceWithFirstRunSetup(
                listOf(placeholder), relatedRecordCount = 0, hasPattern = false
            )
        )
    }

    @Test
    fun userDataOrSecurityMaterialPreventsReplacement() {
        assertFalse(
            LegacyPlaceholderPolicy.canReplaceWithFirstRunSetup(
                listOf(placeholder), relatedRecordCount = 1, hasPattern = false
            )
        )
        assertFalse(
            LegacyPlaceholderPolicy.canReplaceWithFirstRunSetup(
                listOf(placeholder.copy(adminPinHash = "saved")), 0, false
            )
        )
        assertFalse(
            LegacyPlaceholderPolicy.canReplaceWithFirstRunSetup(
                listOf(placeholder), 0, true
            )
        )
    }

    @Test
    fun realProfileIsNeverTreatedAsPlaceholder() {
        assertFalse(
            LegacyPlaceholderPolicy.canReplaceWithFirstRunSetup(
                listOf(placeholder.copy(name = "Erdal", profileNote = "Gerçek profil")), 0, false
            )
        )
    }
}
