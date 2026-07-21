package com.hazerfen.sifahane.ui

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.hazerfen.sifahane.BuildConfig
import com.hazerfen.sifahane.alarm.AlarmDiagnostics
import com.hazerfen.sifahane.alarm.AlarmRefreshResult
import com.hazerfen.sifahane.alarm.AlarmScheduler
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AlarmStatusDialog(
    refreshResult: AlarmRefreshResult?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val alarmManager = context.getSystemService(AlarmManager::class.java)
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    val powerManager = context.getSystemService(PowerManager::class.java)
    val exactAlarmAllowed = Build.VERSION.SDK_INT < 31 || alarmManager.canScheduleExactAlarms()
    val notificationAllowed = Build.VERSION.SDK_INT < 33 ||
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    val fullScreenAllowed = Build.VERSION.SDK_INT < 34 || notificationManager.canUseFullScreenIntent()
    val batteryExcluded = powerManager.isIgnoringBatteryOptimizations(context.packageName)
    var diagnosticsRevision by remember { mutableIntStateOf(0) }
    val snapshot = remember(diagnosticsRevision) { AlarmDiagnostics.snapshot(context) }
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale("tr", "TR")) }
    fun formatTime(value: Long): String = if (value > 0L) formatter.format(Date(value)) else "Kayıt yok"
    fun stateText(ok: Boolean) = if (ok) "Açık" else "İzin gerekli"

    val technicalText = remember(
        snapshot,
        refreshResult,
        exactAlarmAllowed,
        notificationAllowed,
        fullScreenAllowed,
        batteryExcluded
    ) {
        buildString {
            appendLine("Şifahane kişisel verisiz alarm tanılaması")
            appendLine("Uygulama: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
            appendLine("Android API: ${Build.VERSION.SDK_INT}")
            appendLine("Bildirim izni: ${stateText(notificationAllowed)}")
            appendLine("Kesin alarm izni: ${stateText(exactAlarmAllowed)}")
            appendLine("Tam ekran alarm izni: ${stateText(fullScreenAllowed)}")
            appendLine("Pil optimizasyonu muafiyeti: ${stateText(batteryExcluded)}")
            appendLine("Alarm ekranı etkin: ${if (snapshot.activeScreen) "Evet" else "Hayır"}")
            appendLine("Etkin alarm kimliği: ${snapshot.activeGroupHash.ifBlank { "-" }}")
            appendLine("Son tetikleme: ${formatTime(snapshot.lastTriggeredAt)}")
            appendLine("Son alarm kimliği: ${snapshot.lastGroupHash.ifBlank { "-" }}")
            appendLine("Son gruptaki öğe sayısı: ${snapshot.lastDoseCount}")
            appendLine("Son bildirim kimliği: ${snapshot.lastNotificationId}")
            appendLine("Son istek kodu: ${snapshot.lastRequestCode}")
            appendLine("Son erteleme seçimi: ${snapshot.lastSnoozeMinutes} dk")
            appendLine("Son erteleme hedefi: ${formatTime(snapshot.lastSnoozeTriggerAt)}")
            appendLine("Sonraki planlanan alarm: ${formatTime(snapshot.nextScheduledAt)}")
            appendLine("Sonraki alarm kimliği: ${snapshot.nextGroupHash.ifBlank { "-" }}")
            appendLine("Son planlama türü: ${snapshot.nextPlanningType.ifBlank { "Bilinmiyor" }}")
            appendLine("Son otomatik yeniden planlama: ${formatTime(snapshot.lastAutomaticRescheduleAt)}")
            appendLine("Yeniden planlama nedeni: ${snapshot.lastAutomaticRescheduleReason.ifBlank { "Kayıt yok" }}")
            appendLine("Yeniden planlama sonucu: ${if (snapshot.lastAutomaticRescheduleSucceeded) "Başarılı" else "Başarısız/Kayıt yok"}")
            refreshResult?.let {
                appendLine("Aktif ilaç sayısı: ${it.medicationCount}")
                appendLine("Gelecek alarm sayısı: ${it.futureAlarmCount}")
                appendLine("Geçmiş yanıtsız doz sayısı: ${it.catchUpAlarmCount}")
                appendLine("Bekleyen erteleme sayısı: ${it.snoozeAlarmCount}")
            }
        }.trim()
    }

    fun openSpecialSettings(action: String) {
        runCatching {
            context.startActivity(Intent(action, Uri.parse("package:${context.packageName}")))
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Alarm Durumu",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().heightIn(max = 540.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Text("Bildirim izni: ${stateText(notificationAllowed)}") }
                item { Text("Kesin alarm izni: ${stateText(exactAlarmAllowed)}") }
                item { Text("Tam ekran alarm izni: ${stateText(fullScreenAllowed)}") }
                item { Text("Pil optimizasyonu muafiyeti: ${stateText(batteryExcluded)}") }
                item { Text("Alarm ekranı etkin: ${if (snapshot.activeScreen) "Evet" else "Hayır"}") }
                item { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) }
                item { Text("Son alarm: ${formatTime(snapshot.lastTriggeredAt)}") }
                item { Text("Anonim alarm kimliği: ${snapshot.lastGroupHash.ifBlank { "-" }}") }
                item { Text("Son gruptaki öğe sayısı: ${snapshot.lastDoseCount}") }
                item { Text("Son erteleme: ${snapshot.lastSnoozeMinutes} dk → ${formatTime(snapshot.lastSnoozeTriggerAt)}") }
                item { Text("Sonraki planlanan alarm: ${formatTime(snapshot.nextScheduledAt)}") }
                item { Text("Planlama türü: ${snapshot.nextPlanningType.ifBlank { "Bilinmiyor" }}") }
                item { Text("Son otomatik yeniden planlama: ${formatTime(snapshot.lastAutomaticRescheduleAt)}") }
                item { Text("Neden: ${snapshot.lastAutomaticRescheduleReason.ifBlank { "Kayıt yok" }}") }
                item { Text("Sonuç: ${if (snapshot.lastAutomaticRescheduleSucceeded) "Başarılı" else "Başarısız/Kayıt yok"}") }
                refreshResult?.let { result ->
                    item { HorizontalDivider(color = MaterialTheme.colorScheme.primary) }
                    item { Text("Aktif ilaç: ${result.medicationCount}") }
                    item { Text("Gelecek alarm: ${result.futureAlarmCount}") }
                    item { Text("Geçmiş ve yanıtsız doz: ${result.catchUpAlarmCount}") }
                    item { Text("Bekleyen erteleme: ${result.snoozeAlarmCount}") }
                }
                item {
                    Button(
                        onClick = {
                            AlarmScheduler.scheduleTest(context, System.currentTimeMillis() + 10_000L)
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) { Text("10 SANİYE SONRA DENEME ALARMI") }
                }
                if (!exactAlarmAllowed && Build.VERSION.SDK_INT >= 31) {
                    item {
                        OutlinedButton(
                            onClick = { openSpecialSettings(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        ) { Text("KESİN ALARM İZNİNİ AÇ") }
                    }
                }
                if (!fullScreenAllowed && Build.VERSION.SDK_INT >= 34) {
                    item {
                        OutlinedButton(
                            onClick = { openSpecialSettings(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        ) { Text("TAM EKRAN ALARM İZNİNİ AÇ") }
                    }
                }
                if (!batteryExcluded) {
                    item {
                        OutlinedButton(
                            onClick = { openSpecialSettings(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        ) { Text("PİL OPTİMİZASYONUNDAN MUAF TUT") }
                    }
                }
                item {
                    OutlinedButton(
                        onClick = {
                            AlarmDiagnostics.clearHistory(context)
                            diagnosticsRevision++
                            android.widget.Toast.makeText(
                                context,
                                "Tanılama geçmişi temizlendi.",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) { Text("TANILAMA GEÇMİŞİNİ TEMİZLE") }
                }
                item {
                    OutlinedButton(
                        onClick = {
                            context.getSystemService(ClipboardManager::class.java)
                                .setPrimaryClip(ClipData.newPlainText("Şifahane alarm tanılaması", technicalText))
                            android.widget.Toast.makeText(
                                context,
                                "Kişisel veri içermeyen tanılama panoya kopyalandı.",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) { Text("TANILAMAYI KOPYALA") }
                }
                item {
                    OutlinedButton(
                        onClick = {
                            context.startActivity(
                                Intent.createChooser(
                                    Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "Şifahane alarm tanılaması")
                                        putExtra(Intent.EXTRA_TEXT, technicalText)
                                    },
                                    "Kişisel verisiz tanılamayı paylaş"
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) { Text("TANILAMAYI PAYLAŞ") }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Kapat") } }
    )
}
