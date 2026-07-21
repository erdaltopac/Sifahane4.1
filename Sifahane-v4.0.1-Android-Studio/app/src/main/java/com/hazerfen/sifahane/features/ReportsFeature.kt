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
import com.hazerfen.sifahane.alarm.AlarmTimePolicy
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
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal data class AdherenceSummary(
    val planned: Int,
    val taken: Int,
    val skipped: Int,
    val unanswered: Int,
    val postponedEvents: Int
)

internal fun calculateAdherenceSummary(
    medications: List<Medication>,
    logs: List<DoseLog>,
    fromMillis: Long,
    toMillis: Long,
    nowMillis: Long = System.currentTimeMillis(),
    zoneId: ZoneId = ZoneId.systemDefault()
): AdherenceSummary {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    val lower = java.time.Instant.ofEpochMilli(fromMillis).atZone(zoneId).toLocalDate()
    val upperMillis = minOf(toMillis, nowMillis)
    val upper = java.time.Instant.ofEpochMilli(upperMillis).atZone(zoneId).toLocalDate()
    var planned = 0
    medications.forEach { medication ->
        val times = AlarmScheduler.medicationTimes(medication)
        if (times.isEmpty()) return@forEach
        val medicationStart = runCatching { LocalDate.parse(medication.startDate, formatter) }.getOrNull() ?: lower
        val medicationEnd = if (medication.continuous || medication.endDate.isNullOrBlank()) upper else
            runCatching { LocalDate.parse(medication.endDate, formatter) }.getOrNull() ?: upper
        var day = maxOf(lower, medicationStart)
        val lastDay = minOf(upper, medicationEnd)
        var safetyDays = 0
        while (!day.isAfter(lastDay) && safetyDays++ < 20_000) {
            times.forEach { time ->
                val occurrence = AlarmTimePolicy.plannedForDate(day, time, zoneId)
                if (occurrence != null && occurrence in fromMillis..upperMillis) planned++
            }
            day = day.plusDays(1)
        }
    }
    val latest = logs
        .filter { it.scheduledDateTime in fromMillis..upperMillis }
        .groupBy { it.medicationId to it.scheduledDateTime }
        .mapNotNull { (_, entries) -> entries.maxByOrNull(DoseLog::timestamp) }
    val taken = latest.count { DoseAction.fromStorage(it.action) == DoseAction.TAKEN }
    val skipped = latest.count { DoseAction.fromStorage(it.action) == DoseAction.SKIPPED }
    val postponed = logs.count { it.timestamp in fromMillis..upperMillis && it.action.endsWith(" DK ERTELENDİ") }
    return AdherenceSummary(
        planned = planned,
        taken = taken,
        skipped = skipped,
        unanswered = (planned - taken - skipped).coerceAtLeast(0),
        postponedEvents = postponed
    )
}

@Composable
internal fun MeasurementsScreen(
    tab: MeasureTab,
    onTab: (MeasureTab) -> Unit,
    bp: List<BloodPressure>,
    glucose: List<BloodGlucose>,
    modifier: Modifier,
    onEditBp: (BloodPressure) -> Unit,
    onEditGlucose: (BloodGlucose) -> Unit,
    onAddMeasurement: () -> Unit
) {
    Column(modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabRow(
                selectedTabIndex = if (tab == MeasureTab.BP) 0 else 1,
                modifier = Modifier.weight(1f),
                containerColor = Color.Transparent,
                contentColor = LogoColor,
                indicator = { tabPositions ->
                    val selectedIndex = if (tab == MeasureTab.BP) 0 else 1
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                        color = LogoColor
                    )
                }
            ) {
                Tab(
                    selected = tab == MeasureTab.BP,
                    onClick = { onTab(MeasureTab.BP) },
                    icon = { OutlinedLogoIcon(Icons.Default.Favorite, null, size = 30.dp) },
                    text = { Text("Tansiyon", color = LogoColorDark, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, shadow = LogoTextShadow)) }
                )
                Tab(
                    selected = tab == MeasureTab.GLUCOSE,
                    onClick = { onTab(MeasureTab.GLUCOSE) },
                    icon = { OutlinedLogoIcon(Icons.Default.Bloodtype, null, size = 30.dp) },
                    text = { Text("Kan Şekeri", color = LogoColorDark, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, shadow = LogoTextShadow)) }
                )
            }
            Column(
                modifier = Modifier.width(72.dp).heightIn(min = 72.dp).clickable(onClick = onAddMeasurement),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedLogoIcon(Icons.Default.AddCircle, "Yeni ölçüm ekle", size = 30.dp)
                Spacer(Modifier.height(4.dp))
                Text("Ekle", color = LogoColorDark, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, shadow = LogoTextShadow))
            }
        }
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (tab == MeasureTab.BP) {
                if (bp.isEmpty()) item { Text("Henüz tansiyon kaydı yok. Sağ üstteki + düğmesine basın.") }
                items(bp, key = { it.id }) { x ->
                    SifahaneCard(modifier = Modifier.fillMaxWidth(), onClick = { onEditBp(x) }) {
                        Column(Modifier.padding(14.dp)) {
                            Text("${x.systolic}/${x.diastolic} mmHg", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("Nabız: ${x.pulse ?: "-"}")
                            Text(formatDateTime(x.measuredAt))
                            if (x.note.isNotBlank()) Text(x.note)
                            Text("Düzenlemek veya silmek için dokunun.", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            } else {
                if (glucose.isEmpty()) item { Text("Henüz kan şekeri kaydı yok. Sağ üstteki + düğmesine basın.") }
                items(glucose, key = { it.id }) { x ->
                    SifahaneCard(modifier = Modifier.fillMaxWidth(), onClick = { onEditGlucose(x) }) {
                        Column(Modifier.padding(14.dp)) {
                            Text("${x.valueMgDl} mg/dL", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(x.measurementType)
                            Text(formatDateTime(x.measuredAt))
                            if (x.note.isNotBlank()) Text(x.note)
                            Text("Düzenlemek veya silmek için dokunun.", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ReportsScreen(profileId: Long, db: AppDatabase, modifier: Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    var from by remember { mutableStateOf<Long?>(null) }
    var to by remember { mutableStateOf<Long?>(null) }
    var status by remember { mutableStateOf("") }
    var confirmDelete by remember { mutableStateOf(false) }
    var includeBpChart by remember { mutableStateOf(true) }
    var includeGlucoseChart by remember { mutableStateOf(true) }
    var bpSelectionTouched by remember { mutableStateOf(false) }
    var glucoseSelectionTouched by remember { mutableStateOf(false) }
    var chartBp by remember { mutableStateOf<List<BloodPressure>>(emptyList()) }
    var chartGlucose by remember { mutableStateOf<List<BloodGlucose>>(emptyList()) }
    var chartGenerated by remember { mutableStateOf(false) }
    var allRecordsSelected by remember { mutableStateOf(false) }
    var excelGenerated by remember { mutableStateOf(false) }
    var deleteCompleted by remember { mutableStateOf(false) }
    val reportControlShape = RoundedCornerShape(10.dp)
    val allReportMedications by db.medicationDao().observeAllMedications()
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val reportLogs by db.doseLogDao().observeForProfile(profileId)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val nowForSummary = System.currentTimeMillis()
    val earliestRelevant = remember(allReportMedications, reportLogs) {
        val medicationDates = allReportMedications.filter { it.profileId == profileId }.mapNotNull {
            runCatching { LocalDate.parse(it.startDate, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() }.getOrNull()
        }
        (medicationDates + reportLogs.map(DoseLog::scheduledDateTime)).minOrNull()
            ?: (nowForSummary - 30L * 24L * 60L * 60L * 1000L)
    }
    val summaryFrom = from ?: earliestRelevant
    val summaryTo = (to ?: nowForSummary).let { value ->
        if (to != null) value + 24L * 60L * 60L * 1000L - 1L else value
    }
    val adherenceSummary = remember(allReportMedications, reportLogs, summaryFrom, summaryTo, nowForSummary) {
        calculateAdherenceSummary(
            medications = allReportMedications.filter { it.profileId == profileId },
            logs = reportLogs,
            fromMillis = summaryFrom,
            toMillis = summaryTo,
            nowMillis = nowForSummary
        )
    }

    Column(
        modifier
            .fillMaxSize()
            .vantablackPageGlassOverlay()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Raporlar",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                shadow = LogoTextShadow
            ),
            color = LogoColorDark
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f)) {
                Text(
                    "Başlangıç Tarihi",
                    modifier = Modifier.fillMaxWidth(),
                    color = LogoColorDark,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium.copy(
                        shadow = LogoTextShadow
                    )
                )
                OutlinedButton(
                    onClick = { pickDate(context) { from = it; allRecordsSelected = false } },
                    modifier = Modifier.fillMaxWidth().height(52.dp).sifahaneSoftBoundary(5.dp).vantablackGlassOverlay(),
                    shape = reportControlShape,
                    border = BorderStroke(1.dp, Color.Transparent),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = LogoColor.copy(alpha = 0.50f),
                        contentColor = LogoColorDark
                    )
                ) {
                    Text(
                        from?.let { formatDate(it) } ?: "Seçilmedi",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (from != null) FontWeight.Bold else FontWeight.Normal,
                            shadow = LogoTextShadow
                        )
                    )
                }
            }
            Column(Modifier.weight(1f)) {
                Text(
                    "Bitiş Tarihi",
                    modifier = Modifier.fillMaxWidth(),
                    color = LogoColorDark,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium.copy(
                        shadow = LogoTextShadow
                    )
                )
                OutlinedButton(
                    onClick = { pickDate(context) { to = it; allRecordsSelected = false } },
                    modifier = Modifier.fillMaxWidth().height(52.dp).sifahaneSoftBoundary(5.dp).vantablackGlassOverlay(),
                    shape = reportControlShape,
                    border = BorderStroke(1.dp, Color.Transparent),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = LogoColor.copy(alpha = 0.50f),
                        contentColor = LogoColorDark
                    )
                ) {
                    Text(
                        to?.let { formatDate(it) } ?: "Seçilmedi",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (to != null) FontWeight.Bold else FontWeight.Normal,
                            shadow = LogoTextShadow
                        )
                    )
                }
            }
        }
        OutlinedButton(
            onClick = { from = null; to = null; allRecordsSelected = true },
            modifier = Modifier.fillMaxWidth().height(52.dp).sifahaneSoftBoundary(5.dp).vantablackGlassOverlay(),
            shape = reportControlShape,
            border = BorderStroke(1.dp, Color.Transparent),
            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = LogoColor.copy(alpha = 0.50f),
                                contentColor = LogoColorDark
                            )
        ) {
            Text(
                "Tüm Kayıtlar",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (allRecordsSelected) FontWeight.Bold else FontWeight.Normal,
                    shadow = LogoTextShadow
                )
            )
        }


        SifahaneCard(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "Planlanan doz özeti",
                    color = LogoColorDark,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, shadow = LogoTextShadow)
                )
                Text("Planlanan: ${adherenceSummary.planned} · Alınan: ${adherenceSummary.taken} · Alınmadı: ${adherenceSummary.skipped}")
                Text("Henüz yanıtlanmamış: ${adherenceSummary.unanswered} · Erteleme olayı: ${adherenceSummary.postponedEvents}")
                Text(
                    "Hesap yöntemi: seçilen tarih aralığında kayıtlı ilaç saatlerinden bugüne kadar oluşmuş planlar sayılır. Her doz için son Alındı/Alınmadı yanıtı kullanılır; erteleme ayrı olay olarak gösterilir. Bu özet yargılayıcı başarı yüzdesi üretmez.",
                    style = MaterialTheme.typography.bodySmall,
                    color = LogoColorDark
                )
            }
        }


        SifahaneCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Grafik Rapor",
                    color = LogoColorDark,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        shadow = LogoTextShadow
                    )
                )
                Box(
                    Modifier.fillMaxWidth().heightIn(min = 52.dp)
                        .background(LogoColor.copy(alpha = 0.50f), reportControlShape)
                        .sifahaneSoftBoundary(5.dp).vantablackGlassOverlay().padding(horizontal = 8.dp)
                ) {
                    MedicationCheckBox(
                        checked = includeBpChart,
                        onCheckedChange = { includeBpChart = it; bpSelectionTouched = true },
                        label = "Tansiyon Grafiği",
                        centered = true,
                        boldWhenChecked = true,
                        emphasized = bpSelectionTouched && includeBpChart,
                        checkedColor = LogoColor.copy(alpha = 0.50f)
                    )
                }
                Box(
                    Modifier.fillMaxWidth().heightIn(min = 52.dp)
                        .background(LogoColor.copy(alpha = 0.50f), reportControlShape)
                        .sifahaneSoftBoundary(5.dp).vantablackGlassOverlay().padding(horizontal = 8.dp)
                ) {
                    MedicationCheckBox(
                        checked = includeGlucoseChart,
                        onCheckedChange = { includeGlucoseChart = it; glucoseSelectionTouched = true },
                        label = "Kan Şekeri Grafiği",
                        centered = true,
                        boldWhenChecked = true,
                        emphasized = glucoseSelectionTouched && includeGlucoseChart,
                        checkedColor = LogoColor.copy(alpha = 0.50f)
                    )
                }
                Button(
                    enabled = includeBpChart || includeGlucoseChart,
                    onClick = {
                        scope.launch {
                            val start = from ?: Long.MIN_VALUE
                            val endExclusive = to?.plus(86_400_000L) ?: Long.MAX_VALUE
                            chartBp = if (includeBpChart) {
                                withContext(Dispatchers.IO) {
                                    db.vitalsDao().allBp(profileId)
                                        .filter { it.measuredAt in start until endExclusive }
                                        .sortedBy { it.measuredAt }
                                }
                            } else emptyList()
                            chartGlucose = if (includeGlucoseChart) {
                                withContext(Dispatchers.IO) {
                                    db.vitalsDao().allGlucose(profileId)
                                        .filter { it.measuredAt in start until endExclusive }
                                        .sortedBy { it.measuredAt }
                                }
                            } else emptyList()
                            chartGenerated = true
                            status = "Grafik Rapor Oluşturuldu."
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp).sifahaneSoftBoundary(5.dp).vantablackGlassOverlay(),
                    shape = reportControlShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoColor.copy(alpha = 0.50f),
                        contentColor = LogoColorDark,
                        disabledContainerColor = LogoColor.copy(alpha = 0.50f),
                        disabledContentColor = LogoColorDark.copy(alpha = 0.25f)
                    )
                ) {
                    Icon(Icons.Default.ShowChart, null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Grafik Rapor Oluştur",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (chartGenerated) FontWeight.Bold else FontWeight.Normal,
                            shadow = LogoTextShadow
                        )
                    )
                }
            }
        }

        if (chartGenerated) {
            if (includeBpChart) {
                VitalLineChart(
                    title = "Tansiyon Grafiği",
                    series = listOf(
                        "Büyük Tansiyon" to chartBp.map {
                            it.measuredAt to it.systolic.toFloat()
                        },
                        "Küçük Tansiyon" to chartBp.map {
                            it.measuredAt to it.diastolic.toFloat()
                        }
                    )
                )
            }
            if (includeGlucoseChart) {
                VitalLineChart(
                    title = "Kan Şekeri Grafiği",
                    series = listOf(
                        "Kan Şekeri" to chartGlucose.map {
                            it.measuredAt to it.valueMgDl.toFloat()
                        }
                    )
                )
            }
        }

        Button(
            onClick = {
                scope.launch {
                    status = "Excel hazırlanıyor…"
                    val file = withContext(Dispatchers.IO) { exportExcel(context, db, profileId, from, to) }
                    status = "Hazır: ${file.name}"
                    excelGenerated = true
                    shareFile(context, file)
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp).sifahaneSoftBoundary(5.dp).vantablackGlassOverlay(),
            shape = reportControlShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = LogoColor.copy(alpha = 0.50f),
                contentColor = LogoColorDark
            )
        ) {
            Icon(Icons.Default.TableChart, null, tint = LogoColorDark)
            Spacer(Modifier.width(8.dp))
            Text(
                "Excel Raporu Oluştur Ve Paylaş",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (excelGenerated) FontWeight.Bold else FontWeight.Normal,
                    shadow = LogoTextShadow
                )
            )
        }

        val rangeDeleteEnabled = from != null && to != null
        OutlinedButton(
            enabled = rangeDeleteEnabled,
            onClick = { confirmDelete = true },
            modifier = Modifier.fillMaxWidth().height(52.dp).sifahaneSoftBoundary(5.dp).vantablackGlassOverlay(),
            shape = reportControlShape,
            border = BorderStroke(1.dp, Color.Transparent),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = LogoColor.copy(alpha = 0.50f),
                disabledContainerColor = LogoColor.copy(alpha = 0.50f),
                contentColor = Color(0xFFE30A17),
                disabledContentColor = Color(0xFFE30A17).copy(alpha = 0.25f)
            )
        ) {
            Icon(
                Icons.Default.DeleteSweep,
                contentDescription = null,
                tint = if (rangeDeleteEnabled) Color(0xFFE30A17) else Color(0xFFE30A17).copy(alpha = 0.25f)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Seçilen Tarih Aralığındaki Verileri Sil",
                color = if (rangeDeleteEnabled) Color(0xFFE30A17) else Color(0xFFE30A17).copy(alpha = 0.25f),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (deleteCompleted) FontWeight.Bold else FontWeight.Normal,
                    shadow = LogoTextShadow
                )
            )
        }

        Text(status, color = LogoColorDark, style = MaterialTheme.typography.bodyMedium.copy(shadow = LogoTextShadow))
    }

    if (confirmDelete && from != null && to != null) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Tarih Aralığındaki Verileri Sil") },
            text = {
                Text(
                    "${formatDate(from!!)} – ${formatDate(to!!)} arasındaki ilaç alma geçmişi, tansiyon ve kan şekeri kayıtları kalıcı olarak silinecek. İlaç tanımları ve kullanıcı bilgileri korunacaktır."
                )
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch(Dispatchers.IO) {
                        val endExclusive = to!! + 86_400_000L
                        db.doseLogDao().deleteDoseLogRange(profileId, from!!, endExclusive)
                        db.vitalsDao().deleteBpRange(profileId, from!!, endExclusive)
                        db.vitalsDao().deleteGlucoseRange(profileId, from!!, endExclusive)
                    }
                    confirmDelete = false
                    deleteCompleted = true
                    status = "Seçilen tarih aralığındaki geçmiş ve ölçüm kayıtları silindi."
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoColor.copy(alpha = 0.25f),
                        contentColor = Color(0xFF123A37)
                    )
                ) { Text("Evet, kalıcı olarak sil") }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text("İptal") }
            }
        )
    }
}

internal suspend fun exportExcel(
    context: Context,
    db: AppDatabase,
    profileId: Long,
    from: Long?,
    to: Long?
): File {
    val medications = db.medicationDao().allForProfile(profileId)
    val medicationRows = mutableListOf<List<Any?>>()
    medicationRows += listOf(
        "İlaç", "Fonksiyon", "Doz", "Saatler", "Stok", "Kritik Sınır",
        "Doktor", "Branş", "Kurum", "Raporlu", "Rapor Bitiş"
    )
    medications.forEach { medication ->
        medicationRows += listOf(
            medication.name, medication.purpose, medication.dose, medication.timesCsv,
            medication.stock, medication.lowStockLimit, medication.doctorName,
            medication.doctorBranch, medication.doctorInstitution,
            if (medication.isReported) "Evet" else "Hayır", medication.reportEndDate ?: ""
        )
    }

    val endExclusive = to?.plus(86_400_000L)
    val logs = db.doseLogDao().allLogsForProfile(profileId).filter {
        (from == null || it.timestamp >= from) && (endExclusive == null || it.timestamp < endExclusive)
    }
    val logRows = mutableListOf<List<Any?>>()
    logRows += listOf("İlaç", "Planlanan", "Gerçek", "Durum")
    logs.forEach { log ->
        logRows += listOf(
            log.medicationName,
            formatDateTime(log.scheduledDateTime),
            log.actualDateTime?.let(::formatDateTime) ?: "",
            log.action
        )
    }

    val bloodPressure = db.vitalsDao().allBp(profileId).filter {
        (from == null || it.measuredAt >= from) && (endExclusive == null || it.measuredAt < endExclusive)
    }
    val bpRows = mutableListOf<List<Any?>>()
    bpRows += listOf("Tarih", "Büyük (mmHg)", "Küçük (mmHg)", "Nabız (atım/dk)", "Not")
    bloodPressure.forEach { value ->
        bpRows += listOf(
            formatDateTime(value.measuredAt), value.systolic, value.diastolic,
            value.pulse, value.note
        )
    }

    val glucose = db.vitalsDao().allGlucose(profileId).filter {
        (from == null || it.measuredAt >= from) && (endExclusive == null || it.measuredAt < endExclusive)
    }
    val glucoseRows = mutableListOf<List<Any?>>()
    glucoseRows += listOf("Tarih", "Tür", "Kan Şekeri (mg/dL)", "Not")
    glucose.forEach { value ->
        glucoseRows += listOf(
            formatDateTime(value.measuredAt), value.measurementType,
            value.valueMgDl, value.note
        )
    }

    val directory = File(context.cacheDir, "reports").apply { mkdirs() }
    val file = File(directory, "Sifahane_Rapor_${System.currentTimeMillis()}.xlsx")
    SimpleXlsxWriter.write(
        file,
        listOf(
            SimpleXlsxWriter.Sheet("İlaçlar", medicationRows),
            SimpleXlsxWriter.Sheet("Doz Geçmişi", logRows),
            SimpleXlsxWriter.Sheet("Tansiyon", bpRows),
            SimpleXlsxWriter.Sheet("Kan Şekeri", glucoseRows)
        )
    )
    return file
}

internal fun shareFile(context: Context, file: File) {
    confirmSensitiveShare(
        context = context,
        contentDescription = "Excel raporu",
        encrypted = false
    ) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        context.startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                },
                "Excel raporunu paylaş"
            )
        )
    }
}

