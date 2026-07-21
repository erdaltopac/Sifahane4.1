package com.hazerfen.sifahane
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
import androidx.compose.ui.platform.testTag
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.withTransaction
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import coil.compose.rememberAsyncImagePainter
import com.hazerfen.sifahane.alarm.AlarmScheduler
import com.hazerfen.sifahane.alarm.AppointmentPreferences
import com.hazerfen.sifahane.alarm.AppointmentAlarmScheduler
import com.hazerfen.sifahane.data.*
import com.hazerfen.sifahane.export.SimpleXlsxWriter
import com.hazerfen.sifahane.diagnostics.SupportDiagnostics
import com.hazerfen.sifahane.backup.*
import com.hazerfen.sifahane.features.*
import com.hazerfen.sifahane.viewmodel.SifahaneViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContent {
            SifahaneTheme {
                var showSplash by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    delay(2000)
                    showSplash = false
                }
                if (showSplash) {
                    SplashScreen()
                } else {
                    SifahaneRoot()
                }
            }
        }
    }
}


@Composable
internal fun SplashScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable {
                runCatching {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.hazerfen.com.tr")
                        )
                    )
                }
            }
            .padding(horizontal = 22.dp)
            .padding(top = navigationBarHeight, bottom = navigationBarHeight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                color = LogoColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    shadow = LogoTextShadow
                )
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Bismillâhirrahmânirrahîm",
                color = LogoColorDark,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    shadow = LogoTextShadow
                )
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = "İyiliği sonsuz, ikramı bol Allah’ın adıyla.",
                color = LogoColorDark,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    shadow = LogoTextShadow
                )
            )
        }

        Image(
            painter = rememberAsyncImagePainter(R.drawable.sifahane_logo),
            contentDescription = "Şifahane logosu",
            modifier = Modifier
                .fillMaxSize(0.72f)
                .align(Alignment.Center)
                .alpha(0.25f),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Geliştirici",
                color = LogoColorDark,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    shadow = LogoTextShadow
                )
            )
            Text(
                text = "Erdal Topaç",
                color = LogoColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    shadow = LogoTextShadow
                )
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Ücretsizdir",
                color = LogoColorDark.copy(alpha = 0.50f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontStyle = FontStyle.Italic,
                    shadow = LogoTextShadow
                )
            )
        }
    }
}

@Composable
internal fun PartnerLogoBanner() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val density = LocalDensity.current
    val displayMetrics = context.resources.displayMetrics
    val physicalYdpi = displayMetrics.ydpi.takeIf { it.isFinite() && it > 0f }
        ?: displayMetrics.densityDpi.toFloat()
    val combinedHeight = with(density) { (physicalYdpi * 10.0f / 25.4f).toDp() }
    val transitionHeight = 8.dp
    val bannerHeight = (combinedHeight - transitionHeight).coerceAtLeast(1.dp)
    val logoHeight = bannerHeight * 0.95f
    // Son kaynak görsel 1200x250 pikseldir; genişlik yalnız yükseklik ve özgün
    // en-boy oranından üretilir. Böylece hiçbir kopya esnetilmez veya küçülmez.
    val logoWidth = logoHeight * (1200f / 250f)
    val logoWidthPx = with(density) { logoWidth.toPx() }
    // Akış hızı cihaz ekranının genişliğine göre belirlenir: beş dakikada bir ekran.
    val animationDurationMillis = (
        300_000f * logoWidthPx / displayMetrics.widthPixels.coerceAtLeast(1)
    ).roundToInt().coerceAtLeast(1)
    val animationsEnabled = remember { android.animation.ValueAnimator.areAnimatorsEnabled() }
    val lifecycleOwner = LocalLifecycleOwner.current
    var lifecycleResumed by remember(lifecycleOwner) {
        mutableStateOf(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, _ ->
            lifecycleResumed = lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    val offset = remember(logoWidthPx) { Animatable(0f) }
    LaunchedEffect(animationsEnabled, lifecycleResumed, logoWidthPx, animationDurationMillis) {
        if (!animationsEnabled) {
            offset.snapTo(0f)
            return@LaunchedEffect
        }
        if (!lifecycleResumed) return@LaunchedEffect
        while (true) {
            val remainingFraction = ((logoWidthPx + offset.value) / logoWidthPx)
                .coerceIn(0.01f, 1f)
            offset.animateTo(
                targetValue = -logoWidthPx,
                animationSpec = tween(
                    durationMillis = (animationDurationMillis * remainingFraction)
                        .roundToInt()
                        .coerceAtLeast(1),
                    easing = LinearEasing
                )
            )
            offset.snapTo(0f)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(combinedHeight)
            .clipToBounds()
            .clickable {
                runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hazerfen.com.tr"))) }
            }
            .semantics { contentDescription = "Hazerfen kuruluş logoları. İnternet sitesini harici tarayıcıda açar." }
    ) {
        Column(Modifier.fillMaxSize()) {
            BottomShellSoftTransition(mirrored = true, height = transitionHeight)
            BoxWithConstraints(
                Modifier
                    .fillMaxWidth()
                    .height(bannerHeight)
                    .background(Color.White)
            ) {
                val copies = (maxWidth / logoWidth).toInt() + 3
                Row(
                    modifier = Modifier
                        .requiredWidth(logoWidth * copies)
                        .fillMaxHeight()
                        .graphicsLayer { translationX = offset.value },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    repeat(copies) {
                        Image(
                            painter = painterResource(R.drawable.banner_hazerfen),
                            contentDescription = null,
                            modifier = Modifier
                                .width(logoWidth)
                                .height(logoHeight)
                                .alpha(1.00f),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
        // Tek cam katmanı hem yumuşak geçişi hem de kayan logo alanını kaplar.
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0xFF050505).copy(alpha = 0.10f))
        )
    }
}

@Composable
internal fun BottomShellSoftTransition(
    mirrored: Boolean,
    height: androidx.compose.ui.unit.Dp = 8.dp
) {
    val colors = listOf(
        Color(0xFF050505).copy(alpha = 0.10f),
        Color(0xFF050505).copy(alpha = 0.05f),
        Color.Transparent
    ).let { if (mirrored) it.reversed() else it }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(Brush.verticalGradient(colors))
    )
}

@Composable
internal fun GlassLogoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val shape = StandardFieldShape
    Box(
        modifier = modifier
            .height(52.dp)
            .sifahaneSoftBoundary(4.dp)
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.fillMaxSize(),
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = LogoColor.copy(alpha = 0.25f),
                contentColor = LogoColorDark,
                disabledContainerColor = LogoColor.copy(alpha = 0.12f),
                disabledContentColor = LogoColorDark.copy(alpha = 0.35f)
            )
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    shadow = LogoTextShadow
                )
            )
        }
        Box(
            Modifier
                .matchParentSize()
                .clip(shape)
                .background(Color(0xFF050505).copy(alpha = 0.05f))
        )
    }
}

@Composable
internal fun PlainImportOption(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = LogoColorDark
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 2,
            style = MaterialTheme.typography.labelMedium.copy(shadow = LogoTextShadow)
        )
    }
}

internal fun Modifier.vantablackGlassOverlay(): Modifier =
    clip(RoundedCornerShape(10.dp)).drawWithContent {
    drawContent()
    drawRect(Color(0xFF050505).copy(alpha = 0.05f))
}

internal fun Modifier.vantablackPageGlassOverlay(): Modifier = drawWithContent {
    drawContent()
    drawRect(Color(0xFF050505).copy(alpha = 0.05f))
}

@Composable
internal fun WatermarkContainer(content: @Composable BoxScope.() -> Unit) {
    Box(Modifier.fillMaxSize().sifahaneSoftBoundary(2.dp)) {
        Image(
            painter = rememberAsyncImagePainter(R.drawable.sifahane_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .alpha(0.15f)
                .zIndex(0f),
            contentScale = ContentScale.Fit
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
            content = content
        )
    }
}


internal val LogoColor = Color(0xFF72D4CD)
internal val LogoColorDark: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface

internal val Vantablack10: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f)

internal val Vantablack05: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
internal val LogoColorSoft = Color(0x1A72D4CD)
internal val LogoTextShadow = Shadow(
    color = Color(0xFF050505),
    offset = Offset(0.35f, 0.35f),
    blurRadius = 0.45f
)

@Composable
internal fun SifahanePageTitle(
    activeUser: String,
    pageKey: Any
) {
    var showSideLogos by remember(pageKey, activeUser) {
        mutableStateOf(true)
    }

    LaunchedEffect(pageKey, activeUser) {
        while (true) {
            showSideLogos = true
            delay(5000)
            showSideLogos = false

            val now = System.currentTimeMillis()
            val untilNextMinute = 60_000L - (now % 60_000L)
            delay(untilNextMinute)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (showSideLogos) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.sifahane_logo),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(8.dp))
            }

            Text(
                text = "Şifahane",
                color = LogoColor,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    shadow = LogoTextShadow
                ),
                textAlign = TextAlign.Center
            )

            if (showSideLogos) {
                Spacer(Modifier.width(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.sifahane_logo),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        if (activeUser.isNotBlank()) {
            Text(
                text = activeUser,
                style = MaterialTheme.typography.bodyMedium.copy(
                    shadow = LogoTextShadow
                ),
                color = LogoColorDark,
                textAlign = TextAlign.Center
            )
        }
    }
}




@Composable
internal fun logoFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Vantablack10,
    unfocusedContainerColor = Vantablack10,
    disabledContainerColor = Vantablack10,
    focusedTextColor = LogoColorDark,
    unfocusedTextColor = LogoColorDark,
    focusedLabelColor = LogoColorDark,
    unfocusedLabelColor = LogoColorDark,
    focusedBorderColor = LogoColor,
    unfocusedBorderColor = LogoColorDark.copy(alpha = 0.55f),
    cursorColor = LogoColorDark
)


@Composable
internal fun medicationFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Vantablack05,
    unfocusedContainerColor = Vantablack05,
    disabledContainerColor = Vantablack05,
    focusedTextColor = LogoColorDark,
    unfocusedTextColor = LogoColorDark,
    disabledTextColor = LogoColorDark.copy(alpha = 0.65f),
    focusedLabelColor = LogoColor,
    unfocusedLabelColor = LogoColorDark,
    focusedBorderColor = LogoColor,
    unfocusedBorderColor = LogoColorDark.copy(alpha = 0.55f),
    cursorColor = LogoColorDark
)

@Composable
internal fun medicationOutlinedButtonColors() =
    ButtonDefaults.outlinedButtonColors(
        containerColor = Vantablack05,
        contentColor = LogoColorDark,
        disabledContainerColor = Vantablack05,
        disabledContentColor = LogoColorDark.copy(alpha = 0.45f)
    )

@Composable
internal fun MedicationCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    centered: Boolean = false,
    boldWhenChecked: Boolean = false,
    emphasized: Boolean? = null,
    checkedColor: Color = LogoColor
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (centered) Arrangement.Center else Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = if (checked) {
                Modifier.shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(4.dp),
                    ambientColor = Color.Black,
                    spotColor = Color.Black
                )
            } else {
                Modifier
            }
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = checkedColor,
                    uncheckedColor = LogoColorDark,
                    checkmarkColor = Color.Black
                )
            )
        }
        Text(
            text = label,
            color = LogoColorDark,
            style = (if (centered) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodyLarge).copy(
                fontWeight = if (emphasized ?: (boldWhenChecked && checked)) FontWeight.Bold else FontWeight.Normal,
                shadow = LogoTextShadow
            )
        )
    }
}


@Composable
internal fun profileFieldColors() = standardFieldColors()

@Composable
internal fun profileOutlinedButtonColors() =
    ButtonDefaults.outlinedButtonColors(
        containerColor = Vantablack05,
        contentColor = LogoColorDark,
        disabledContainerColor = Vantablack05,
        disabledContentColor = LogoColorDark.copy(alpha = 0.45f)
    )

@Composable
internal fun IdleHeartOverlay(
    visible: Boolean,
    animationKey: Long
) {
    if (!visible) return

    val scale = remember(animationKey) { Animatable(0.05f) }
    val alpha = remember(animationKey) { Animatable(0f) }

    LaunchedEffect(animationKey) {
        alpha.snapTo(0f)
        scale.snapTo(0.05f)

        alpha.animateTo(0.25f, tween(250))
        scale.animateTo(1.00f, tween(900))
        scale.animateTo(0.88f, tween(220))
        scale.animateTo(1.00f, tween(220))
        scale.animateTo(0.88f, tween(220))
        scale.animateTo(1.00f, tween(220))
        delay(450)
        alpha.animateTo(0f, tween(350))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                this.alpha = alpha.value
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = LogoColor,
            modifier = Modifier.fillMaxSize(0.92f)
        )
    }
}


internal fun titleCaseTr(value: String): String {
    val locale = Locale("tr", "TR")
    return value.lowercase(locale)
        .split(Regex("\\s+"))
        .joinToString(" ") { word ->
            if (word.isBlank()) word
            else word.substring(0, 1).uppercase(locale) + word.substring(1)
        }
}

@Composable
internal fun ThemedDateButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    medicationStyle: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        border = BorderStroke(1.5.dp, LogoColor),
        colors = if (medicationStyle) {
            ButtonDefaults.outlinedButtonColors(
                containerColor = Vantablack05,
                contentColor = LogoColorDark
            )
        } else {
            profileOutlinedButtonColors()
        }
    ) {
        Text(
            text = text,
            color = LogoColorDark,
            style = MaterialTheme.typography.labelLarge.copy(
                shadow = LogoTextShadow
            )
        )
    }
}

@Composable
internal fun VitalLineChart(
    title: String,
    series: List<Pair<String, List<Pair<Long, Float>>>>,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val chartForeground = MaterialTheme.colorScheme.onSurface
    val chartSurface = MaterialTheme.colorScheme.surfaceVariant
    SifahaneCard(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(
                titleCaseTr(title),
                color = LogoColorDark,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    shadow = LogoTextShadow
                )
            )
            Spacer(Modifier.height(8.dp))

            val allValues = series.flatMap { it.second }.map { it.second }
            if (allValues.isEmpty()) {
                Text("Seçilen tarih aralığında kayıt bulunamadı.")
            } else {
                val minValue = allValues.minOrNull() ?: 0f
                val maxValue = allValues.maxOrNull() ?: 1f
                val span = (maxValue - minValue).takeIf { abs(it) > 0.001f } ?: 1f
                val allTimes = series.flatMap { it.second }.map { it.first }
                val minTime = allTimes.minOrNull() ?: 0L
                val maxTime = allTimes.maxOrNull() ?: minTime + 1L
                val timeSpan = (maxTime - minTime).coerceAtLeast(1L)

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(chartSurface.copy(alpha = 0.72f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    val left = 58f
                    val right = size.width - 8f
                    val top = 8f
                    val bottom = size.height - 30f
                    val plotWidth = (right - left).coerceAtLeast(1f)
                    val plotHeight = (bottom - top).coerceAtLeast(1f)
                    val labelPaint = Paint().apply {
                        color = AndroidColor.rgb(58, 143, 137)
                        textSize = 22f
                        isAntiAlias = true
                    }
                    repeat(5) { tick ->
                        val ratio = tick / 4f
                        val y = bottom - ratio * plotHeight
                        val value = minValue + ratio * span
                        drawLine(
                            color = Color.Black.copy(alpha = 0.10f),
                            start = Offset(left, y), end = Offset(right, y), strokeWidth = 1f
                        )
                        drawContext.canvas.nativeCanvas.drawText("%.0f".format(value), 2f, y + 7f, labelPaint)
                    }
                    val dateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())
                    repeat(3) { tick ->
                        val ratio = tick / 2f
                        val x = left + ratio * plotWidth
                        val time = minTime + (timeSpan * ratio).toLong()
                        val label = dateFormat.format(Date(time))
                        val width = labelPaint.measureText(label)
                        drawContext.canvas.nativeCanvas.drawText(label, (x - width / 2).coerceIn(0f, size.width - width), size.height - 3f, labelPaint)
                    }
                    drawLine(
                        color = chartForeground.copy(alpha = 0.55f),
                        start = Offset(left, bottom),
                        end = Offset(right, bottom),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = chartForeground.copy(alpha = 0.55f),
                        start = Offset(left, top),
                        end = Offset(left, bottom),
                        strokeWidth = 2f
                    )

                    series.forEachIndexed { index, (_, points) ->
                        val lineColor = if (index % 2 == 0) LogoColor else chartForeground
                        points.sortedBy { it.first }.zipWithNext().forEach { (a, b) ->
                            val ax = left + ((a.first - minTime).toFloat() / timeSpan.toFloat()) * plotWidth
                            val bx = left + ((b.first - minTime).toFloat() / timeSpan.toFloat()) * plotWidth
                            val ay = bottom - ((a.second - minValue) / span) * plotHeight
                            val by = bottom - ((b.second - minValue) / span) * plotHeight
                            drawLine(
                                color = lineColor,
                                start = Offset(ax, ay),
                                end = Offset(bx, by),
                                strokeWidth = 6f,
                                cap = StrokeCap.Round
                            )
                        }
                        points.forEach { point ->
                            val x = left + ((point.first - minTime).toFloat() / timeSpan.toFloat()) * plotWidth
                            val y = bottom - ((point.second - minValue) / span) * plotHeight
                            drawCircle(lineColor, radius = 6f, center = Offset(x, y))
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                series.forEachIndexed { index, (name, _) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(12.dp)
                                .background(
                                    if (index % 2 == 0) LogoColor else chartForeground,
                                    CircleShape
                                )
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(titleCaseTr(name), color = LogoColorDark)
                    }
                }
                Text(
                    "En düşük: ${"%.1f".format(minValue)} • En yüksek: ${"%.1f".format(maxValue)}",
                    color = LogoColorDark,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            confirmSensitiveShare(
                                context = context,
                                contentDescription = "Grafik raporu",
                                encrypted = false
                            ) {
                                val file = createVitalChartPng(context, title, series)
                                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                                context.startActivity(Intent.createChooser(
                                    Intent(Intent.ACTION_SEND).apply {
                                        type = "image/png"
                                        putExtra(Intent.EXTRA_STREAM, uri)
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        clipData = android.content.ClipData.newUri(context.contentResolver, file.name, uri)
                                    }, "Grafik raporunu paylaş"
                                ))
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) { Icon(Icons.Default.Share, null); Spacer(Modifier.width(4.dp)); Text("PAYLAŞ") }
                    OutlinedButton(
                        onClick = {
                            val file = createVitalChartPng(context, title, series)
                            saveChartToGallery(context, file)
                        },
                        modifier = Modifier.weight(1f)
                    ) { Icon(Icons.Default.SaveAlt, null); Spacer(Modifier.width(4.dp)); Text("GALERİ") }
                }
            }
        }
    }
}

internal fun createVitalChartPng(
    context: Context,
    title: String,
    series: List<Pair<String, List<Pair<Long, Float>>>>
): File {
    val bitmap = Bitmap.createBitmap(1600, 1000, Bitmap.Config.ARGB_8888)
    val canvas = AndroidCanvas(bitmap)
    canvas.drawColor(AndroidColor.WHITE)
    val text = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = AndroidColor.rgb(5, 5, 5) }
    text.textSize = 54f; text.isFakeBoldText = true
    canvas.drawText(titleCaseTr(title), 80f, 90f, text)
    text.textSize = 28f; text.isFakeBoldText = false
    canvas.drawText("Şifahane • ${BuildConfig.VERSION_NAME} • ${formatDateTime(System.currentTimeMillis())}", 80f, 140f, text)
    val values = series.flatMap { it.second }.map { it.second }
    val times = series.flatMap { it.second }.map { it.first }
    if (values.isNotEmpty() && times.isNotEmpty()) {
        val minValue = values.minOrNull() ?: 0f
        val maxValue = values.maxOrNull() ?: 1f
        val span = (maxValue - minValue).takeIf { abs(it) > .001f } ?: 1f
        val minTime = times.minOrNull() ?: 0L
        val maxTime = times.maxOrNull() ?: minTime + 1L
        val timeSpan = (maxTime - minTime).coerceAtLeast(1L)
        val left = 150f; val right = 1520f; val top = 210f; val bottom = 820f
        val axis = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = AndroidColor.rgb(5, 5, 5); strokeWidth = 3f }
        val grid = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = AndroidColor.argb(30, 5, 5, 5); strokeWidth = 2f }
        text.textSize = 26f
        repeat(5) { tick ->
            val ratio = tick / 4f
            val y = bottom - ratio * (bottom - top)
            canvas.drawLine(left, y, right, y, grid)
            canvas.drawText("%.0f".format(minValue + ratio * span), 35f, y + 9f, text)
        }
        canvas.drawLine(left, top, left, bottom, axis); canvas.drawLine(left, bottom, right, bottom, axis)
        val df = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        repeat(3) { tick ->
            val ratio = tick / 2f
            val x = left + ratio * (right - left)
            val label = df.format(Date(minTime + (timeSpan * ratio).toLong()))
            canvas.drawText(label, x - text.measureText(label) / 2, 865f, text)
        }
        series.forEachIndexed { index, (name, points) ->
            val color = if (index % 2 == 0) AndroidColor.rgb(114, 212, 205) else AndroidColor.rgb(5, 5, 5)
            val line = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color; strokeWidth = 8f; style = Paint.Style.STROKE }
            points.sortedBy { it.first }.zipWithNext().forEach { (a, b) ->
                fun x(t: Long) = left + ((t - minTime).toFloat() / timeSpan) * (right - left)
                fun y(v: Float) = bottom - ((v - minValue) / span) * (bottom - top)
                canvas.drawLine(x(a.first), y(a.second), x(b.first), y(b.second), line)
            }
            text.color = color; text.textSize = 28f
            canvas.drawText(titleCaseTr(name), 80f + index * 300f, 940f, text)
        }
    }
    val dir = File(context.cacheDir, "chart_reports").apply { mkdirs() }
    return File(dir, "Sifahane_Grafik_${System.currentTimeMillis()}.png").apply {
        outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        bitmap.recycle()
    }
}

internal fun saveChartToGallery(context: Context, file: File) {
    val values = ContentValues().apply {
        put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, file.name)
        put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(android.provider.MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Şifahane Raporları")
            put(android.provider.MediaStore.Images.Media.IS_PENDING, 1)
        }
    }
    val uri = context.contentResolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) ?: return
    context.contentResolver.openOutputStream(uri)?.use { output -> file.inputStream().use { it.copyTo(output) } }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.contentResolver.update(uri, ContentValues().apply {
            put(android.provider.MediaStore.Images.Media.IS_PENDING, 0)
        }, null, null)
    }
}


@Composable
internal fun CenteredLabeledField(
    label: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            modifier = Modifier.fillMaxWidth(),
            color = LogoColorDark,
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                shadow = LogoTextShadow
            )
        )
        Spacer(Modifier.height(1.dp))
        content()
    }
}



@Composable
internal fun CompactProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        textStyle = LocalTextStyle.current.copy(
            color = LogoColorDark,
            textAlign = TextAlign.Center,
            fontSize = 13.sp
        ),
        cursorBrush = SolidColor(LogoColorDark),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Vantablack05, StandardFieldShape)
                    .border(
                        width = 1.2.dp,
                        color = LogoColorDark.copy(alpha = 0.55f),
                        shape = StandardFieldShape
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    )
}

internal val StandardFieldShape = RoundedCornerShape(14.dp)

@Composable
internal fun standardFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Vantablack05,
    unfocusedContainerColor = Vantablack05,
    disabledContainerColor = Vantablack05,
    focusedTextColor = LogoColorDark,
    unfocusedTextColor = LogoColorDark,
    disabledTextColor = LogoColorDark.copy(alpha = 0.65f),
    focusedLabelColor = LogoColor,
    unfocusedLabelColor = LogoColorDark,
    focusedBorderColor = LogoColor,
    unfocusedBorderColor = LogoColorDark.copy(alpha = 0.55f),
    cursorColor = LogoColorDark
)

@Composable
internal fun CenteredCheckboxField(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            label,
            modifier = Modifier.fillMaxWidth(),
            color = LogoColorDark,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                shadow = LogoTextShadow
            )
        )
        Spacer(Modifier.height(0.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
                .then(
                    if (checked) {
                        Modifier.shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(6.dp),
                            ambientColor = Color.Black,
                            spotColor = Color.Black
                        )
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = LogoColor,
                    uncheckedColor = LogoColorDark,
                    checkmarkColor = Color.Black
                )
            )
        }
    }
}

internal enum class AdminPinPurpose {
    INITIAL_SETUP,
    UPDATE_VERIFICATION,
    UNLOCK_PROFILE,
    CHANGE_PATTERN,
    DELETE_PROFILE,
    ADMIN_SETTINGS
}

@Composable
internal fun AdminPinDialog(
    purpose: AdminPinPurpose,
    profileName: String? = null,
    verificationProfiles: List<UserProfile> = emptyList(),
    onDismiss: () -> Unit,
    onVerified: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var pin by remember { mutableStateOf("") }
    var newPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val initialSetup = purpose == AdminPinPurpose.INITIAL_SETUP
    val updateVerification = purpose == AdminPinPurpose.UPDATE_VERIFICATION

    AlertDialog(
        onDismissRequest = {
            if (!initialSetup && !updateVerification) onDismiss()
        },
        title = {
            Text(
                when {
                    initialSetup -> "Yönetici şifresi oluşturun"
                    updateVerification -> "Yönetici doğrulaması"
                    purpose == AdminPinPurpose.UNLOCK_PROFILE ->
                        "${profileName ?: "Kullanıcı"} için yönetici doğrulaması"
                    purpose == AdminPinPurpose.CHANGE_PATTERN ->
                        "Desen kilidini sıfırla"
                    purpose == AdminPinPurpose.DELETE_PROFILE ->
                        "Kullanıcı silme yetkisi"
                    else -> "Yönetici doğrulaması"
                },
                color = LogoColorDark,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    shadow = LogoTextShadow
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    when {
                        initialSetup ->
                            "İlk kullanım için yalnız sizin bildiğiniz 4–12 haneli sayısal bir yönetici şifresi belirleyin. Uygulamada varsayılan veya arka kapı şifresi yoktur."
                        updateVerification ->
                            "Güncellemeyi tamamlamak için mevcut yönetici şifresini girin."
                        purpose == AdminPinPurpose.UNLOCK_PROFILE ->
                            "Bu kişinin desenini güvenli biçimde sıfırlamak için yönetici şifresini girin."
                        purpose == AdminPinPurpose.CHANGE_PATTERN ->
                            "Bu kişinin desenini değiştirmek için yönetici şifresini girin."
                        else ->
                            "Yönetici işlemini onaylamak için yönetici şifresini girin."
                    }
                )

                if (initialSetup) {
                    OutlinedTextField(
                        value = newPin,
                        onValueChange = { value ->
                            if (value.length <= 12 && value.all(Char::isDigit)) newPin = value
                        },
                        label = { Text("Yeni yönetici şifresi") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = logoFieldColors()
                    )
                    OutlinedTextField(
                        value = confirmPin,
                        onValueChange = { value ->
                            if (value.length <= 12 && value.all(Char::isDigit)) confirmPin = value
                        },
                        label = { Text("Yeni şifreyi tekrar girin") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = logoFieldColors()
                    )
                } else {
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { value ->
                            if (value.length <= 12 && value.all(Char::isDigit)) pin = value
                        },
                        label = { Text("Yönetici şifresi") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = logoFieldColors()
                    )
                }

                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    error = null
                    if (initialSetup) {
                        when {
                            newPin.length !in 4..12 ->
                                error = "Yeni şifre 4–12 haneli olmalıdır."
                            newPin != confirmPin ->
                                error = "Yeni şifreler eşleşmiyor."
                            else -> {
                                AdminPinStore.saveNewPin(context, newPin)
                                onVerified()
                            }
                        }
                    } else {
                        val lockedFor = AdminPinStore.remainingLockoutMillis(context)
                        if (lockedFor > 0L) {
                            error = "Çok sayıda başarısız deneme yapıldı. ${((lockedFor + 999L) / 1000L)} saniye sonra yeniden deneyin."
                        } else {
                            val profilePinVerified = verificationProfiles
                                .asSequence()
                                .filter { UserRoles.isAdmin(it) && !it.adminPinHash.isNullOrBlank() }
                                .any { AdminCredentialHasher.verify(it, pin) }
                            val globalPinVerified = if (profilePinVerified) {
                                false
                            } else {
                                AdminPinStore.verify(context, pin)
                            }
                            if (profilePinVerified || globalPinVerified) {
                                if (profilePinVerified && AdminPinStore.requiresInitialSetup(context)) {
                                    runCatching { AdminPinStore.saveNewPin(context, pin) }
                                } else {
                                    AdminPinStore.clearFailureState(context)
                                }
                                onVerified()
                            } else {
                                if (AdminPinStore.requiresInitialSetup(context)) {
                                    AdminPinStore.recordExternalFailure(context)
                                }
                                val remaining = AdminPinStore.remainingLockoutMillis(context)
                                error = if (remaining > 0L) {
                                    "Çok sayıda başarısız deneme yapıldı. ${((remaining + 999L) / 1000L)} saniye sonra yeniden deneyin."
                                } else if (verificationProfiles.none { UserRoles.isAdmin(it) && !it.adminPinHash.isNullOrBlank() } && AdminPinStore.requiresInitialSetup(context)) {
                                    "Henüz yönetici şifresi oluşturulmamış. Yönetici profiliyle giriş yaptıktan sonra Ayarlar > Güvenlik bölümünden şifre belirleyin."
                                } else {
                                    "Yönetici şifresi yanlış."
                                }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LogoColor.copy(alpha = 0.25f),
                    contentColor = Color(0xFF123A37)
                )
            ) {
                Text(
                    if (initialSetup) "Şifreyi Kaydet" else "Doğrula",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        shadow = LogoTextShadow
                    )
                )
            }
        },
        dismissButton = {
            if (!initialSetup && !updateVerification) {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Vantablack10,
                        contentColor = LogoColorDark
                    )
                ) { Text("İptal") }
            }
        }
    )
}

internal enum class PatternMode { CREATE, CONFIRM, VERIFY }


internal fun biometricAvailability(context: Context): Int =
    BiometricManager.from(context).canAuthenticate(
        BiometricManager.Authenticators.BIOMETRIC_WEAK
    )

internal fun startBiometricAuthentication(
    activity: FragmentActivity,
    profileName: String,
    onSuccess: () -> Unit,
    onMessage: (String) -> Unit
) {
    val prompt = BiometricPrompt(
        activity,
        ContextCompat.getMainExecutor(activity),
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (
                    errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON &&
                    errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                    errorCode != BiometricPrompt.ERROR_CANCELED
                ) {
                    onMessage("Biyometrik giriş tamamlanamadı: $errString")
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onMessage("Biyometrik doğrulama başarısız. Yeniden deneyin.")
            }
        }
    )
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Şifahane biyometrik giriş")
        .setSubtitle("$profileName profilini açmak için doğrulayın")
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        .setNegativeButtonText("İptal")
        .build()
    prompt.authenticate(promptInfo)
}

@Composable
internal fun ProfilePatternGate(
    profile: UserProfile,
    forceCreate: Boolean = false,
    onUnlocked: () -> Unit,
    onCancel: () -> Unit,
    onAdminRequested: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = context as? FragmentActivity
    var firstPattern by remember(profile.id, forceCreate) {
        mutableStateOf<List<Int>?>(null)
    }
    var drawnPattern by remember(profile.id, forceCreate) {
        mutableStateOf(emptyList<Int>())
    }
    var currentPointer by remember(profile.id, forceCreate) {
        mutableStateOf<Offset?>(null)
    }
    var saving by remember { mutableStateOf(false) }

    val creating = forceCreate || !PatternStore.hasPattern(context, profile.id)
    var showPatternEntry by remember(profile.id, creating) { mutableStateOf(creating) }
    var message by remember(profile.id, creating) {
        mutableStateOf(
            if (creating) "Yeni deseninizi parmağınızı kaldırmadan çizin"
            else "Giriş yöntemini seçin"
        )
    }

    fun resetDrawing() {
        drawnPattern = emptyList()
        currentPointer = null
    }

    fun complete(pattern: List<Int>) {
        if (saving) return
        if (pattern.size < 4) {
            message = "Desen en az 4 noktadan oluşmalıdır."
            resetDrawing()
            return
        }

        if (creating) {
            if (firstPattern == null) {
                firstPattern = pattern.toList()
                message = "Aynı deseni ikinci kez çizin"
                resetDrawing()
                return
            }
            if (firstPattern != pattern) {
                firstPattern = null
                message = "İki desen eşleşmedi. İlk deseni yeniden çizin."
                resetDrawing()
                return
            }
            saving = true
            try {
                PatternStore.save(context, profile.id, pattern)
                if (PatternStore.matchesStoredPattern(context, profile.id, pattern)) {
                    message = "Desen başarıyla kaydedildi."
                    resetDrawing()
                    onUnlocked()
                } else {
                    firstPattern = null
                    message = "Desen kaydedilemedi. Yeniden deneyin."
                    resetDrawing()
                }
            } catch (_: Exception) {
                firstPattern = null
                message = "Desen kaydedilemedi. Yeniden deneyin."
                resetDrawing()
            } finally {
                saving = false
            }
            return
        }

        val remaining = PatternStore.remainingLockoutMillis(context, profile.id)
        if (remaining > 0) {
            message = "Çok fazla hatalı deneme. ${remaining / 1000 + 1} saniye bekleyin."
            resetDrawing()
            return
        }
        if (PatternStore.verify(context, profile.id, pattern)) {
            message = "Desen doğru."
            resetDrawing()
            onUnlocked()
        } else {
            val after = PatternStore.remainingLockoutMillis(context, profile.id)
            message = if (after > 0) {
                "Beş hatalı deneme yapıldı. 30 saniye bekleyin."
            } else {
                "Desen yanlış. Tekrar deneyin."
            }
            resetDrawing()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.sifahane_logo),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().alpha(0.10f),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!profile.photoUri.isNullOrBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(profile.photoUri),
                        contentDescription = "${profile.name} profil fotoğrafı",
                        modifier = Modifier.size(88.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Kullanıcı profili",
                        tint = LogoColor,
                        modifier = Modifier.size(72.dp)
                    )
                }

                Text(
                    "${profile.name} ${profile.surname}".trim(),
                    color = LogoColorDark,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        shadow = LogoTextShadow
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(message, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(16.dp))

                if (!creating) {
                    OutlinedButton(
                        onClick = {
                            showPatternEntry = true
                            message = "Deseninizi çizerek giriş yapın"
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) {
                        Icon(Icons.Default.Gesture, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Desen Kilidi")
                    }
                }

                if (showPatternEntry) {
                    Spacer(Modifier.height(12.dp))
                    SwipePatternGrid(
                        selected = drawnPattern,
                        pointer = currentPointer,
                        enabled = !saving,
                        onPatternChanged = {
                            drawnPattern = it.first
                            currentPointer = it.second
                        },
                        onCompleted = { complete(it) }
                    )
                    if (drawnPattern.isNotEmpty()) {
                        TextButton(onClick = { resetDrawing() }, enabled = !saving) {
                            Text("Deseni Temizle")
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }

                if (!creating) {
                    OutlinedButton(
                        onClick = {
                            when {
                                activity == null -> message = "Biyometrik giriş bu ekranda başlatılamadı."
                                !BiometricPreferences.isEnabled(context, profile.id) ->
                                    message = "Biyometrik giriş bu kullanıcı için kapalı. Ayarlar > Güvenlik ve Yetkilendirme bölümünden etkinleştirin."
                                biometricAvailability(context) != BiometricManager.BIOMETRIC_SUCCESS ->
                                    message = "Cihazda kullanılabilir biyometri bulunamadı veya henüz tanımlanmadı."
                                else -> startBiometricAuthentication(
                                    activity = activity,
                                    profileName = "${profile.name} ${profile.surname}".trim(),
                                    onSuccess = onUnlocked,
                                    onMessage = { message = it }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) {
                        Icon(Icons.Default.Fingerprint, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Biyometrik Giriş")
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onAdminRequested,
                        enabled = !saving,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) {
                        Icon(Icons.Default.Password, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Yönetici Şifresi")
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(
                        onClick = onCancel,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) { Text("Geri") }
                    TextButton(
                        onClick = onCancel,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) { Text("İptal") }
                } else {
                    Spacer(Modifier.height(8.dp))
                    TextButton(
                        onClick = onCancel,
                        enabled = !saving,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) { Text("Geri") }
                    TextButton(
                        onClick = onCancel,
                        enabled = !saving,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) { Text("İptal") }
                }
            }
        }
    }
}

@Composable
internal fun SwipePatternGrid(
    selected: List<Int>,
    pointer: Offset?,
    enabled: Boolean = true,
    onPatternChanged: (Pair<List<Int>, Offset?>) -> Unit,
    onCompleted: (List<Int>) -> Unit
) {
    val nodeRadius = 20.dp
    val hitRadiusPx = with(androidx.compose.ui.platform.LocalDensity.current) {
        38.dp.toPx()
    }
    val patternForeground = MaterialTheme.colorScheme.onSurface
    val patternSurface = MaterialTheme.colorScheme.surface

    Canvas(
        modifier = Modifier
            .size(300.dp)
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                var current = emptyList<Int>()

                fun nodeCenters(size: Size): List<Offset> {
                    val xs = listOf(size.width * 0.18f, size.width * 0.50f, size.width * 0.82f)
                    val ys = listOf(size.height * 0.18f, size.height * 0.50f, size.height * 0.82f)
                    return ys.flatMap { y -> xs.map { x -> Offset(x, y) } }
                }

                fun addHit(position: Offset) {
                    val centers = nodeCenters(Size(size.width.toFloat(), size.height.toFloat()))
                    val hit = centers.indexOfFirst {
                        (it - position).getDistance() <= hitRadiusPx
                    }
                    if (hit >= 0 && (hit + 1) !in current) {
                        current = current + (hit + 1)
                    }
                    onPatternChanged(current to position)
                }

                detectDragGestures(
                    onDragStart = { start ->
                        current = emptyList()
                        addHit(start)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        addHit(change.position)
                    },
                    onDragEnd = {
                        val completed = current.toList()
                        onPatternChanged(completed to null)
                        current = emptyList()
                        onCompleted(completed)
                    },
                    onDragCancel = {
                        onPatternChanged(emptyList<Int>() to null)
                        current = emptyList()
                    }
                )
            }
    ) {
        val xs = listOf(size.width * 0.18f, size.width * 0.50f, size.width * 0.82f)
        val ys = listOf(size.height * 0.18f, size.height * 0.50f, size.height * 0.82f)
        val centers = ys.flatMap { y -> xs.map { x -> Offset(x, y) } }

        selected.zipWithNext().forEach { (a, b) ->
            drawLine(
                color = LogoColor,
                start = centers[a - 1],
                end = centers[b - 1],
                strokeWidth = 9f,
                cap = StrokeCap.Round
            )
        }

        if (selected.isNotEmpty() && pointer != null) {
            drawLine(
                color = LogoColor.copy(alpha = 0.75f),
                start = centers[selected.last() - 1],
                end = pointer,
                strokeWidth = 7f,
                cap = StrokeCap.Round
            )
        }

        centers.forEachIndexed { index, center ->
            val active = (index + 1) in selected
            drawCircle(
                color = if (active) LogoColor else patternSurface,
                radius = nodeRadius.toPx(),
                center = center
            )
            drawCircle(
                color = patternForeground,
                radius = nodeRadius.toPx(),
                center = center,
                style = Stroke(width = if (active) 5f else 3f)
            )
            if (active) {
                drawCircle(
                    color = patternForeground,
                    radius = 6.dp.toPx(),
                    center = center
                )
            }
        }
    }
}


internal enum class Screen(val route: String) {
    PROFILES("profiles"),
    TODAY("today"),
    MEDICINES("medicines"),
    APPOINTMENTS("appointments"),
    MEASUREMENTS("measurements"),
    REPORTS("reports"),
    SETTINGS("settings");

    companion object {
        fun fromRoute(route: String?): Screen = entries.firstOrNull { it.route == route } ?: TODAY
    }
}
internal enum class SettingsPage {
    HOME, NOTIFICATIONS, ALARMS, APPOINTMENTS, BACKUP, SECURITY, PERMISSIONS, ACCESSIBILITY, THEME, DATABASE, HELP, ABOUT
}
internal enum class MeasureTab { BP, GLUCOSE }

internal enum class BottomMenuSize(val storageValue: String) {
    SMALL("small"), MEDIUM("medium"), LARGE("large");
    companion object {
        fun fromStorage(value: String?): BottomMenuSize =
            entries.firstOrNull { it.storageValue == value } ?: MEDIUM
    }
}

data class DailyDose(val medication: Medication, val time: String, val minutes: Int)

internal enum class ProfileBootstrapStatus { CHECKING, NEEDS_FIRST_PROFILE, READY }

@Composable
internal fun FirstRunPatternDialog(
    onDismiss: () -> Unit,
    onPatternConfirmed: (List<Int>) -> Unit
) {
    var firstPattern by remember { mutableStateOf<List<Int>?>(null) }
    var drawnPattern by remember { mutableStateOf(emptyList<Int>()) }
    var pointer by remember { mutableStateOf<Offset?>(null) }
    var message by remember { mutableStateOf("En az dört noktadan oluşan kilit desenini çizin") }

    fun reset() {
        drawnPattern = emptyList()
        pointer = null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Kilit Desenini Belirle",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = LogoColorDark
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(message, textAlign = TextAlign.Center)
                SwipePatternGrid(
                    selected = drawnPattern,
                    pointer = pointer,
                    onPatternChanged = {
                        drawnPattern = it.first
                        pointer = it.second
                    },
                    onCompleted = { pattern ->
                        when {
                            pattern.size < 4 -> {
                                message = "Desen en az dört noktadan oluşmalıdır."
                                reset()
                            }
                            firstPattern == null -> {
                                firstPattern = pattern.toList()
                                message = "Aynı deseni doğrulamak için tekrar çizin"
                                reset()
                            }
                            firstPattern != pattern -> {
                                firstPattern = null
                                message = "Desenler eşleşmedi. Baştan yeniden çizin."
                                reset()
                            }
                            else -> {
                                onPatternConfirmed(pattern.toList())
                            }
                        }
                    }
                )
                if (drawnPattern.isNotEmpty()) {
                    TextButton(onClick = { reset() }) { Text("Temizle") }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("İptal") }
        }
    )
}

@Composable
internal fun FirstRunProfileSetupScreen(
    onCreate: (UserProfile, String, List<Int>, (String?) -> Unit) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var relation by rememberSaveable { mutableStateOf("Kendim") }
    var birthDate by rememberSaveable { mutableStateOf<String?>(null) }
    var bloodGroup by rememberSaveable { mutableStateOf("Bilinmiyor") }
    var note by rememberSaveable { mutableStateOf("") }
    var adminPin by rememberSaveable { mutableStateOf("") }
    var confirmPin by rememberSaveable { mutableStateOf("") }
    var pattern by remember { mutableStateOf<List<Int>?>(null) }
    var showPatternDialog by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var saving by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("first_run_setup"),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.sifahane_logo),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().alpha(0.06f),
                contentScale = ContentScale.Fit
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        "Kullanıcı Ekle",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = LogoColorDark,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            shadow = LogoTextShadow
                        )
                    )
                    Text(
                        "Şifahane ilk kullanımda bilgi girişiyle bir yönetici hesabı oluşturur. Ad, yönetici şifresi ve kilit deseni zorunludur.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Ad *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = logoFieldColors()
                    )
                }
                item {
                    OutlinedTextField(
                        value = surname,
                        onValueChange = { surname = it },
                        label = { Text("Soyad") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = logoFieldColors()
                    )
                }
                item {
                    OutlinedTextField(
                        value = relation,
                        onValueChange = { relation = it },
                        label = { Text("Yakınlık") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = logoFieldColors()
                    )
                }
                item {
                    ThemedDateButton(
                        text = "Doğum Tarihi: ${birthDate ?: "Seçilmedi"}",
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                        onClick = { pickDateString(context) { birthDate = it } }
                    )
                }
                item {
                    var expanded by remember { mutableStateOf(false) }
                    Box(Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        ) { Text("Kan Grubu: $bloodGroup") }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf(
                                "A Rh+", "A Rh-", "B Rh+", "B Rh-", "AB Rh+", "AB Rh-",
                                "0 Rh+", "0 Rh-", "Bilinmiyor"
                            ).forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        bloodGroup = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Profil Notu") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        colors = logoFieldColors()
                    )
                }
                item {
                    SifahaneCard(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Tıbbi güvenlik", fontWeight = FontWeight.Bold)
                            Text(
                                "Şifahane yalnız kayıt ve hatırlatma aracıdır; tanı, tedavi veya doz değişikliği önermez. Acil durumda uygulamaya güvenmeyin, 112'yi arayın. Kaçırılan doz için çift doz almayın; hekim veya eczacınıza danışın.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                item {
                    HorizontalDivider()
                    Text(
                        "Yönetici Güvenliği",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = LogoColorDark
                    )
                }
                item {
                    OutlinedTextField(
                        value = adminPin,
                        onValueChange = { value ->
                            if (value.length <= 12 && value.all(Char::isDigit)) adminPin = value
                        },
                        label = { Text("Yönetici Şifresi *") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = logoFieldColors()
                    )
                }
                item {
                    OutlinedTextField(
                        value = confirmPin,
                        onValueChange = { value ->
                            if (value.length <= 12 && value.all(Char::isDigit)) confirmPin = value
                        },
                        label = { Text("Yönetici Şifresini Tekrar Girin *") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = logoFieldColors()
                    )
                }
                item {
                    OutlinedButton(
                        onClick = { showPatternDialog = true },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp).testTag("first_run_pattern"),
                        border = BorderStroke(1.5.dp, if (pattern == null) LogoColor else LogoColorDark)
                    ) {
                        Icon(
                            if (pattern == null) Icons.Default.Gesture else Icons.Default.CheckCircle,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(if (pattern == null) "Kilit Deseni Belirle *" else "Kilit Deseni Belirlendi")
                    }
                }
                error?.let { message ->
                    item {
                        Text(
                            message,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                item {
                    Button(
                        enabled = !saving,
                        onClick = {
                            error = null
                            val validationError = FirstRunSecurityPolicy.validationError(
                                name = name,
                                pin = adminPin,
                                confirmation = confirmPin,
                                pattern = pattern
                            )
                            if (validationError != null) {
                                error = validationError
                            } else {
                                saving = true
                                onCreate(
                                    UserProfile(
                                        name = name.trim(),
                                        surname = surname.trim(),
                                        relation = relation.trim(),
                                        birthDate = birthDate,
                                        bloodGroup = bloodGroup,
                                        profileNote = note.trim(),
                                        role = UserRoles.ADMIN,
                                        permissionsCsv = UserRoles.ADMIN_PERMISSIONS
                                    ),
                                    adminPin,
                                    requireNotNull(pattern)
                                ) { failure ->
                                    if (failure != null) {
                                        saving = false
                                        error = failure
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp).testTag("first_run_save")
                    ) {
                        Text(if (saving) "Kaydediliyor…" else "Kaydet")
                    }
                }
            }
        }
    }

    if (showPatternDialog) {
        FirstRunPatternDialog(
            onDismiss = { showPatternDialog = false },
            onPatternConfirmed = {
                pattern = it
                showPatternDialog = false
            }
        )
    }
}
