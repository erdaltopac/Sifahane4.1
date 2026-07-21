package com.hazerfen.sifahane.diagnostics

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.PowerManager
import androidx.core.content.ContextCompat
import com.hazerfen.sifahane.BuildConfig
import com.hazerfen.sifahane.alarm.AlarmDiagnostics
import com.hazerfen.sifahane.ui.ThemePreferences

/**
 * Destek için ad, ilaç, doktor, ölçüm, dosya yolu veya açık alarm anahtarı içermeyen
 * teknik özet üretir. Bu metin kullanıcı açıkça kopyalamadan/paylaşmadan cihazdan çıkmaz.
 */
object SupportDiagnostics {
    fun create(context: Context): String {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val powerManager = context.getSystemService(PowerManager::class.java)
        val notificationAllowed = Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val exactAllowed = Build.VERSION.SDK_INT < 31 || alarmManager.canScheduleExactAlarms()
        val fullScreenAllowed = Build.VERSION.SDK_INT < 34 || notificationManager.canUseFullScreenIntent()
        val batteryExcluded = powerManager.isIgnoringBatteryOptimizations(context.packageName)
        val cameraAllowed = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val theme = ThemePreferences.load(context)
        val alarm = AlarmDiagnostics.snapshot(context)
        val startupMillis = context.getSharedPreferences("sifahane_performance", Context.MODE_PRIVATE)
            .getLong("last_application_on_create_ms", -1L)

        fun state(value: Boolean) = if (value) "Açık" else "Kapalı / kullanıcı işlemi gerekli"
        return buildString {
            appendLine("Şifahane kişisel verisiz destek tanılaması")
            appendLine("Uygulama: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
            appendLine("Derleme: ${BuildConfig.BUILD_TYPE}")
            appendLine("Android API: ${Build.VERSION.SDK_INT}")
            appendLine("Veritabanı şema sürümü: 11")
            appendLine("Son Application.onCreate süresi: ${if (startupMillis >= 0L) "${startupMillis} ms" else "Kayıt yok"}")
            appendLine("Cihaz üreticisi/modeli: ${Build.MANUFACTURER} / ${Build.MODEL}")
            appendLine("Bildirim: ${state(notificationAllowed)}")
            appendLine("Kesin alarm: ${state(exactAllowed)}")
            appendLine("Tam ekran alarm: ${state(fullScreenAllowed)}")
            appendLine("Pil optimizasyonu muafiyeti: ${state(batteryExcluded)}")
            appendLine("Kamera: ${state(cameraAllowed)}")
            appendLine("Dosya erişimi: Android sistem seçicisi; geniş depolama izni istenmez")
            appendLine("Tema: ${theme.mode.storageValue}/${theme.preset.storageValue}")
            appendLine("Dinamik renk: ${if (theme.dynamicColor) "Açık" else "Kapalı"}")
            appendLine("Yazı tipi: ${theme.font.storageValue}")
            appendLine("Ek yazı ölçeği: ${theme.fontScale}")
            appendLine("Alarm ekranı etkin: ${if (alarm.activeScreen) "Evet" else "Hayır"}")
            appendLine("Son anonim alarm kimliği: ${alarm.lastGroupHash.ifBlank { "-" }}")
            appendLine("Sonraki anonim alarm kimliği: ${alarm.nextGroupHash.ifBlank { "-" }}")
        }.trim()
    }
}
