package com.hazerfen.sifahane.security

import android.content.Context

/**
 * Biyometrik giriş tercihini profil bazında saklar.
 * Tercih varsayılan olarak kapalıdır ve yalnız kullanıcı sistem biyometri
 * penceresinde başarıyla doğrulandıktan sonra açılır.
 */
object BiometricPreferences {
    private const val PREF = "sifahane_biometric_preferences"

    private fun key(profileId: Long) = "enabled_$profileId"

    fun isEnabled(context: Context, profileId: Long): Boolean =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getBoolean(key(profileId), false)

    fun setEnabled(context: Context, profileId: Long, enabled: Boolean) {
        val editor = context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit()
        if (enabled) editor.putBoolean(key(profileId), true) else editor.remove(key(profileId))
        check(editor.commit()) { "Biyometrik giriş tercihi kaydedilemedi." }
    }

    fun clear(context: Context, profileId: Long) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .remove(key(profileId))
            .apply()
    }
}
