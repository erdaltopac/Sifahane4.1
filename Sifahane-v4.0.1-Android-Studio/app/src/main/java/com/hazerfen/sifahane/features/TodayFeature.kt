package com.hazerfen.sifahane.features

import com.hazerfen.sifahane.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.animation.animateColorAsState
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.LinearEasing
import com.hazerfen.sifahane.alarm.AlarmRescheduler
import com.hazerfen.sifahane.alarm.AlarmRefreshResult
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlinx.coroutines.Job
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.Animatable
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import com.hazerfen.sifahane.security.AdminPinStore
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Lifecycle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import com.hazerfen.sifahane.security.PatternStore
import com.hazerfen.sifahane.security.AdminCredentialHasher
import com.hazerfen.sifahane.security.BiometricPreferences
import com.hazerfen.sifahane.security.FirstRunSecurityPolicy
import com.hazerfen.sifahane.security.LegacyPlaceholderPolicy
import com.hazerfen.sifahane.security.UserRoles
import com.hazerfen.sifahane.validation.HealthDataValidator
import androidx.compose.ui.graphics.Shadow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import com.hazerfen.sifahane.ui.SifahaneTheme
import com.hazerfen.sifahane.ui.ThemeConfiguration
import com.hazerfen.sifahane.ui.ThemePreferences
import com.hazerfen.sifahane.ui.ThemeMode
import com.hazerfen.sifahane.ui.ThemePreset
import com.hazerfen.sifahane.ui.ThemeFont
import com.hazerfen.sifahane.ui.SifahaneCard
import com.hazerfen.sifahane.ui.AlarmStatusDialog
import com.hazerfen.sifahane.ui.OutlinedLogoIcon
import com.hazerfen.sifahane.ui.sifahaneSoftBoundary
import org.json.JSONObject
import org.json.JSONArray
import java.util.zip.ZipOutputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipEntry
import java.io.BufferedOutputStream
import java.io.BufferedInputStream
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.result.contract.ActivityResultContracts.OpenDocumentTree
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.background
import androidx.compose.ui.zIndex
import androidx.compose.ui.draw.alpha
import kotlinx.coroutines.delay
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import android.graphics.Color as AndroidColor
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import android.app.Activity
import android.view.WindowManager
import com.google.mlkit.vision.barcode.common.Barcode
import kotlin.math.min
import kotlin.math.max
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import android.provider.OpenableColumns
import android.graphics.RectF
import android.graphics.Paint
import android.graphics.Matrix
import android.graphics.Canvas as AndroidCanvas
import java.io.FileOutputStream
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.foundation.gestures.detectTransformGestures
import android.graphics.BitmapFactory
import android.graphics.Bitmap

import android.app.*
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.withTransaction
import coil.compose.rememberAsyncImagePainter
import com.hazerfen.sifahane.alarm.AlarmScheduler
import com.hazerfen.sifahane.alarm.AppointmentPreferences
import com.hazerfen.sifahane.alarm.AppointmentAlarmScheduler
import com.hazerfen.sifahane.data.*
import com.hazerfen.sifahane.export.SimpleXlsxWriter
import com.hazerfen.sifahane.diagnostics.SupportDiagnostics
import com.hazerfen.sifahane.backup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
internal fun TodayScreen(
    meds: List<Medication>,
    logs: List<DoseLog>,
    appointments: List<Appointment>,
    modifier: Modifier,
    onOpenAppointments: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = remember(context) { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale("tr", "TR")) }
    var selectedDose by remember { mutableStateOf<DailyDose?>(null) }
    var selectedDoseLog by remember { mutableStateOf<DoseLog?>(null) }
    var selectedTakenTime by remember { mutableStateOf<Long?>(null) }
    var takenTimeMenuExpanded by remember { mutableStateOf(false) }
    var undoDose by remember { mutableStateOf<DailyDose?>(null) }
    var undoDoseLog by remember { mutableStateOf<DoseLog?>(null) }
    var restoreLegacyStock by remember { mutableStateOf(true) }
    val doses = meds.filter {
        it.active && !it.archived && it.startDate <= today && (it.continuous || it.endDate == null || it.endDate >= today)
    }.flatMap { med ->
        med.timesCsv.split(",").mapNotNull { t ->
            val p = t.trim().split(":")
            if (p.size == 2) DailyDose(med, t.trim(), (p[0].toIntOrNull() ?: 0) * 60 + (p[1].toIntOrNull() ?: 0)) else null
        }
    }.sortedBy { it.minutes }

    val nextAppointment = appointments
        .filter { AppointmentStatus.fromStorage(it.status) == AppointmentStatus.PLANNED && it.active && it.appointmentDateTime >= System.currentTimeMillis() }
        .minByOrNull { it.appointmentDateTime }

    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        nextAppointment?.let { appointment ->
            item {
                AppointmentSummaryCard(
                    item = appointment,
                    contextLabel = if (
                        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(appointment.appointmentDateTime)) == today
                    ) "Bugünkü Doktor Randevusu" else "Yaklaşan Doktor Randevusu",
                    onClick = onOpenAppointments,
                    footer = {
                        Text(
                            "Randevu ayrıntılarını açmak için dokunun.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )
            }
        }
        item {
            Text(
                "Bugünkü Tedavi Planı",
                modifier = Modifier.fillMaxWidth(),
                color = LogoColorDark,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    shadow = LogoTextShadow
                )
            )
        }
        items(doses, key = { "${it.medication.id}-${it.time}" }) { d ->
            val plannedForCard = Calendar.getInstance().apply {
                val parts = d.time.split(":")
                set(Calendar.HOUR_OF_DAY, parts.getOrNull(0)?.toIntOrNull() ?: 0)
                set(Calendar.MINUTE, parts.getOrNull(1)?.toIntOrNull() ?: 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            val doseLog = logs
                .filter {
                    it.medicationId == d.medication.id &&
                    SimpleDateFormat("yyyy-MM-dd", Locale.US)
                        .format(Date(it.scheduledDateTime)) == today &&
                    kotlin.math.abs(it.scheduledDateTime - plannedForCard) < 90 * 60 * 1000L
                }
                .maxByOrNull { it.timestamp }

            SifahaneCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                        selectedDose = d
                        selectedDoseLog = doseLog
                        selectedTakenTime = doseLog
                            ?.takeIf { it.action == DoseAction.TAKEN.storageValue }
                            ?.actualDateTime
                    }
            ) {
                Column {
                    if (!d.medication.photoUri.isNullOrBlank()) {
                        Image(
                            rememberAsyncImagePainter(d.medication.photoUri),
                            null,
                            Modifier.fillMaxWidth().height(260.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    HorizontalDivider(thickness = 2.dp, color = LogoColor.copy(alpha = 0.85f))
                    Column(Modifier.padding(16.dp)) {
                        Text(d.time, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(d.medication.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        if (d.medication.purpose.isNotBlank()) Text(d.medication.purpose)
                        Text(d.medication.dose)
                        if (d.medication.notes.isNotBlank()) Text("Not: ${d.medication.notes}")
                        when (doseLog?.action) {
                            DoseAction.TAKEN.storageValue -> Text(
                                "Alındı • ${doseLog.actualDateTime?.let { timeFormatter.format(Date(it)) } ?: "-"}",
                                color = LogoColorDark,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    shadow = LogoTextShadow
                                )
                            )
                            "10 DK ERTELENDİ" -> Text(
                                "10 Dakika Ertelendi",
                                color = LogoColorDark,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    shadow = LogoTextShadow
                                )
                            )
                            DoseAction.SKIPPED.storageValue -> Text(
                                "Bugün Alınmayacak",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            "ALINDI GERİ ALINDI" -> Text(
                                "Henüz Alınmadı • Önceki alındı kaydı geri alındı",
                                color = LogoColorDark.copy(alpha = 0.82f),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            else -> Text(
                                "Henüz Alınmadı",
                                color = LogoColorDark.copy(alpha = 0.72f)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Alarm seçeneklerini açmak için karta dokunun",
                            color = LogoColorDark,
                            style = MaterialTheme.typography.bodySmall.copy(
                                shadow = LogoTextShadow
                            )
                        )
                    }
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 3.dp, color = LogoColor)
        }
    }

    selectedDose?.let { dose ->
        val planned = Calendar.getInstance().apply {
            val parts = dose.time.split(":")
            set(Calendar.HOUR_OF_DAY, parts.getOrNull(0)?.toIntOrNull() ?: 0)
            set(Calendar.MINUTE, parts.getOrNull(1)?.toIntOrNull() ?: 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        AlertDialog(
            onDismissRequest = { selectedDose = null; selectedDoseLog = null; selectedTakenTime = null; takenTimeMenuExpanded = false },
            title = {
                Text(
                    dose.medication.name,
                    modifier = Modifier.fillMaxWidth(),
                    color = LogoColorDark,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        shadow = LogoTextShadow
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Planlanan Saat: ${dose.time}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    Text(dose.medication.dose, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    if (dose.medication.purpose.isNotBlank()) {
                        Text(dose.medication.purpose, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    }

                    if (selectedDoseLog?.action == DoseAction.TAKEN.storageValue) {
                        Text(
                            "Kayıtlı Alınma Saati: ${
                                selectedDoseLog?.actualDateTime?.let {
                                    timeFormatter.format(Date(it))
                                } ?: "-"
                            }",
                            color = LogoColorDark,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                shadow = LogoTextShadow
                            )
                        )
                    }

                    fun saveTakenTime(takenAt: Long) {
                        scope.launch {
                            AlarmScheduler.cancelSnooze(
                                context,
                                dose.medication.id
                            )
                            val result = DoseActionRepository.recordTaken(
                                db = db,
                                profileId = dose.medication.profileId,
                                medicationId = dose.medication.id,
                                medicationName = dose.medication.name,
                                scheduledDateTime = planned,
                                actualDateTime = takenAt
                            )
                            selectedTakenTime = takenAt
                            selectedDoseLog = result.log
                            takenTimeMenuExpanded = false
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "İlacın Alındığı Saat",
                            modifier = Modifier.fillMaxWidth().sifahaneSoftBoundary(2.dp),
                            color = LogoColorDark,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                shadow = LogoTextShadow
                            )
                        )

                        Spacer(Modifier.height(5.dp))

                        Box(
                            modifier = Modifier.width(230.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            OutlinedButton(
                                onClick = { takenTimeMenuExpanded = true },
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.5.dp, LogoColor),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Vantablack05,
                                    contentColor = LogoColorDark
                                )
                            ) {
                                Text(
                                    selectedTakenTime?.let {
                                        timeFormatter.format(Date(it))
                                    } ?: "Saat Seçiniz",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        shadow = LogoTextShadow
                                    )
                                )
                            }

                            DropdownMenu(
                                expanded = takenTimeMenuExpanded,
                                onDismissRequest = {
                                    takenTimeMenuExpanded = false
                                },
                                modifier = Modifier
                                    .width(230.dp)
                                    .border(
                                        1.5.dp,
                                        LogoColor,
                                        RoundedCornerShape(14.dp)
                                    ),
                                shape = RoundedCornerShape(14.dp),
                                containerColor = Color(0xFFF2F2F2),
                                tonalElevation = 0.dp,
                                shadowElevation = 6.dp
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Zamanında",
                                            modifier = Modifier.fillMaxWidth(),
                                            color = LogoColorDark,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                shadow = LogoTextShadow
                                            )
                                        )
                                    },
                                    onClick = { saveTakenTime(planned) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Vantablack05)
                                )
                                HorizontalDivider(
                                    color = LogoColor.copy(alpha = 0.45f)
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Şimdi",
                                            modifier = Modifier.fillMaxWidth(),
                                            color = LogoColorDark,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                shadow = LogoTextShadow
                                            )
                                        )
                                    },
                                    onClick = {
                                        saveTakenTime(System.currentTimeMillis())
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Vantablack05)
                                )
                                HorizontalDivider(
                                    color = LogoColor.copy(alpha = 0.45f)
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Saat Seç",
                                            modifier = Modifier.fillMaxWidth(),
                                            color = LogoColorDark,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                shadow = LogoTextShadow
                                            )
                                        )
                                    },
                                    onClick = {
                                        takenTimeMenuExpanded = false
                                        val initial = Calendar.getInstance().apply {
                                            timeInMillis = selectedTakenTime
                                                ?: System.currentTimeMillis()
                                        }
                                        TimePickerDialog(
                                            context,
                                            { _, hour, minute ->
                                                val chosen = Calendar.getInstance().apply {
                                                    set(Calendar.HOUR_OF_DAY, hour)
                                                    set(Calendar.MINUTE, minute)
                                                    set(Calendar.SECOND, 0)
                                                    set(Calendar.MILLISECOND, 0)
                                                }.timeInMillis
                                                saveTakenTime(chosen)
                                            },
                                            initial.get(Calendar.HOUR_OF_DAY),
                                            initial.get(Calendar.MINUTE),
                                            true
                                        ).show()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Vantablack05)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                saveTakenTime(
                                    selectedTakenTime ?: System.currentTimeMillis()
                                )
                                selectedDose = null
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LogoColor,
                            contentColor = Color(0xFF123A37)
                        )
                    ) {
                        Text(
                            "İLACI ALDIM",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium.copy(
                                shadow = LogoTextShadow
                            )
                        )
                    }

                    selectedDoseLog
                        ?.takeIf { it.action == DoseAction.TAKEN.storageValue }
                        ?.let { takenLog ->
                            OutlinedButton(
                                onClick = {
                                    undoDose = dose
                                    undoDoseLog = takenLog
                                    restoreLegacyStock = takenLog.stockDecreased ?: true
                                    selectedDose = null
                                    selectedDoseLog = null
                                    selectedTakenTime = null
                                    takenTimeMenuExpanded = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.28f),
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(Icons.Default.Undo, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "ALINDI KAYDINI GERİ AL",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                AlarmScheduler.cancelSnooze(
                                    context,
                                    dose.medication.id
                                )
                                val snoozeAt = System.currentTimeMillis() + 10 * 60_000L
                                AlarmScheduler.snoozeAt(
                                    context,
                                    dose.medication,
                                    dose.time,
                                    planned,
                                    snoozeAt
                                )
                                db.doseLogDao().insert(
                                    DoseLog(
                                        profileId = dose.medication.profileId,
                                        medicationId = dose.medication.id,
                                        medicationName = dose.medication.name,
                                        scheduledDateTime = planned,
                                        actualDateTime = snoozeAt,
                                        action = "10 DK ERTELENDİ"
                                    )
                                )
                                selectedDose = null
                                selectedDoseLog = null
                                selectedTakenTime = null
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.5.dp, LogoColor),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Vantablack10,
                            contentColor = LogoColorDark
                        )
                    ) {
                        Text("10 DK ERTELE")
                    }

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                AlarmScheduler.cancelSnooze(
                                    context,
                                    dose.medication.id
                                )
                                db.doseLogDao().insert(
                                    DoseLog(
                                        profileId = dose.medication.profileId,
                                        medicationId = dose.medication.id,
                                        medicationName = dose.medication.name,
                                        scheduledDateTime = planned,
                                        actualDateTime = System.currentTimeMillis(),
                                        action = DoseAction.SKIPPED.storageValue
                                    )
                                )
                                selectedDose = null
                                selectedDoseLog = null
                                selectedTakenTime = null
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.5.dp, LogoColor),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Vantablack10,
                            contentColor = LogoColorDark
                        )
                    ) {
                        Text("BUGÜN ALMAYACAĞIM")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = {
                            selectedDose = null
                            selectedDoseLog = null
                            selectedTakenTime = null
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Vantablack10,
                            contentColor = LogoColorDark
                        )
                    ) {
                        Text("Kapat", textAlign = TextAlign.Center)
                    }
                }
            }
        )
    }

    val pendingUndoDose = undoDose
    val pendingUndoLog = undoDoseLog
    if (pendingUndoDose != null && pendingUndoLog != null) {
        AlertDialog(
            onDismissRequest = {
                undoDose = null
                undoDoseLog = null
            },
            title = {
                Text(
                    "Alındı Kaydını Geri Al",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "${pendingUndoDose.medication.name} için ${pendingUndoDose.time} dozuna ait ‘Alındı’ kaydı geri alınacak. Doz yeniden ‘Henüz Alınmadı’ durumuna dönecektir.",
                        textAlign = TextAlign.Center
                    )
                    when (pendingUndoLog.stockDecreased) {
                        true -> Text(
                            "Bu kayıt oluşturulurken stoktan 1 adet düşülmüştü. Geri alma işleminde stoka 1 adet eklenecek.",
                            fontWeight = FontWeight.Bold
                        )
                        false -> Text(
                            "Bu kayıt oluşturulurken stok azaltılmamıştı. Geri alma işleminde stok değişmeyecek.",
                            fontWeight = FontWeight.Bold
                        )
                        null -> {
                            Text(
                                "Bu kayıt önceki bir Şifahane sürümünde oluşturulduğu için stok hareketi kesin olarak belirlenemiyor.",
                                color = MaterialTheme.colorScheme.error
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { restoreLegacyStock = !restoreLegacyStock },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = restoreLegacyStock,
                                    onCheckedChange = { restoreLegacyStock = it }
                                )
                                Text("Stoka 1 adet geri ekle")
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            var undoResult: DoseActionResult? = null
                            runCatching {
                                undoResult = DoseActionRepository.undoTaken(
                                    db = db,
                                    takenLog = pendingUndoLog,
                                    restoreLegacyStock = restoreLegacyStock
                                )
                                AlarmScheduler.cancelSnooze(
                                    context,
                                    pendingUndoDose.medication.id
                                )
                            }.onSuccess {
                                val result = undoResult
                                android.widget.Toast.makeText(
                                    context,
                                    when {
                                        result?.duplicateSuppressed == true -> "Bu alındı kaydı daha önce geri alınmış."
                                        result?.stockChanged == true -> "Alındı kaydı geri alındı ve stok düzeltildi."
                                        else -> "Alındı kaydı geri alındı."
                                    },
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }.onFailure {
                                android.widget.Toast.makeText(
                                    context,
                                    "Kayıt geri alınamadı: ${it.message ?: "Bilinmeyen hata"}",
                                    android.widget.Toast.LENGTH_LONG
                                ).show()
                            }
                            undoDose = null
                            undoDoseLog = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Evet, Kaydı Geri Al")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        undoDose = null
                        undoDoseLog = null
                    }
                ) {
                    Text("İptal")
                }
            }
        )
    }

}


