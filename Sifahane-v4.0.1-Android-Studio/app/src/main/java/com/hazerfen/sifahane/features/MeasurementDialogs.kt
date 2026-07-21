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
import androidx.exifinterface.media.ExifInterface
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
internal fun BpDialog(onDismiss: () -> Unit, onSave: (Int, Int, Int?, String) -> Unit) {
    var systolic by rememberSaveable { mutableStateOf("") }
    var diastolic by rememberSaveable { mutableStateOf("") }
    var pulse by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmDiscard by remember { mutableStateOf(false) }
    fun requestDismiss() {
        if (systolic.isNotBlank() || diastolic.isNotBlank() || pulse.isNotBlank() || note.isNotBlank()) {
            confirmDiscard = true
        } else onDismiss()
    }
    androidx.activity.compose.BackHandler { requestDismiss() }

    AlertDialog(
        onDismissRequest = { requestDismiss() },
        title = { Text("Tansiyon Ölçümü Ekle") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    systolic,
                    { systolic = it.filter(Char::isDigit); error = null },
                    label = { Text("Büyük tansiyon (mmHg)") },
                    colors = logoFieldColors()
                )
                OutlinedTextField(
                    diastolic,
                    { diastolic = it.filter(Char::isDigit); error = null },
                    label = { Text("Küçük tansiyon (mmHg)") },
                    colors = logoFieldColors()
                )
                OutlinedTextField(
                    pulse,
                    { pulse = it.filter(Char::isDigit); error = null },
                    label = { Text("Nabız (atım/dk)") },
                    colors = logoFieldColors()
                )
                OutlinedTextField(note, { note = it }, label = { Text("Not") }, colors = logoFieldColors())
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val validation = HealthDataValidator.bloodPressureError(
                        systolic.toIntOrNull(),
                        diastolic.toIntOrNull(),
                        pulse.toIntOrNull()
                    )
                    if (validation != null) error = validation
                    else onSave(systolic.toInt(), diastolic.toInt(), pulse.toIntOrNull(), note.trim())
                },
                modifier = Modifier.heightIn(min = 48.dp)
            ) { Text("Kaydet") }
        },
        dismissButton = { TextButton(onClick = { requestDismiss() }, modifier = Modifier.heightIn(min = 48.dp)) { Text("İptal") } }
    )
    if (confirmDiscard) ConfirmDiscardDialog(
        onKeepEditing = { confirmDiscard = false },
        onDiscard = onDismiss
    )
}

@Composable
internal fun GlucoseDialog(onDismiss: () -> Unit, onSave: (Int, String, String) -> Unit) {
    var value by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf(GlucoseMeasurementType.FASTING) }
    var note by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmDiscard by remember { mutableStateOf(false) }
    fun requestDismiss() {
        if (value.isNotBlank() || type != GlucoseMeasurementType.FASTING || note.isNotBlank()) confirmDiscard = true
        else onDismiss()
    }
    androidx.activity.compose.BackHandler { requestDismiss() }

    AlertDialog(
        onDismissRequest = { requestDismiss() },
        title = { Text("Kan Şekeri Ölçümü Ekle") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value,
                    { value = it.filter(Char::isDigit); error = null },
                    label = { Text("Kan şekeri (mg/dL)") },
                    colors = logoFieldColors()
                )
                GlucoseMeasurementType.entries.forEach { option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(type == option, { type = option })
                        Text(option.displayName)
                    }
                }
                OutlinedTextField(note, { note = it }, label = { Text("Not") }, colors = logoFieldColors())
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val validation = HealthDataValidator.glucoseError(value.toIntOrNull())
                    if (validation != null) error = validation
                    else onSave(value.toInt(), type.displayName, note.trim())
                },
                modifier = Modifier.heightIn(min = 48.dp)
            ) { Text("Kaydet") }
        },
        dismissButton = { TextButton(onClick = { requestDismiss() }, modifier = Modifier.heightIn(min = 48.dp)) { Text("İptal") } }
    )
    if (confirmDiscard) ConfirmDiscardDialog(
        onKeepEditing = { confirmDiscard = false },
        onDiscard = onDismiss
    )
}

@Composable
internal fun EditBpDialog(
    item: BloodPressure,
    onDismiss: () -> Unit,
    onSave: (BloodPressure) -> Unit,
    onDelete: () -> Unit
) {
    var systolic by rememberSaveable { mutableStateOf(item.systolic.toString()) }
    var diastolic by rememberSaveable { mutableStateOf(item.diastolic.toString()) }
    var pulse by rememberSaveable { mutableStateOf(item.pulse?.toString() ?: "") }
    var note by rememberSaveable { mutableStateOf(item.note) }
    var measuredAt by rememberSaveable(item.id) { mutableLongStateOf(item.measuredAt) }
    var confirmDelete by remember { mutableStateOf(false) }
    var confirmDiscard by remember { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current
    fun requestDismiss() {
        val dirty = systolic != item.systolic.toString() ||
            diastolic != item.diastolic.toString() || pulse != item.pulse?.toString().orEmpty() ||
            note != item.note || measuredAt != item.measuredAt
        if (dirty) confirmDiscard = true else onDismiss()
    }
    androidx.activity.compose.BackHandler { requestDismiss() }

    AlertDialog(
        onDismissRequest = { requestDismiss() },
        title = { Text("Tansiyon Kaydını Düzenle") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(systolic, { systolic = it.filter(Char::isDigit) }, label = { Text("Büyük tansiyon") },
                        colors = logoFieldColors()
                    )
                OutlinedTextField(diastolic, { diastolic = it.filter(Char::isDigit) }, label = { Text("Küçük tansiyon") },
                        colors = logoFieldColors()
                    )
                OutlinedTextField(pulse, { pulse = it.filter(Char::isDigit) }, label = { Text("Nabız") },
                        colors = logoFieldColors()
                    )
                OutlinedTextField(note, { note = it }, label = { Text("Not") },
                        colors = logoFieldColors()
                    )
                OutlinedButton(
                    onClick = { pickDate(context) { measuredAt = it } },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.5.dp, LogoColor),
                    colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Vantablack10,
                                contentColor = LogoColorDark
                            )
                ) {
                    Text("Tarih: ${formatDate(measuredAt)}", color = LogoColorDark, style = MaterialTheme.typography.labelLarge.copy(shadow = LogoTextShadow))
                }
                OutlinedButton(
                    onClick = { confirmDelete = true },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.5.dp, LogoColor),
                    colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Vantablack10,
                                contentColor = LogoColorDark
                            )
                ) { Text("Kaydı sil", color = LogoColorDark, style = MaterialTheme.typography.labelLarge.copy(shadow = LogoTextShadow)) }
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val validation = HealthDataValidator.bloodPressureError(
                        systolic.toIntOrNull(),
                        diastolic.toIntOrNull(),
                        pulse.toIntOrNull()
                    )
                    if (validation != null) {
                        error = validation
                    } else {
                        onSave(
                            item.copy(
                                systolic = systolic.toInt(),
                                diastolic = diastolic.toInt(),
                                pulse = pulse.toIntOrNull(),
                                measuredAt = measuredAt,
                                note = note.trim()
                            )
                        )
                    }
                }
            ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LogoColor.copy(alpha = 0.25f),
                    contentColor = Color(0xFF123A37)
                )
            ) { Text("Kaydet") }
        },
        dismissButton = {
            TextButton(
                onClick = { requestDismiss() },
                colors = ButtonDefaults.textButtonColors(
                            containerColor = Vantablack10,
                            contentColor = LogoColorDark
                        )
            ) { Text("İptal") }
        }
    )

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Ölçümü sil") },
            text = { Text("Bu tansiyon kaydını silmek istediğinizden emin misiniz?") },
            confirmButton = {
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoColor.copy(alpha = 0.25f),
                        contentColor = Color(0xFF123A37)
                    )
                ) { Text("Evet, sil") }
            },
            dismissButton = {
                TextButton(
                    onClick = { confirmDelete = false },
                    colors = ButtonDefaults.textButtonColors(
                            containerColor = Vantablack10,
                            contentColor = LogoColorDark
                        )
                ) { Text("İptal") }
            }
        )
    }
    if (confirmDiscard) ConfirmDiscardDialog(
        onKeepEditing = { confirmDiscard = false },
        onDiscard = onDismiss
    )
}

@Composable
internal fun EditGlucoseDialog(
    item: BloodGlucose,
    onDismiss: () -> Unit,
    onSave: (BloodGlucose) -> Unit,
    onDelete: () -> Unit
) {
    var value by rememberSaveable { mutableStateOf(item.valueMgDl.toString()) }
    var type by rememberSaveable(item.id) { mutableStateOf(GlucoseMeasurementType.fromStorage(item.measurementType)) }
    var note by rememberSaveable { mutableStateOf(item.note) }
    var measuredAt by rememberSaveable(item.id) { mutableLongStateOf(item.measuredAt) }
    var confirmDelete by remember { mutableStateOf(false) }
    var confirmDiscard by remember { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current
    fun requestDismiss() {
        val dirty = value != item.valueMgDl.toString() || type.displayName != item.measurementType ||
            note != item.note || measuredAt != item.measuredAt
        if (dirty) confirmDiscard = true else onDismiss()
    }
    androidx.activity.compose.BackHandler { requestDismiss() }

    AlertDialog(
        onDismissRequest = { requestDismiss() },
        title = { Text("Kan Şekeri Kaydını Düzenle") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value, { value = it.filter(Char::isDigit) }, label = { Text("mg/dL") },
                        colors = logoFieldColors()
                    )
                GlucoseMeasurementType.entries.forEach { option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = type == option, onClick = { type = option })
                        Text(option.displayName)
                    }
                }
                OutlinedTextField(note, { note = it }, label = { Text("Not") },
                        colors = logoFieldColors()
                    )
                OutlinedButton(
                    onClick = { pickDate(context) { measuredAt = it } },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.5.dp, LogoColor),
                    colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Vantablack10,
                                contentColor = LogoColorDark
                            )
                ) {
                    Text("Tarih: ${formatDate(measuredAt)}", color = LogoColorDark, style = MaterialTheme.typography.labelLarge.copy(shadow = LogoTextShadow))
                }
                OutlinedButton(
                    onClick = { confirmDelete = true },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.5.dp, LogoColor),
                    colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Vantablack10,
                                contentColor = LogoColorDark
                            )
                ) { Text("Kaydı sil", color = LogoColorDark, style = MaterialTheme.typography.labelLarge.copy(shadow = LogoTextShadow)) }
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val validation = HealthDataValidator.glucoseError(value.toIntOrNull())
                    if (validation != null) {
                        error = validation
                    } else {
                        onSave(
                            item.copy(
                                valueMgDl = value.toInt(),
                                measurementType = type.displayName,
                                measuredAt = measuredAt,
                                note = note.trim()
                            )
                        )
                    }
                }
            ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LogoColor.copy(alpha = 0.25f),
                    contentColor = Color(0xFF123A37)
                )
            ) { Text("Kaydet") }
        },
        dismissButton = {
            TextButton(
                onClick = { requestDismiss() },
                colors = ButtonDefaults.textButtonColors(
                            containerColor = Vantablack10,
                            contentColor = LogoColorDark
                        )
            ) { Text("İptal") }
        }
    )

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Ölçümü sil") },
            text = { Text("Bu kan şekeri kaydını silmek istediğinizden emin misiniz?") },
            confirmButton = {
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoColor.copy(alpha = 0.25f),
                        contentColor = Color(0xFF123A37)
                    )
                ) { Text("Evet, sil") }
            },
            dismissButton = {
                TextButton(
                    onClick = { confirmDelete = false },
                    colors = ButtonDefaults.textButtonColors(
                            containerColor = Vantablack10,
                            contentColor = LogoColorDark
                        )
                ) { Text("İptal") }
            }
        )
    }
    if (confirmDiscard) ConfirmDiscardDialog(
        onKeepEditing = { confirmDiscard = false },
        onDiscard = onDismiss
    )
}

@Composable
internal fun ConfirmDiscardDialog(
    onKeepEditing: () -> Unit,
    onDiscard: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onKeepEditing,
        title = { Text("Değişiklikler kaydedilmedi") },
        text = { Text("Girdiğiniz bilgiler kaybolacak. Formdan çıkmak istediğinizden emin misiniz?") },
        confirmButton = {
            Button(onClick = onDiscard, modifier = Modifier.heightIn(min = 48.dp)) {
                Text("Kaydetmeden çık")
            }
        },
        dismissButton = {
            TextButton(onClick = onKeepEditing, modifier = Modifier.heightIn(min = 48.dp)) {
                Text("Düzenlemeye devam et")
            }
        }
    )
}


internal fun pickDate(context: Context, onPicked: (Long) -> Unit) {
    val c = Calendar.getInstance()
    DatePickerDialog(context, { _, y, m, d ->
        onPicked(Calendar.getInstance().apply { set(y, m, d, 0, 0, 0); set(Calendar.MILLISECOND, 0) }.timeInMillis)
    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
}

internal fun pickDateString(context: Context, onPicked: (String) -> Unit) {
    val c = Calendar.getInstance()
    DatePickerDialog(context, { _, y, m, d ->
        onPicked("%04d-%02d-%02d".format(y, m + 1, d))
    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
}

internal fun formatDate(m: Long) = SimpleDateFormat("dd.MM.yyyy", Locale("tr", "TR")).format(Date(m))
internal fun formatDateTime(m: Long) = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("tr", "TR")).format(Date(m))



internal fun persistImageToAppStorage(
    context: Context,
    sourceUri: Uri,
    subfolder: String,
    prefix: String
): String? {
    return runCatching {
        val dir = File(context.filesDir, subfolder).apply { mkdirs() }
        val target = File(dir, "${prefix}_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(sourceUri).use { input ->
            requireNotNull(input)
            FileOutputStream(target).use { output -> input.copyTo(output) }
        }
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", target).toString()
    }.getOrNull()
}




private fun decodeManagedImage(context: Context, uri: Uri, maxDimension: Int = 4096): Bitmap? {
    val orientation = runCatching {
        context.contentResolver.openInputStream(uri).use { input ->
            input?.let { ExifInterface(it).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL) }
                ?: ExifInterface.ORIENTATION_NORMAL
        }
    }.getOrDefault(ExifInterface.ORIENTATION_NORMAL)

    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    context.contentResolver.openInputStream(uri).use { input ->
        BitmapFactory.decodeStream(input, null, bounds)
    }
    if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null
    var sample = 1
    while (max(bounds.outWidth / sample, bounds.outHeight / sample) > maxDimension) sample *= 2
    val decoded = context.contentResolver.openInputStream(uri).use { input ->
        BitmapFactory.decodeStream(
            input,
            null,
            BitmapFactory.Options().apply {
                inSampleSize = sample
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
        )
    } ?: return null

    val matrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.setScale(1f, -1f)
        ExifInterface.ORIENTATION_TRANSPOSE -> { matrix.setRotate(90f); matrix.postScale(-1f, 1f) }
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
        ExifInterface.ORIENTATION_TRANSVERSE -> { matrix.setRotate(-90f); matrix.postScale(-1f, 1f) }
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
    }
    if (matrix.isIdentity) return decoded
    return Bitmap.createBitmap(decoded, 0, 0, decoded.width, decoded.height, matrix, true).also {
        if (it !== decoded) decoded.recycle()
    }
}

internal fun cropAndSaveImage(
    context: Context,
    sourceUri: String,
    scale: Float,
    offset: Offset,
    viewport: IntSize,
    targetAspectRatio: Float,
    subfolder: String,
    prefix: String
): String? {
    return runCatching {
        val bitmap = decodeManagedImage(context, Uri.parse(sourceUri)) ?: return null

        val viewW = viewport.width.toFloat()
        val viewH = viewport.height.toFloat()
        if (viewW <= 0f || viewH <= 0f) return null

        val frameW: Float
        val frameH: Float
        if (viewW / viewH > targetAspectRatio) {
            frameH = viewH * 0.86f
            frameW = frameH * targetAspectRatio
        } else {
            frameW = viewW * 0.86f
            frameH = frameW / targetAspectRatio
        }
        val frameLeft = (viewW - frameW) / 2f
        val frameTop = (viewH - frameH) / 2f

        // Preview uses ContentScale.Fit. Recreate exactly the same base placement.
        val baseScale = min(viewW / bitmap.width.toFloat(), viewH / bitmap.height.toFloat())
        val totalScale = baseScale * scale
        val displayedW = bitmap.width * totalScale
        val displayedH = bitmap.height * totalScale
        val imageLeft = (viewW - displayedW) / 2f + offset.x
        val imageTop = (viewH - displayedH) / 2f + offset.y

        val outW = if (targetAspectRatio >= 1f) 1200 else 1000
        val outH = (outW / targetAspectRatio).toInt().coerceAtLeast(1)
        val output = Bitmap.createBitmap(outW, outH, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(output)
        canvas.drawColor(AndroidColor.WHITE)

        val outputScaleX = outW / frameW
        val outputScaleY = outH / frameH

        val matrix = android.graphics.Matrix().apply {
            postScale(totalScale * outputScaleX, totalScale * outputScaleY)
            postTranslate(
                (imageLeft - frameLeft) * outputScaleX,
                (imageTop - frameTop) * outputScaleY
            )
        }

        canvas.save()
        canvas.clipRect(0f, 0f, outW.toFloat(), outH.toFloat())
        canvas.drawBitmap(bitmap, matrix, android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG))
        canvas.restore()

        val dir = File(context.filesDir, subfolder).apply { mkdirs() }
        val outFile = File(dir, "${prefix}_crop_${System.currentTimeMillis()}.jpg")
        FileOutputStream(outFile).use { out ->
            output.compress(Bitmap.CompressFormat.JPEG, 94, out)
        }

        output.recycle()
        bitmap.recycle()

        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            outFile
        ).toString()
    }.getOrNull()
}

@Composable
internal fun CropImageDialog(
    sourceUri: String,
    circularPreview: Boolean,
    targetAspectRatio: Float,
    outputSubfolder: String,
    outputPrefix: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var viewport by remember { mutableStateOf(IntSize.Zero) }
    var saving by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Fotoğrafı çerçeveye göre ayarla") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Fotoğrafın tamamı gösterilir. Tek parmakla kaydırın, iki parmakla büyütüp küçültün.")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .onSizeChanged { viewport = it }
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(0.5f, 8f)
                                offset += pan
                            }
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(sourceUri),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y
                            )
                    )

                    Canvas(Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val aspect = targetAspectRatio
                        val frameW: Float
                        val frameH: Float
                        if (w / h > aspect) {
                            frameH = h * 0.86f
                            frameW = frameH * aspect
                        } else {
                            frameW = w * 0.86f
                            frameH = frameW / aspect
                        }
                        val left = (w - frameW) / 2f
                        val top = (h - frameH) / 2f

                        drawRect(Color.Black.copy(alpha = 0.48f), Offset.Zero, androidx.compose.ui.geometry.Size(w, top))
                        drawRect(Color.Black.copy(alpha = 0.48f), Offset(0f, top + frameH), androidx.compose.ui.geometry.Size(w, h - top - frameH))
                        drawRect(Color.Black.copy(alpha = 0.48f), Offset(0f, top), androidx.compose.ui.geometry.Size(left, frameH))
                        drawRect(Color.Black.copy(alpha = 0.48f), Offset(left + frameW, top), androidx.compose.ui.geometry.Size(w - left - frameW, frameH))
                        if (circularPreview) {
                            drawCircle(
                                color = Color.White,
                                radius = min(frameW, frameH) / 2f,
                                center = Offset(left + frameW / 2f, top + frameH / 2f),
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f)
                            )
                        } else {
                            drawRect(
                                color = Color.White,
                                topLeft = Offset(left, top),
                                size = androidx.compose.ui.geometry.Size(frameW, frameH),
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                enabled = !saving && viewport != IntSize.Zero,
                onClick = {
                    saving = true
                    val result = cropAndSaveImage(
                        context = context,
                        sourceUri = sourceUri,
                        scale = scale,
                        offset = offset,
                        viewport = viewport,
                        targetAspectRatio = targetAspectRatio,
                        subfolder = outputSubfolder,
                        prefix = outputPrefix
                    )
                    saving = false
                    if (result != null) onConfirm(result)
                }
            ) { Text(if (saving) "Kaydediliyor…" else "Kırp ve kullan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("İptal") }
        }
    )
}
