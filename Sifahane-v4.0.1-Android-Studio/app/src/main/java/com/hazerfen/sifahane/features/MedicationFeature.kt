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
internal fun MedicineScreen(
    active: List<Medication>,
    archive: List<Medication>,
    reportGroups: List<ReportGroup>,
    modifier: Modifier,
    onEditMedication: (Medication) -> Unit,
    onNewReportGroup: () -> Unit,
    onEditReportGroup: (ReportGroup) -> Unit,
    selectedTab: Int,
    onTabChange: (Int) -> Unit,
    onAdd: () -> Unit,
    onDeleteReportGroup: (ReportGroup) -> Unit
) {
    val tab = selectedTab
    var deleteGroup by remember { mutableStateOf<ReportGroup?>(null) }

    Column(modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                TabRow(
                    selectedTabIndex = tab,
                    containerColor = Color.Transparent,
                    contentColor = LogoColor,
                    indicator = { tabPositions ->
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[tab]),
                            color = LogoColor
                        )
                    }
                ) {
                    listOf("Aktif İlaçlar", "İlaç Arşivi", "Rapor Grupları")
                        .forEachIndexed { index, label ->
                            Tab(
                                selected = tab == index,
                                onClick = { onTabChange(index) },
                                text = {
                                    Text(
                                        titleCaseTr(label),
                                        color = LogoColorDark,
                                        maxLines = 1,
                                        fontSize = 11.sp,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            shadow = LogoTextShadow
                                        )
                                    )
                                }
                            )
                        }
                }
            }
            IconButton(
                onClick = onAdd,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(48.dp)
                    .background(Vantablack05, CircleShape)
            ) {
                OutlinedLogoIcon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription =
                        if (tab == 2) "Rapor Grubu Ekle" else "İlaç Ekle",
                    size = 28.dp
                )
            }
        }

        if (tab < 2) {
            val list = if (tab == 0) active else archive
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                items(list, key = { it.id }) { med ->
                    SifahaneCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onEditMedication(med) }
                    ) {
                        Column {
                            if (!med.photoUri.isNullOrBlank()) {
                                Image(
                                    rememberAsyncImagePainter(med.photoUri),
                                    null,
                                    Modifier.fillMaxWidth().height(240.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = LogoColor.copy(alpha = 0.85f)
                            )
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    titleCaseTr(med.name),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                if (med.purpose.isNotBlank()) Text(med.purpose)
                                Text("${med.dose} • ${med.timesCsv}")
                                Text("Stok: ${med.stock} • Kritik Sınır: ${med.lowStockLimit}")
                                if (med.doctorName.isNotBlank()) {
                                    Text("Doktor: ${med.doctorName} – ${med.doctorBranch}")
                                }
                                if (med.isReported) {
                                    val groupName = reportGroups
                                        .firstOrNull { it.id == med.reportGroupId }?.name
                                    Text(
                                        "Rapor: ${med.reportStartDate ?: "-"} – ${med.reportEndDate ?: "-"}" +
                                            if (groupName != null) " • ${titleCaseTr(groupName)}" else ""
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 3.dp,
                        color = LogoColor
                    )
                }
            }
        } else {
            Column(Modifier.fillMaxSize()) {
                if (reportGroups.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Henüz Rapor Grubu Oluşturulmadı.",
                            color = LogoColorDark,
                            style = MaterialTheme.typography.titleMedium.copy(
                                shadow = LogoTextShadow
                            )
                        )
                    }
                } else {
                    LazyColumn(
                        Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(reportGroups, key = { it.id }) { group ->
                            val linked = (active + archive).count {
                                it.reportGroupId == group.id
                            }
                            SifahaneCard(modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(
                                        titleCaseTr(group.name),
                                        color = LogoColorDark,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            shadow = LogoTextShadow
                                        )
                                    )
                                    Text(
                                        "Rapor Başlangıç: ${group.startDate}",
                                        color = LogoColorDark
                                    )
                                    Text(
                                        "Rapor Bitiş: ${group.endDate}",
                                        color = LogoColorDark
                                    )
                                    Text(
                                        "Uyarı: ${group.warningDays} Gün Önce • Bağlı İlaç: $linked",
                                        color = LogoColorDark
                                    )
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(
                                            onClick = { onEditReportGroup(group) },
                                            colors = ButtonDefaults.textButtonColors(
                                                containerColor = Vantablack10,
                                                contentColor = LogoColorDark
                                            )
                                        ) {
                                            Icon(Icons.Default.Edit, null)
                                            Spacer(Modifier.width(4.dp))
                                            Text("Düzenle")
                                        }
                                        TextButton(
                                            onClick = { deleteGroup = group },
                                            colors = ButtonDefaults.textButtonColors(
                                                containerColor = Vantablack10,
                                                contentColor = MaterialTheme.colorScheme.error
                                            )
                                        ) {
                                            Icon(Icons.Default.Delete, null)
                                            Spacer(Modifier.width(4.dp))
                                            Text("Sil")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    deleteGroup?.let { group ->
        AlertDialog(
            onDismissRequest = { deleteGroup = null },
            title = { Text("Rapor Grubunu Sil") },
            text = {
                Text(
                    "${titleCaseTr(group.name)} silinecek. Bu gruba bağlı ilaçlar silinmeyecek; yalnızca grup bağlantıları kaldırılacak."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteReportGroup(group)
                        deleteGroup = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoColor.copy(alpha = 0.25f),
                        contentColor = Color(0xFF123A37)
                    )
                ) {
                    Text("Evet, Sil")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteGroup = null }) {
                    Text("İptal")
                }
            }
        )
    }
}

@Composable
internal fun ReportGroupEditorDialog(
    existing: ReportGroup?,
    profileId: Long,
    onDismiss: () -> Unit,
    onSave: (ReportGroup) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val today = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }
    var name by remember(existing?.id) {
        mutableStateOf(existing?.name ?: "")
    }
    var startDate by remember(existing?.id) {
        mutableStateOf(existing?.startDate ?: today)
    }
    var endDate by remember(existing?.id) {
        mutableStateOf(existing?.endDate ?: today)
    }
    var warningDays by remember(existing?.id) {
        mutableStateOf((existing?.warningDays ?: 7).toString())
    }
    var error by remember(existing?.id) { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (existing == null)
                    "Yeni Rapor Grubu"
                else
                    "Rapor Grubunu Düzenle",
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
                CenteredLabeledField("Rapor Grubu Adı") {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                        colors = medicationFieldColors()
                    )
                }
                CenteredLabeledField("Rapor Başlangıç Tarihi") {
                    ThemedDateButton(
                        text = startDate,
                        onClick = {
                            pickDateString(context) { startDate = it }
                        },
                        medicationStyle = true
                    )
                }
                CenteredLabeledField("Rapor Bitiş Tarihi") {
                    ThemedDateButton(
                        text = endDate,
                        onClick = {
                            pickDateString(context) { endDate = it }
                        },
                        medicationStyle = true
                    )
                }
                CenteredLabeledField("Kaç Gün Önce Uyarı") {
                    OutlinedTextField(
                        value = warningDays,
                        onValueChange = {
                            warningDays = it.filter(Char::isDigit)
                            error = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                        colors = medicationFieldColors()
                    )
                }
                error?.let { Text(it, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center) }
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val validation = HealthDataValidator.reportGroupError(
                            name = name,
                            startDate = startDate,
                            endDate = endDate,
                            warningDays = warningDays.toIntOrNull()
                        )
                        if (validation != null) {
                            error = validation
                        } else {
                            onSave(
                                ReportGroup(
                                    id = existing?.id ?: 0,
                                    profileId = profileId,
                                    name = name.trim(),
                                    startDate = startDate,
                                    endDate = endDate,
                                    warningDays = warningDays.toInt()
                                )
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoColor.copy(alpha = 0.25f),
                        contentColor = Color(0xFF123A37)
                    )
                ) {
                    Text("Kaydet")
                }
            }
        },
        dismissButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = onDismiss) {
                    Text("İptal")
                }
            }
        }
    )
}

