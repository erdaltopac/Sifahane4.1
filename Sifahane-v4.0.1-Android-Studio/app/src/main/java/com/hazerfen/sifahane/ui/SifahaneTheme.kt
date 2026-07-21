package com.hazerfen.sifahane.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.hazerfen.sifahane.R

private val LogoColor = Color(0xFF72D4CD)
private val Vantablack = Color(0xFF050505)
private val TurkishRed = Color(0xFFE30A17)
private val TurkishBlue = Color(0xFF00AEEF)

internal val LocalCardInnerAccentOpacity = staticCompositionLocalOf { 0.50f }

private fun brandLightPalette(config: ThemeConfiguration): ColorScheme {
    val accentAlpha = when (config.preset) {
        ThemePreset.CALM -> config.accentOpacity * .82f
        ThemePreset.VIVID -> config.accentOpacity
        ThemePreset.HIGH_CONTRAST -> 1f
    }.coerceIn(.35f, 1f)
    val accent = LogoColor.copy(alpha = accentAlpha)
    val softAccent = LogoColor.copy(
        alpha = when (config.preset) {
            ThemePreset.CALM -> .10f
            ThemePreset.VIVID -> .18f
            ThemePreset.HIGH_CONTRAST -> .24f
        }
    )
    val surface = when (config.preset) {
        ThemePreset.CALM -> Color(0xFFF8FBFA)
        ThemePreset.VIVID -> Color.White
        ThemePreset.HIGH_CONTRAST -> Color.White
    }
    return lightColorScheme(
        primary = accent,
        onPrimary = Vantablack,
        primaryContainer = softAccent,
        onPrimaryContainer = Vantablack,
        inversePrimary = accent,
        secondary = accent,
        onSecondary = Vantablack,
        secondaryContainer = softAccent,
        onSecondaryContainer = Vantablack,
        tertiary = TurkishBlue,
        onTertiary = Color.White,
        tertiaryContainer = TurkishBlue.copy(alpha = .12f),
        onTertiaryContainer = Vantablack,
        surface = surface,
        onSurface = Vantablack,
        surfaceVariant = softAccent,
        onSurfaceVariant = Vantablack.copy(alpha = .78f),
        background = if (config.preset == ThemePreset.CALM) Color(0xFFFBFDFC) else Color.White,
        onBackground = Vantablack,
        outline = if (config.preset == ThemePreset.HIGH_CONTRAST) Vantablack else Vantablack.copy(alpha = .58f),
        outlineVariant = TurkishBlue.copy(alpha = config.cardInnerAccentOpacity),
        error = TurkishRed,
        onError = Color.White,
        errorContainer = TurkishRed.copy(alpha = .10f),
        onErrorContainer = TurkishRed
    )
}

private fun brandDarkPalette(config: ThemeConfiguration, oled: Boolean): ColorScheme {
    val background = if (oled) Color.Black else Color(0xFF0D1110)
    val surface = if (oled) Color.Black else Color(0xFF151B19)
    val elevated = if (oled) Color(0xFF090B0A) else Color(0xFF202825)
    val accent = LogoColor.copy(alpha = config.accentOpacity.coerceIn(.55f, 1f))
    val onDark = Color(0xFFF4FAF8)
    return darkColorScheme(
        primary = accent,
        onPrimary = Vantablack,
        primaryContainer = LogoColor.copy(alpha = .22f),
        onPrimaryContainer = onDark,
        inversePrimary = LogoColor,
        secondary = accent,
        onSecondary = Vantablack,
        secondaryContainer = LogoColor.copy(alpha = .18f),
        onSecondaryContainer = onDark,
        tertiary = Color(0xFF58C7F0),
        onTertiary = Color(0xFF002E3D),
        tertiaryContainer = TurkishBlue.copy(alpha = .25f),
        onTertiaryContainer = onDark,
        surface = surface,
        onSurface = onDark,
        surfaceVariant = elevated,
        onSurfaceVariant = onDark.copy(alpha = .82f),
        background = background,
        onBackground = onDark,
        outline = if (config.preset == ThemePreset.HIGH_CONTRAST) Color.White else onDark.copy(alpha = .64f),
        outlineVariant = Color(0xFF58C7F0).copy(alpha = config.cardInnerAccentOpacity),
        error = Color(0xFFFF6B6B),
        onError = Color(0xFF420001),
        errorContainer = Color(0xFF640006),
        onErrorContainer = Color(0xFFFFDAD6)
    )
}

private fun sifahaneTypography(config: ThemeConfiguration) = Typography().let { base ->
    val family = when (config.font) {
        ThemeFont.NOTO_SANS -> FontFamily(Font(R.font.noto_sans))
        ThemeFont.ATKINSON -> FontFamily(Font(R.font.atkinson_hyperlegible))
        ThemeFont.LEXEND -> FontFamily(Font(R.font.lexend))
        ThemeFont.NOTO_SERIF -> FontFamily(Font(R.font.noto_serif))
        ThemeFont.ROBOTO -> FontFamily(Font(R.font.roboto))
    }
    fun TextStyle.unified() = copy(fontFamily = family, fontSize = fontSize * config.fontScale)
    base.copy(
        displayLarge = base.displayLarge.unified(), displayMedium = base.displayMedium.unified(),
        displaySmall = base.displaySmall.unified(), headlineLarge = base.headlineLarge.unified(),
        headlineMedium = base.headlineMedium.unified(), headlineSmall = base.headlineSmall.unified(),
        titleLarge = base.titleLarge.unified(), titleMedium = base.titleMedium.unified(),
        titleSmall = base.titleSmall.unified(), bodyLarge = base.bodyLarge.unified(),
        bodyMedium = base.bodyMedium.unified(), bodySmall = base.bodySmall.unified(),
        labelLarge = base.labelLarge.unified(), labelMedium = base.labelMedium.unified(),
        labelSmall = base.labelSmall.unified()
    )
}

@Composable
fun SifahaneTheme(
    configuration: ThemeConfiguration? = null,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val config = configuration ?: ThemePreferences.load(context)
    val systemDark = isSystemInDarkTheme()
    val dark = when (config.mode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.DARK, ThemeMode.OLED -> true
        ThemeMode.LIGHT -> false
    }
    val dynamicAvailable = config.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        dynamicAvailable && dark -> dynamicDarkColorScheme(context)
        dynamicAvailable -> dynamicLightColorScheme(context)
        dark -> brandDarkPalette(config, oled = config.mode == ThemeMode.OLED)
        else -> brandLightPalette(config)
    }
    CompositionLocalProvider(
        LocalCardInnerAccentOpacity provides config.cardInnerAccentOpacity
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = sifahaneTypography(config),
            content = content
        )
    }
}
