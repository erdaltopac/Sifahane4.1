package com.hazerfen.sifahane.ui

import android.content.Context

enum class ThemeMode(val storageValue: String) {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark"),
    OLED("oled");

    companion object {
        fun fromStorage(value: String?): ThemeMode =
            entries.firstOrNull { it.storageValue == value } ?: SYSTEM
    }
}

enum class ThemePreset(val storageValue: String) {
    CALM("calm"),
    VIVID("vivid"),
    HIGH_CONTRAST("high_contrast");

    companion object {
        fun fromStorage(value: String?): ThemePreset = when (value) {
            // Eski tema anahtarları kaybolmadan yeni kullanıcı odaklı adlara taşınır.
            "default", "soft" -> CALM
            "turkish_blue" -> VIVID
            "turkish_red" -> HIGH_CONTRAST
            else -> entries.firstOrNull { it.storageValue == value } ?: CALM
        }
    }
}

enum class ThemeFont(val storageValue: String) {
    ROBOTO("roboto"),
    NOTO_SANS("noto_sans"),
    ATKINSON("atkinson"),
    LEXEND("lexend"),
    NOTO_SERIF("noto_serif");

    companion object {
        fun fromStorage(value: String?): ThemeFont = when (value) {
            "sans" -> ROBOTO
            "serif" -> NOTO_SERIF
            else -> entries.firstOrNull { it.storageValue == value } ?: ROBOTO
        }
    }
}

data class ThemeConfiguration(
    val mode: ThemeMode = ThemeMode.SYSTEM,
    val preset: ThemePreset = ThemePreset.CALM,
    val font: ThemeFont = ThemeFont.ROBOTO,
    val fontScale: Float = 1f,
    val accentOpacity: Float = 1f,
    val cardInnerAccentOpacity: Float = 0.50f,
    val dynamicColor: Boolean = false
)

object ThemePreferences {
    private const val FILE = "sifahane_theme"

    fun load(context: Context): ThemeConfiguration {
        val prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        return ThemeConfiguration(
            mode = ThemeMode.fromStorage(prefs.getString("mode", "system")),
            preset = ThemePreset.fromStorage(prefs.getString("preset", "calm")),
            font = ThemeFont.fromStorage(prefs.getString("font", "roboto")),
            // Bu çarpan Android'in sistem yazı ölçeğine eklenir; sistem ölçeğini sınırlamaz.
            fontScale = prefs.getFloat("font_scale", 1f).coerceIn(.85f, 2.0f),
            accentOpacity = prefs.getFloat("accent_opacity", 1f).coerceIn(.35f, 1f),
            cardInnerAccentOpacity = prefs.getFloat("card_inner_accent_opacity", .50f)
                .coerceIn(.10f, .70f),
            dynamicColor = prefs.getBoolean("dynamic_color", false)
        )
    }

    fun save(context: Context, config: ThemeConfiguration) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit()
            .putString("mode", config.mode.storageValue)
            .putString("preset", config.preset.storageValue)
            .putString("font", config.font.storageValue)
            .putFloat("font_scale", config.fontScale.coerceIn(.85f, 2.0f))
            .putFloat("accent_opacity", config.accentOpacity.coerceIn(.35f, 1f))
            .putFloat(
                "card_inner_accent_opacity",
                config.cardInnerAccentOpacity.coerceIn(.10f, .70f)
            )
            .putBoolean("dynamic_color", config.dynamicColor)
            .apply()
    }

    fun reset(context: Context) =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit().clear().apply()
}
