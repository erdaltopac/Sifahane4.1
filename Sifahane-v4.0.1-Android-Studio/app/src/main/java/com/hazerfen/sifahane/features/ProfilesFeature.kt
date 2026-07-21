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
internal fun ProfilesScreen(
    profiles: List<UserProfile>,
    activeId: Long,
    modifier: Modifier,
    onSelect: (UserProfile) -> Unit,
    onEdit: (UserProfile) -> Unit,
    onDelete: (UserProfile) -> Unit,
    onChangePattern: (UserProfile) -> Unit
) {
    var deleteTarget by remember { mutableStateOf<UserProfile?>(null) }
    var secondConfirm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(profiles, key = { it.id }) { p ->
            SifahaneCard(modifier = Modifier.fillMaxWidth(), onClick = { onSelect(p) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!p.photoUri.isNullOrBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(p.photoUri),
                            contentDescription = "Kişi fotoğrafı",
                            modifier = Modifier
                                .size(104.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = LogoColorDark,
                            modifier = Modifier.size(92.dp)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "${p.name} ${p.surname}".trim(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        if (!p.birthDate.isNullOrBlank()) {
                            Text(
                                "Doğum tarihi: ${p.birthDate}",
                                textAlign = TextAlign.Center
                            )
                        }
                        Text(
                            "Kan grubu: ${p.bloodGroup}",
                            textAlign = TextAlign.Center
                        )
                        if (p.relation.isNotBlank()) {
                            Text(
                                "Yakınlık: ${p.relation}",
                                textAlign = TextAlign.Center
                            )
                        }
                        if (p.id == activeId) {
                            Text(
                                "Aktif kullanıcı",
                                color = LogoColor,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    shadow = LogoTextShadow
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onEdit(p) }) {
                            Icon(
                                Icons.Default.Edit,
                                "Düzenle",
                                tint = LogoColorDark
                            )
                        }
                        IconButton(onClick = { onChangePattern(p) }) {
                            Icon(
                                Icons.Default.Pattern,
                                "Deseni değiştir",
                                tint = LogoColorDark
                            )
                        }
                        IconButton(
                            enabled = profiles.size > 1,
                            onClick = {
                                deleteTarget = p
                                secondConfirm = false
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                "Kişiyi sil"
                            )
                        }
                    }
                }
            }
        }
    }

    deleteTarget?.let { target ->
        AlertDialog(
            onDismissRequest = {
                deleteTarget = null
                secondConfirm = false
            },
            title = { Text(if (secondConfirm) "Son onay" else "Kullanıcıyı sil") },
            text = {
                Text(
                    if (secondConfirm)
                        "${target.name} kullanıcısı ile ilişkili ilaçlar, ölçümler ve geçmiş kayıtları kalıcı olarak silinecek."
                    else
                        "${target.name} kullanıcısını silmek istediğinizden emin misiniz?"
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (!secondConfirm) secondConfirm = true
                    else {
                        onDelete(target)
                        deleteTarget = null
                        secondConfirm = false
                    }
                }) { Text(if (secondConfirm) "Kalıcı olarak sil" else "Devam et") }
            },
            dismissButton = {
                TextButton(onClick = {
                    deleteTarget = null
                    secondConfirm = false
                }) { Text("İptal") }
            }
        )
    }
}

