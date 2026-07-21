package com.hazerfen.sifahane.security

import com.hazerfen.sifahane.data.UserProfile

/**
 * v3.6 temiz kurulumunda otomatik üretilen, hiçbir kullanıcı bilgisi taşımayan
 * "Kendim" kaydını gerçek profillerden ayırır. Veri varsa hiçbir zaman silme
 * önermez.
 */
object LegacyPlaceholderPolicy {
    fun canReplaceWithFirstRunSetup(
        allProfiles: List<UserProfile>,
        relatedRecordCount: Int,
        hasPattern: Boolean
    ): Boolean {
        if (allProfiles.size != 1 || relatedRecordCount != 0 || hasPattern) return false
        val profile = allProfiles.single()
        return profile.name.trim().equals("Kendim", ignoreCase = true) &&
            profile.surname.isBlank() &&
            profile.relation.isBlank() &&
            profile.photoUri.isNullOrBlank() &&
            profile.birthDate.isNullOrBlank() &&
            profile.bloodGroup == "Bilinmiyor" &&
            profile.profileNote.isBlank() &&
            profile.adminPinHash.isNullOrBlank() &&
            profile.role == UserRoles.ADMIN
    }
}
