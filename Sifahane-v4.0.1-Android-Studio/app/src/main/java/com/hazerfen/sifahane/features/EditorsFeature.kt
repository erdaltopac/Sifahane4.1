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
internal fun ProfileEditorDialog(existing: UserProfile?, onDismiss: () -> Unit, onSave: (UserProfile) -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var name by rememberSaveable(existing?.id) { mutableStateOf(existing?.name ?: "") }
    var surname by rememberSaveable(existing?.id) { mutableStateOf(existing?.surname ?: "") }
    var relation by rememberSaveable(existing?.id) { mutableStateOf(existing?.relation ?: "") }
    var birthDate by rememberSaveable(existing?.id) { mutableStateOf(existing?.birthDate) }
    var bloodGroup by rememberSaveable(existing?.id) { mutableStateOf(existing?.bloodGroup ?: "Bilinmiyor") }
    var note by rememberSaveable(existing?.id) { mutableStateOf(existing?.profileNote ?: "") }
    var photo by rememberSaveable(existing?.id) { mutableStateOf(existing?.photoUri) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    var cropCandidate by remember { mutableStateOf<String?>(null) }
    var confirmDiscard by remember { mutableStateOf(false) }
    val originalSnapshot = remember(existing?.id) {
        listOf(
            existing?.name.orEmpty(), existing?.surname.orEmpty(), existing?.relation.orEmpty(),
            existing?.birthDate, existing?.bloodGroup ?: "Bilinmiyor", existing?.profileNote.orEmpty(),
            existing?.photoUri
        )
    }
    fun requestDismiss() {
        val current = listOf(name, surname, relation, birthDate, bloodGroup, note, photo)
        if (current != originalSnapshot) confirmDiscard = true else onDismiss()
    }
    androidx.activity.compose.BackHandler { requestDismiss() }

    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            persistImageToAppStorage(context, uri, "profile_photos", "profile")?.let {
                cropCandidate = it
            }
        }
    }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        if (ok) pendingCameraUri?.toString()?.let { cropCandidate = it }
    }

    AlertDialog(
        onDismissRequest = { requestDismiss() },
        title = {
            Text(
                "Kullanıcı Profili",
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
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 4.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    if (!photo.isNullOrBlank()) {
                        Image(
                            rememberAsyncImagePainter(photo),
                            null,
                            Modifier.size(96.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                gallery.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                            shape = StandardFieldShape,
                            colors = profileOutlinedButtonColors()
                        ) { Text("Galeriden", color = LogoColorDark, fontSize = 13.sp, style = MaterialTheme.typography.labelMedium.copy(shadow = LogoTextShadow)) }
                        OutlinedButton(
                            onClick = {
                            val dir = File(context.filesDir, "profile_photos").apply { mkdirs() }
                            val file = File(dir, "profile_${System.currentTimeMillis()}.jpg")
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                            pendingCameraUri = uri
                            camera.launch(uri)
                            },
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                            shape = StandardFieldShape,
                            colors = profileOutlinedButtonColors()
                        ) {
                            Text(
                                "Kamera",
                                modifier = Modifier.fillMaxWidth(),
                                color = LogoColorDark,
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    shadow = LogoTextShadow
                                )
                            )
                        }
                    }
                }
                item {
                    CenteredLabeledField("Ad") {
                        CompactProfileTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                        )
                    }
                }
                item {
                    CenteredLabeledField("Soyad") {
                        CompactProfileTextField(
                            value = surname,
                            onValueChange = { surname = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                        )
                    }
                }
                item {
                    CenteredLabeledField("Doğum Tarihi") {
                        ThemedDateButton(
                            text = birthDate ?: "Seçilmedi",
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                            onClick = {
                                pickDateString(context) { birthDate = it }
                            }
                        )
                    }
                }
                item {
                    var expanded by remember { mutableStateOf(false) }
                    CenteredLabeledField("Kan Grubu") {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            OutlinedButton(
                                onClick = { expanded = true },
                                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                                border = BorderStroke(1.5.dp, LogoColor),
                                shape = StandardFieldShape,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Vantablack05,
                                    contentColor = LogoColorDark
                                )
                            ) {
                                Text(
                                    bloodGroup,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = LogoColorDark,
                                    fontSize = 13.sp,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        shadow = LogoTextShadow
                                    )
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.fillMaxWidth(0.86f),
                                shape = StandardFieldShape,
                                containerColor = Color(0xFFF2F2F2)
                            ) {
                                listOf(
                                    "A Rh+", "A Rh-", "B Rh+", "B Rh-",
                                    "AB Rh+", "AB Rh-", "0 Rh+", "0 Rh-",
                                    "Bilinmiyor"
                                ).forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                option,
                                                modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.Center,
                                                color = LogoColorDark
                                            )
                                        },
                                        onClick = {
                                            bloodGroup = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    CenteredLabeledField("Yakınlık") {
                        CompactProfileTextField(
                            value = relation,
                            onValueChange = { relation = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                        )
                    }
                }
                item {
                    CenteredLabeledField("Not") {
                        CompactProfileTextField(
                            value = note,
                            onValueChange = { note = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp, max = 116.dp),
                            singleLine = false,
                            minLines = 1,
                            maxLines = 5
                        )
                    }
                }
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    enabled = name.isNotBlank(),
                    onClick = {
                        onSave(
                            UserProfile(
                                existing?.id ?: 0,
                                name,
                                relation,
                                surname,
                                photo,
                                birthDate,
                                bloodGroup,
                                note
                            )
                        )
                    }
                ) {
                    Text(
                        "Kaydet",
                        color = LogoColorDark,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            shadow = LogoTextShadow
                        )
                    )
                }
            }
        },
        dismissButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = { requestDismiss() }, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text(
                        "İptal",
                        color = LogoColorDark,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            shadow = LogoTextShadow
                        )
                    )
                }
            }
        }
    )

    if (confirmDiscard) {
        AlertDialog(
            onDismissRequest = { confirmDiscard = false },
            title = { Text("Değişiklikler kaydedilmedi") },
            text = { Text("Profil formuna girdiğiniz bilgiler kaybolacak. Formdan çıkılsın mı?") },
            confirmButton = {
                Button(onClick = onDismiss, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text("Değişiklikleri sil")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDiscard = false }, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text("Forma dön")
                }
            }
        )
    }

    cropCandidate?.let { candidate ->
        CropImageDialog(
            sourceUri = candidate,
            circularPreview = true,
            targetAspectRatio = 1f,
            outputSubfolder = "profile_photos",
            outputPrefix = "profile",
            onDismiss = { cropCandidate = null },
            onConfirm = {
                photo = it
                cropCandidate = null
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SuggestionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    modifier: Modifier = Modifier.fillMaxWidth(),
    medicationStyle: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    val filtered = remember(value, options) {
        options.asSequence()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .filter { value.isBlank() || it.contains(value, ignoreCase = true) }
            .take(12)
            .toList()
    }

    ExposedDropdownMenuBox(
        expanded = expanded && filtered.isNotEmpty(),
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = true
            },
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = modifier.menuAnchor(),
            colors = if (medicationStyle) medicationFieldColors() else logoFieldColors(),
            textStyle = LocalTextStyle.current.copy(
                color = if (medicationStyle) LogoColorDark else LocalContentColor.current
            )
        )

        ExposedDropdownMenu(
            expanded = expanded && filtered.isNotEmpty(),
            onDismissRequest = { expanded = false }
        ) {
            filtered.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
internal fun MedicationEditorDialog(
    existing: Medication?,
    profileId: Long,
    suggestions: List<Medication>,
    reportGroups: List<ReportGroup>,
    onCreateReportGroup: (ReportGroup) -> Unit,
    onUpdateReportGroup: (ReportGroup) -> Unit,
    onDismiss: () -> Unit,
    onSave: (Medication) -> Unit,
    onDelete: (Medication) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }

    var name by rememberSaveable(existing?.id) { mutableStateOf(existing?.name ?: "") }
    var purpose by rememberSaveable(existing?.id) { mutableStateOf(existing?.purpose ?: "") }
    var dose by rememberSaveable(existing?.id) { mutableStateOf(existing?.dose ?: "1 tablet") }
    var times by remember { mutableStateOf(existing?.timesCsv?.split(",")?.toMutableList() ?: mutableListOf("08:00")) }
    var stock by rememberSaveable(existing?.id) { mutableStateOf((existing?.stock ?: 28).toString()) }
    var lowStock by rememberSaveable(existing?.id) { mutableStateOf((existing?.lowStockLimit ?: 5).toString()) }
    var notes by rememberSaveable(existing?.id) { mutableStateOf(existing?.notes ?: "") }
    var startDate by rememberSaveable(existing?.id) { mutableStateOf(existing?.startDate ?: today) }
    var endDate by rememberSaveable(existing?.id) { mutableStateOf(existing?.endDate ?: today) }
    var continuous by rememberSaveable(existing?.id) { mutableStateOf(existing?.continuous ?: false) }
    var archived by rememberSaveable(existing?.id) { mutableStateOf(existing?.archived ?: false) }
    var active by rememberSaveable(existing?.id) { mutableStateOf(existing?.active ?: true) }
    var photo by rememberSaveable(existing?.id) { mutableStateOf(existing?.photoUri) }
    var barcode by rememberSaveable(existing?.id) { mutableStateOf(existing?.barcode ?: "") }
    var prospectus by rememberSaveable(existing?.id) { mutableStateOf(existing?.prospectusUrl ?: "") }
    var prospectusVerified by rememberSaveable(existing?.id) {
        mutableStateOf(!existing?.prospectusUrl.isNullOrBlank())
    }
    var doctorName by rememberSaveable(existing?.id) { mutableStateOf(existing?.doctorName ?: "") }
    var doctorBranch by rememberSaveable(existing?.id) { mutableStateOf(existing?.doctorBranch ?: "") }
    var doctorInstitution by rememberSaveable(existing?.id) { mutableStateOf(existing?.doctorInstitution ?: "") }
    var doctorPhone by rememberSaveable(existing?.id) { mutableStateOf(existing?.doctorPhone ?: "") }
    var isReported by rememberSaveable(existing?.id) { mutableStateOf(existing?.isReported ?: false) }
    var reportStart by rememberSaveable(existing?.id) { mutableStateOf(existing?.reportStartDate ?: today) }
    var reportEnd by rememberSaveable(existing?.id) { mutableStateOf(existing?.reportEndDate ?: today) }
    var reportWarning by rememberSaveable(existing?.id) { mutableStateOf((existing?.reportWarningDays ?: 7).toString()) }
    var selectedReportGroupId by rememberSaveable(existing?.id) { mutableStateOf(existing?.reportGroupId) }
    var reportModeGroup by rememberSaveable(existing?.id) { mutableStateOf(existing?.reportGroupId != null) }
    var showNewGroupDialog by remember { mutableStateOf(false) }
    var validationError by rememberSaveable(existing?.id) { mutableStateOf<String?>(null) }
    var confirmDiscard by remember { mutableStateOf(false) }

    fun formSnapshot(): List<Any?> = listOf(
        name, purpose, dose, times.joinToString(","), stock, lowStock, notes,
        startDate, endDate, continuous, archived, active, photo, barcode, prospectus,
        doctorName, doctorBranch, doctorInstitution, doctorPhone, isReported,
        reportStart, reportEnd, reportWarning, selectedReportGroupId, reportModeGroup
    )
    val originalSnapshot = remember(existing?.id) {
        listOf(
            existing?.name.orEmpty(), existing?.purpose.orEmpty(), existing?.dose ?: "1 tablet",
            existing?.timesCsv ?: "08:00", (existing?.stock ?: 28).toString(),
            (existing?.lowStockLimit ?: 5).toString(), existing?.notes.orEmpty(),
            existing?.startDate ?: today, existing?.endDate ?: today,
            existing?.continuous ?: false, existing?.archived ?: false, existing?.active ?: true,
            existing?.photoUri, existing?.barcode.orEmpty(), existing?.prospectusUrl.orEmpty(),
            existing?.doctorName.orEmpty(), existing?.doctorBranch.orEmpty(),
            existing?.doctorInstitution.orEmpty(), existing?.doctorPhone.orEmpty(),
            existing?.isReported ?: false, existing?.reportStartDate ?: today,
            existing?.reportEndDate ?: today, (existing?.reportWarningDays ?: 7).toString(),
            existing?.reportGroupId, existing?.reportGroupId != null
        )
    }
    fun requestDismiss() {
        if (formSnapshot() != originalSnapshot) confirmDiscard = true else onDismiss()
    }
    androidx.activity.compose.BackHandler { requestDismiss() }

    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    var confirmDelete by remember { mutableStateOf(false) }
    val nameOptions = remember(suggestions) { suggestions.map { it.name } }
    val purposeOptions = remember(suggestions) { suggestions.map { it.purpose } }
    val doseOptions = remember(suggestions) { suggestions.map { it.dose } }
    val notesOptions = remember(suggestions) { suggestions.map { it.notes } }
    val doctorNameOptions = remember(suggestions) { suggestions.map { it.doctorName } }
    val doctorBranchOptions = remember(suggestions) { suggestions.map { it.doctorBranch } }
    val doctorInstitutionOptions = remember(suggestions) { suggestions.map { it.doctorInstitution } }
    val doctorPhoneOptions = remember(suggestions) { suggestions.map { it.doctorPhone } }


    var scannerMessage by rememberSaveable(existing?.id) { mutableStateOf("") }

    val localScanner = rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val raw = result.data?.getStringExtra(ScannerActivity.EXTRA_CODE).orEmpty()
            if (raw.isNotBlank()) {
                barcode = raw
                scannerMessage = "Kod okundu: $raw. İlaç adı, doz ve prospektüs bilgilerini kaydetmeden önce doğrulayın."
            } else {
                scannerMessage = "Kod okunamadı."
            }
        } else {
            scannerMessage = "Tarama iptal edildi."
        }
    }

    fun startBarcodeScan() {
        scannerMessage = "Kamera açılıyor…"
        localScanner.launch(Intent(context, ScannerActivity::class.java))
    }


    var cropCandidate by remember { mutableStateOf<String?>(null) }

    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            persistImageToAppStorage(context, uri, "medicine_photos", "medicine")?.let {
                cropCandidate = it
            }
        }
    }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        if (ok) pendingCameraUri?.toString()?.let { cropCandidate = it }
    }

    fun pickTime(index: Int) {
        val p = times[index].split(":")
        TimePickerDialog(context, { _, h, m ->
            val n = times.toMutableList()
            n[index] = "%02d:%02d".format(h, m)
            times = n
        }, p[0].toInt(), p[1].toInt(), true).show()
    }

    AlertDialog(
        onDismissRequest = { requestDismiss() },
        title = {
            Text(
                if (existing == null) "Yeni İlaç" else "İlacı Düzenle",
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
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { if (!photo.isNullOrBlank()) Image(rememberAsyncImagePainter(photo), null, Modifier.fillMaxWidth().height(190.dp), contentScale = ContentScale.Crop) }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { gallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                            shape = StandardFieldShape,
                            colors = medicationOutlinedButtonColors()
                        ) {
                            Text(
                                "Galeriden",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }
                        OutlinedButton(
                            onClick = {
                            val dir = File(context.filesDir, "medicine_photos").apply { mkdirs() }
                            val file = File(dir, "medicine_${System.currentTimeMillis()}.jpg")
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                            pendingCameraUri = uri
                            camera.launch(uri)
                            },
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                            shape = StandardFieldShape,
                            colors = medicationOutlinedButtonColors()
                        ) {
                            Text(
                                "Kamera",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                item { SuggestionTextField(name, { name = it }, "İlaç adı", nameOptions, medicationStyle = true) }
                item { SuggestionTextField(purpose, { purpose = it }, "Fonksiyonu", purposeOptions, medicationStyle = true) }
                item { SuggestionTextField(dose, { dose = it }, "Doz", doseOptions, medicationStyle = true) }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton({ if (times.size > 1) times = times.dropLast(1).toMutableList() }) { Icon(Icons.Default.RemoveCircle, null) }
                        Text(
                            "Günlük Doz: ${times.size}",
                            textAlign = TextAlign.Center,
                            color = LogoColorDark,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                shadow = LogoTextShadow
                            )
                        )
                        IconButton({ if (times.size < 8) times = (times + "08:00").toMutableList() }) { Icon(Icons.Default.AddCircle, null) }
                    }
                }
                items(times.size) { i -> OutlinedButton(
                            onClick = { pickTime(i) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = medicationOutlinedButtonColors()
                        ) { Text("${i + 1}. doz: ${times[i]}") } }
                item { OutlinedButton({ pickDateString(context) { startDate = it } }, Modifier.fillMaxWidth()) { Text("Başlangıç: $startDate") } }
                item {
                    CenteredCheckboxField(
                        label = "Sürekli Kullanım",
                        checked = continuous,
                        onCheckedChange = { continuous = it }
                    )
                }
                if (!continuous) item { OutlinedButton({ pickDateString(context) { endDate = it } }, Modifier.fillMaxWidth()) { Text("Bitiş: $endDate") } }
                item { Spacer(Modifier.height(10.dp)) }
                item {
                    CenteredCheckboxField(
                        label = "Arşiv İlacı",
                        checked = archived,
                        onCheckedChange = { archived = it }
                    )
                }
                item { OutlinedTextField(stock, { stock = it.filter(Char::isDigit) }, label = { Text("Kalan stok") },
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                        shape = StandardFieldShape,
                        colors = standardFieldColors()
                    ) }
                item { OutlinedTextField(lowStock, { lowStock = it.filter(Char::isDigit) }, label = { Text("Kritik stok sınırı") },
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                        shape = StandardFieldShape,
                        colors = standardFieldColors()
                    ) }
                item { SuggestionTextField(notes, { notes = it }, "Kullanım talimatı ve not", notesOptions, medicationStyle = true) }
                item {
                    SuggestionTextField(
                        doctorName,
                        { selected ->
                            doctorName = selected
                            suggestions.firstOrNull { it.doctorName == selected }?.let { source ->
                                if (doctorBranch.isBlank()) doctorBranch = source.doctorBranch
                                if (doctorInstitution.isBlank()) doctorInstitution = source.doctorInstitution
                                if (doctorPhone.isBlank()) doctorPhone = source.doctorPhone
                            }
                        },
                        "Doktor adı soyadı",
                        doctorNameOptions,
                        medicationStyle = true
                    )
                }
                item { SuggestionTextField(doctorBranch, { doctorBranch = it }, "Doktor branşı", doctorBranchOptions, medicationStyle = true) }
                item { SuggestionTextField(doctorInstitution, { doctorInstitution = it }, "Hastane / kurum", doctorInstitutionOptions, medicationStyle = true) }
                item { SuggestionTextField(doctorPhone, { doctorPhone = it }, "Doktor telefonu", doctorPhoneOptions, medicationStyle = true) }
                item {
                    CenteredCheckboxField(
                        label = "Raporlu İlaç",
                        checked = isReported,
                        onCheckedChange = {
                            isReported = it
                            if (!it) {
                                selectedReportGroupId = null
                                reportModeGroup = false
                            }
                        }
                    )
                }
                if (isReported) {
                    item {
                        var groupExpanded by remember { mutableStateOf(false) }
                        Box {
                            OutlinedButton(
                                onClick = { groupExpanded = true },
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.5.dp, LogoColor),
                                colors = medicationOutlinedButtonColors()
                            ) {
                                val selectedName = reportGroups
                                    .firstOrNull { it.id == selectedReportGroupId }
                                    ?.name
                                Text(
                                    selectedName?.let {
                                        "Rapor Grubu: ${titleCaseTr(it)}"
                                    } ?: "Rapor Grubu Seçilmedi",
                                    maxLines = 1
                                )
                            }
                            DropdownMenu(
                                expanded = groupExpanded,
                                onDismissRequest = { groupExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Rapor Grubu Seçilmedi") },
                                    onClick = {
                                        selectedReportGroupId = null
                                        reportModeGroup = false
                                        groupExpanded = false
                                    }
                                )
                                reportGroups.forEach { group ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "${titleCaseTr(group.name)} • ${group.startDate} – ${group.endDate}"
                                            )
                                        },
                                        onClick = {
                                            selectedReportGroupId = group.id
                                            reportModeGroup = true
                                            reportStart = group.startDate
                                            reportEnd = group.endDate
                                            reportWarning = group.warningDays.toString()
                                            groupExpanded = false
                                        }
                                    )
                                }
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Yeni Rapor Grubu Oluştur") },
                                    onClick = {
                                        groupExpanded = false
                                        showNewGroupDialog = true
                                    }
                                )
                            }
                        }
                    }
                    item {
                        ThemedDateButton(
                            text = "Rapor Başlangıç: $reportStart",
                            onClick = {
                                if (!reportModeGroup) {
                                    pickDateString(context) { reportStart = it }
                                }
                            },
                            medicationStyle = true
                        )
                    }
                    item {
                        ThemedDateButton(
                            text = "Rapor Bitiş: $reportEnd",
                            onClick = {
                                if (!reportModeGroup) {
                                    pickDateString(context) { reportEnd = it }
                                }
                            },
                            medicationStyle = true
                        )
                    }
                    if (reportModeGroup) {
                        item {
                            Text(
                                "Tarihler seçilen rapor grubundan otomatik alınmıştır. Elle değiştirmek için “Rapor Grubu Seçilmedi” seçeneğini kullanın.",
                                color = LogoColorDark,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    shadow = LogoTextShadow
                                )
                            )
                        }
                    }
                    item {
                        OutlinedTextField(
                            reportWarning,
                            { reportWarning = it.filter(Char::isDigit) },
                            label = { Text("Kaç Gün Önce Uyarı") },
                            enabled = !reportModeGroup,
                            colors = standardFieldColors()
                        )
                    }
                }
                item {
                    OutlinedButton(
                        onClick = { startBarcodeScan() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = medicationOutlinedButtonColors()
                    ) {
                        Icon(Icons.Default.QrCodeScanner, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Barkod / Karekod Oku")
                    }
                }
                if (scannerMessage.isNotBlank()) item { Text(scannerMessage) }
                if (barcode.isNotBlank()) item { Text("Okunan kod: $barcode") }
                item {
                    Button(
                        enabled = barcode.isNotBlank() || name.isNotBlank(),
                        onClick = {
                            val query = listOf(name, barcode, "resmi üretici prospektüs PDF")
                                .filter { it.isNotBlank() }
                                .joinToString(" ")
                            val searchUrl = "https://www.google.com/search?q=" + Uri.encode(query)
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl)))
                            scannerMessage = "Arama sonucu otomatik kaydedilmez. Yalnız resmi üretici veya sağlık otoritesi bağlantısını aşağıdaki alana yapıştırıp doğrulayın."
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(24.dp),
                                ambientColor = Color.Black,
                                spotColor = Color.Black
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LogoColor.copy(alpha = 0.25f),
                            contentColor = Color.Black,
                            disabledContainerColor = LogoColor.copy(alpha = 0.45f),
                            disabledContentColor = Color.Black.copy(alpha = 0.55f)
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            Icons.Default.Description,
                            null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            "Üretici Prospektüsünü Bul ve Aç",
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Clip,
                            fontSize = 12.sp
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = prospectus,
                        onValueChange = {
                            prospectus = it.trim()
                            prospectusVerified = false
                        },
                        label = { Text("Doğrulanmış resmi prospektüs bağlantısı") },
                        supportingText = { Text("Arama sayfası değil; resmi üretici veya sağlık otoritesi sayfası olmalıdır.") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = medicationFieldColors()
                    )
                }
                if (prospectus.isNotBlank()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { prospectusVerified = !prospectusVerified },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = prospectusVerified,
                                onCheckedChange = { prospectusVerified = it }
                            )
                            Text("Bağlantının resmi üretici veya sağlık otoritesi sayfası olduğunu doğruladım.")
                        }
                    }
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Vantablack05, StandardFieldShape)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Switch(active, { active = it })
                        Text(
                            "Aktif",
                            color = LogoColorDark,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                shadow = LogoTextShadow
                            )
                        )
                    }
                }
                if (existing != null) item {
                    OutlinedButton({ confirmDelete = true }, colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("İLACI SİL") }
                }
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    validationError?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Button(
                    onClick = {
                        val stockValue = stock.toIntOrNull()
                        val lowStockValue = lowStock.toIntOrNull()
                        val reportWarningValue = reportWarning.toIntOrNull()
                        val error = HealthDataValidator.medicationError(
                            name = name,
                            dose = dose,
                            times = times,
                            stock = stockValue,
                            lowStockLimit = lowStockValue,
                            startDate = startDate,
                            endDate = if (continuous) null else endDate,
                            continuous = continuous,
                            reportWarningDays = if (isReported) reportWarningValue else null
                        )
                        if (error != null) {
                            validationError = error
                            return@Button
                        }
                        if (prospectus.isNotBlank()) {
                            val parsedUrl = runCatching { Uri.parse(prospectus) }.getOrNull()
                            val validScheme = parsedUrl?.scheme?.lowercase(Locale.ROOT) in setOf("http", "https")
                            if (!validScheme || parsedUrl?.host.isNullOrBlank()) {
                                validationError = "Prospektüs bağlantısı geçerli bir http/https adresi olmalıdır."
                                return@Button
                            }
                            if (!prospectusVerified) {
                                validationError = "Prospektüs bağlantısını resmi kaynak olarak doğruladığınızı işaretleyin."
                                return@Button
                            }
                        }
                        onSave(
                            Medication(
                                id = existing?.id ?: 0,
                                profileId = profileId,
                                name = name,
                                purpose = purpose,
                                dose = dose,
                                timesCsv = times.joinToString(","),
                                stock = requireNotNull(stockValue),
                                lowStockLimit = requireNotNull(lowStockValue),
                                photoUri = photo,
                                notes = notes,
                                startDate = startDate,
                                endDate = if (continuous) null else endDate,
                                continuous = continuous,
                                active = active,
                                archived = archived,
                                barcode = barcode.ifBlank { null },
                                prospectusUrl = prospectus.ifBlank { null },
                                doctorName = doctorName,
                                doctorBranch = doctorBranch,
                                doctorInstitution = doctorInstitution,
                                doctorPhone = doctorPhone,
                                isReported = isReported,
                                reportStartDate = if (isReported) reportStart else null,
                                reportEndDate = if (isReported) reportEnd else null,
                                reportWarningDays = reportWarningValue ?: 7,
                                reportGroupId =
                                    if (isReported && reportModeGroup)
                                        selectedReportGroupId
                                    else
                                        null
                            )
                        )
                    }
                ) {
                    Text("Kaydet")
                }
                }
            }
        },
        dismissButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = { requestDismiss() }, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text("İptal")
                }
            }
        }
    )

    if (confirmDiscard) {
        AlertDialog(
            onDismissRequest = { confirmDiscard = false },
            title = { Text("Değişiklikler kaydedilmedi") },
            text = { Text("İlaç formuna girdiğiniz bilgiler kaybolacak. Formdan çıkılsın mı?") },
            confirmButton = {
                Button(onClick = onDismiss, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text("Değişiklikleri sil")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDiscard = false }, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text("Forma dön")
                }
            }
        )
    }

    if (confirmDelete && existing != null) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Emin misiniz?") },
            text = { Text("${existing.name} silinecek.") },
            confirmButton = { Button({ onDelete(existing); confirmDelete = false }) { Text("Evet, sil") } },
            dismissButton = { TextButton({ confirmDelete = false }) { Text("İptal") } }
        )
    }

    cropCandidate?.let { candidate ->
        CropImageDialog(
            sourceUri = candidate,
            circularPreview = false,
            targetAspectRatio = 4f / 3f,
            outputSubfolder = "medicine_photos",
            outputPrefix = "medicine",
            onDismiss = { cropCandidate = null },
            onConfirm = {
                photo = it
                cropCandidate = null
            }
        )
    }

    if (showNewGroupDialog) {
        var groupName by rememberSaveable { mutableStateOf("") }
        var groupStart by rememberSaveable { mutableStateOf(reportStart) }
        var groupEnd by rememberSaveable { mutableStateOf(reportEnd) }
        var groupWarning by rememberSaveable { mutableStateOf(reportWarning) }

        AlertDialog(
            onDismissRequest = { showNewGroupDialog = false },
            title = { Text("Yeni Rapor Grubu") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(groupName, { groupName = it }, label = { Text("Rapor Grubu Adı") },
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                        shape = StandardFieldShape,
                        colors = standardFieldColors()
                    )
                    ThemedDateButton(
                        text = "Rapor Başlangıç: $groupStart",
                        onClick = { pickDateString(context) { groupStart = it } },
                        medicationStyle = true
                    )
                    ThemedDateButton(
                        text = "Rapor Bitiş: $groupEnd",
                        onClick = { pickDateString(context) { groupEnd = it } },
                        medicationStyle = true
                    )
                    OutlinedTextField(groupWarning, { groupWarning = it.filter(Char::isDigit) }, label = { Text("Kaç gün önce uyarı") },
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                        shape = StandardFieldShape,
                        colors = standardFieldColors()
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = groupName.isNotBlank(),
                    onClick = {
                        onCreateReportGroup(
                            ReportGroup(
                                profileId = profileId,
                                name = groupName,
                                startDate = groupStart,
                                endDate = groupEnd,
                                warningDays = groupWarning.toIntOrNull() ?: 7
                            )
                        )
                        reportStart = groupStart
                        reportEnd = groupEnd
                        reportWarning = groupWarning
                        showNewGroupDialog = false
                    }
                ) { Text("Kaydet") }
            },
            dismissButton = { TextButton(onClick = { showNewGroupDialog = false }) { Text("İptal") } }
        )
    }
}

