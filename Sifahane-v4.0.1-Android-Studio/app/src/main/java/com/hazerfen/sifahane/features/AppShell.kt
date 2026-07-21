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
import com.hazerfen.sifahane.viewmodel.SifahaneViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SifahaneRoot() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val systemAnimationsEnabled = remember { android.animation.ValueAnimator.areAnimatorsEnabled() }
    val db = remember { AppDatabase.get(context) }
    val rootViewModel: SifahaneViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val menuPreferences = remember {
        context.getSharedPreferences("sifahane_bottom_menu", Context.MODE_PRIVATE)
    }
    val defaultMenuOrder = remember {
        listOf(Screen.PROFILES, Screen.TODAY, Screen.MEDICINES, Screen.APPOINTMENTS,
            Screen.MEASUREMENTS, Screen.REPORTS, Screen.SETTINGS)
    }
    fun loadStoredMenuOrder(): List<Screen> {
        val stored = menuPreferences.getString("menu_order", null)
            ?.split(",")
            ?.mapNotNull { runCatching { Screen.valueOf(it) }.getOrNull() }
            .orEmpty()
        return (stored + defaultMenuOrder).distinct().filter { it in defaultMenuOrder }
    }
    var bottomMenuOrder by remember { mutableStateOf(loadStoredMenuOrder()) }
    var bottomMenuSize by remember {
        mutableStateOf(BottomMenuSize.fromStorage(menuPreferences.getString("menu_size", "medium")))
    }
    var bottomMenuEditing by remember { mutableStateOf(false) }
    var bottomMenuPopupVisible by remember { mutableStateOf(false) }

    fun persistBottomMenuOrder(order: List<Screen>) {
        bottomMenuOrder = order
        menuPreferences.edit().putString("menu_order", order.joinToString(",") { it.name }).apply()
    }
    fun changeBottomMenuSize(size: BottomMenuSize) {
        bottomMenuSize = size
        menuPreferences.edit().putString("menu_size", size.storageValue).apply()
    }

    val profiles by rootViewModel.profiles.collectAsStateWithLifecycle(initialValue = emptyList())

    var activeProfileId by remember { mutableLongStateOf(1L) }
    var unlockedProfileId by remember { mutableStateOf<Long?>(null) }
    var pendingPatternProfile by remember { mutableStateOf<UserProfile?>(null) }
    var forcePatternCreate by remember { mutableStateOf(false) }
    var screen by remember { mutableStateOf(Screen.TODAY) }
    val currentRoute = navBackStackEntry?.destination?.route
    LaunchedEffect(screen, currentRoute) {
        if (currentRoute != screen.route) {
            navController.navigate(screen.route) {
                launchSingleTop = true
                restoreState = true
            }
        }
    }
    DisposableEffect(navController) {
        val listener = androidx.navigation.NavController.OnDestinationChangedListener { _, destination, _ ->
            screen = Screen.fromRoute(destination.route)
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose { navController.removeOnDestinationChangedListener(listener) }
    }
    var retainedSettingsPage by remember { mutableStateOf(SettingsPage.HOME) }
    var initialScreenSet by remember { mutableStateOf(false) }
    var backgroundedAt by remember { mutableLongStateOf(0L) }
    var adminPinPurpose by remember { mutableStateOf<AdminPinPurpose?>(null) }
    var adminTargetProfile by remember { mutableStateOf<UserProfile?>(null) }
    var adminSessionUnlocked by remember { mutableStateOf(false) }
    var patternChangeProfileId by remember { mutableStateOf<Long?>(null) }
    var adminDeleteProfile by remember { mutableStateOf<UserProfile?>(null) }
    var idleHeartVisible by remember { mutableStateOf(false) }
    var idleHeartKey by remember { mutableLongStateOf(0L) }
    var idleTimerJob by remember { mutableStateOf<Job?>(null) }
    var alarmRefreshResult by remember {
        mutableStateOf<AlarmRefreshResult?>(null)
    }
    var showAlarmStatus by remember { mutableStateOf(false) }
    var alarmRefreshBusy by remember { mutableStateOf(false) }
    var profileBootstrapStatus by remember {
        mutableStateOf(ProfileBootstrapStatus.CHECKING)
    }

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    fun restartIdleTimer() {
        idleHeartVisible = false
        idleTimerJob?.cancel()
        val timeout = AppPreferences.lockTimeoutMillis(context)
        if (timeout <= 0L) return
        idleTimerJob = scope.launch {
            delay(timeout)
            if (pendingPatternProfile == null && adminPinPurpose == null) {
                profiles.firstOrNull { it.id == activeProfileId }?.let { profile ->
                    if (PatternStore.hasPattern(context, profile.id)) {
                        unlockedProfileId = null
                        pendingPatternProfile = profile
                        forcePatternCreate = false
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        restartIdleTimer()
        val profileCount = withContext(Dispatchers.IO) {
            val existingProfiles = db.profileDao().allProfiles()
            val onlyProfile = existingProfiles.singleOrNull()
            val replaceLegacyPlaceholder = onlyProfile != null &&
                LegacyPlaceholderPolicy.canReplaceWithFirstRunSetup(
                    allProfiles = existingProfiles,
                    relatedRecordCount = db.profileDao().relatedRecordCount(onlyProfile.id),
                    hasPattern = PatternStore.hasPattern(context, onlyProfile.id)
                )
            if (replaceLegacyPlaceholder) {
                db.profileDao().deleteProfileCascade(onlyProfile.id)
                PatternStore.clear(context, onlyProfile.id)
                BiometricPreferences.clear(context, onlyProfile.id)
                0
            } else {
                existingProfiles.size
            }
        }
        profileBootstrapStatus = if (profileCount == 0) {
            ProfileBootstrapStatus.NEEDS_FIRST_PROFILE
        } else {
            ProfileBootstrapStatus.READY
        }
        if (
            profileCount > 0 &&
            android.os.Build.VERSION.SDK_INT >= 33 &&
            androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(
                android.Manifest.permission.POST_NOTIFICATIONS
            )
        }
        alarmRefreshResult = withContext(Dispatchers.IO) {
            AlarmRescheduler.refreshAll(context)
        }
        AdminPinStore.prepareForVersion(context, BuildConfig.VERSION_CODE)
        if (profileCount > 0) {
            // Eski sürümlerde profil oluşturulmadan önce gösterilen küresel şifre
            // penceresi kaldırıldı. Mevcut kullanıcıların profil şifreleri doğrulama
            // için kabul edilir; temiz kurulumda şifre ilk profil formunda oluşturulur.
            AppPreferences.markVersionVerified(context, BuildConfig.VERSION_CODE)
        }
    }

    when (profileBootstrapStatus) {
        ProfileBootstrapStatus.CHECKING -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }
        ProfileBootstrapStatus.NEEDS_FIRST_PROFILE -> {
            FirstRunProfileSetupScreen { profile, pin, pattern, reportFailure ->
                scope.launch {
                    var savedId: Long? = null
                    try {
                        savedId = withContext(Dispatchers.IO) {
                            db.withTransaction {
                                val id = db.profileDao().insert(profile)
                                db.profileDao().updateAdminPinHash(
                                    id,
                                    AdminCredentialHasher.hash(id, pin)
                                )
                                id
                            }
                        }
                        AdminPinStore.saveNewPin(context, pin)
                        PatternStore.save(context, requireNotNull(savedId), pattern)
                        activeProfileId = requireNotNull(savedId)
                        unlockedProfileId = savedId
                        pendingPatternProfile = null
                        forcePatternCreate = false
                        adminSessionUnlocked = true
                        initialScreenSet = true
                        screen = Screen.TODAY
                        AppPreferences.markVersionVerified(context, BuildConfig.VERSION_CODE)
                        profileBootstrapStatus = ProfileBootstrapStatus.READY
                        if (
                            android.os.Build.VERSION.SDK_INT >= 33 &&
                            androidx.core.content.ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.POST_NOTIFICATIONS
                            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                        ) {
                            notificationPermissionLauncher.launch(
                                android.Manifest.permission.POST_NOTIFICATIONS
                            )
                        }
                    } catch (_: Exception) {
                        savedId?.let { failedId ->
                            runCatching { PatternStore.clear(context, failedId) }
                            withContext(Dispatchers.IO) {
                                runCatching { db.profileDao().deleteProfileCascade(failedId) }
                            }
                        }
                        runCatching { AdminPinStore.clearForIncompleteFirstRun(context) }
                        reportFailure("İlk kullanıcı kaydedilemedi. Yarım kayıt temizlendi; yeniden deneyin.")
                    }
                }
            }
            return
        }
        ProfileBootstrapStatus.READY -> Unit
    }

    LaunchedEffect(screen) {
        restartIdleTimer()
    }

    val activeProfile = profiles.firstOrNull { it.id == activeProfileId }
    val activeIsAdmin = UserRoles.isAdmin(activeProfile)

    fun openMainScreen(target: Screen) {
        if (target == screen) return
        if (target == Screen.PROFILES) {
            screen = target
            return
        }
        val active = profiles.firstOrNull { it.id == activeProfileId }
        if (active != null && unlockedProfileId == active.id) {
            screen = target
        } else if (active != null) {
            if (PatternStore.hasPattern(context, active.id)) {
                pendingPatternProfile = active
                forcePatternCreate = false
            } else {
                adminTargetProfile = active
                adminPinPurpose = AdminPinPurpose.UNLOCK_PROFILE
            }
        }
    }

    androidx.activity.compose.BackHandler(
        enabled = screen != Screen.PROFILES && pendingPatternProfile == null && adminPinPurpose == null
    ) {
        if (!navController.popBackStack()) {
            screen = Screen.PROFILES
            unlockedProfileId = null
        }
    }

    LaunchedEffect(profiles) {
        if (profiles.isNotEmpty() && profiles.none { it.id == activeProfileId }) {
            activeProfileId = profiles.first().id
            unlockedProfileId = null
        }
        if (!initialScreenSet && profiles.isNotEmpty()) {
            if (profiles.size > 1) {
                screen = Screen.PROFILES
            } else {
                activeProfileId = profiles.first().id
                if (PatternStore.hasPattern(context, profiles.first().id)) {
                    pendingPatternProfile = profiles.first()
                    forcePatternCreate = false
                } else {
                    adminTargetProfile = profiles.first()
                    adminPinPurpose = AdminPinPurpose.UNLOCK_PROFILE
                }
                // Sağlık ekranı yalnız doğrulama tamamlandıktan sonra açılır.
                screen = Screen.PROFILES
            }
            initialScreenSet = true
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> backgroundedAt = System.currentTimeMillis()
                Lifecycle.Event.ON_START -> {
                    if (
                        backgroundedAt > 0L &&
                        System.currentTimeMillis() - backgroundedAt > 120_000L
                    ) {
                        unlockedProfileId = null
                        profiles.firstOrNull { it.id == activeProfileId }?.let {
                            if (PatternStore.hasPattern(context, it.id)) {
                                pendingPatternProfile = it
                                forcePatternCreate = false
                            } else {
                                adminTargetProfile = it
                                adminPinPurpose = AdminPinPurpose.UNLOCK_PROFILE
                            }
                        }
                    }
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(activeProfileId) { rootViewModel.selectProfile(activeProfileId) }

    val meds by rootViewModel.medications.collectAsStateWithLifecycle(initialValue = emptyList())
    val allMedications by rootViewModel.allMedications.collectAsStateWithLifecycle(initialValue = emptyList())
    val reportGroups by rootViewModel.reportGroups.collectAsStateWithLifecycle(initialValue = emptyList())
    val archive by rootViewModel.archivedMedications.collectAsStateWithLifecycle(initialValue = emptyList())
    val logs by rootViewModel.doseLogs.collectAsStateWithLifecycle(initialValue = emptyList())
    val appointments by rootViewModel.appointments.collectAsStateWithLifecycle(initialValue = emptyList())
    val bp by rootViewModel.bloodPressure.collectAsStateWithLifecycle(initialValue = emptyList())
    val glucose by rootViewModel.glucose.collectAsStateWithLifecycle(initialValue = emptyList())

    var editMedication by remember { mutableStateOf<Medication?>(null) }
    var newMedication by remember { mutableStateOf(false) }
    var editProfile by remember { mutableStateOf<UserProfile?>(null) }
    var newProfile by remember { mutableStateOf(false) }
    var showAddProfileChoice by remember { mutableStateOf(false) }
    var addBp by remember { mutableStateOf(false) }
    var addGlucose by remember { mutableStateOf(false) }
    var editBp by remember { mutableStateOf<BloodPressure?>(null) }
    var editGlucose by remember { mutableStateOf<BloodGlucose?>(null) }
    var editReportGroup by remember { mutableStateOf<ReportGroup?>(null) }
    var newReportGroup by remember { mutableStateOf(false) }
    var medicinesTab by remember { mutableIntStateOf(0) }

    var measureTab by remember { mutableStateOf(MeasureTab.BP) }
    var exportStatus by remember { mutableStateOf("") }
    var pendingExportProfileId by remember { mutableStateOf<Long?>(null) }
    var pendingExportFile by remember { mutableStateOf<File?>(null) }
    var showSecondCopyQuestion by remember { mutableStateOf(false) }
    var treeAction by remember { mutableStateOf("export") }
    var showImportSourceDialog by remember { mutableStateOf(false) }
    var folderBackups by remember { mutableStateOf<List<BackupDocument>>(emptyList()) }
    var showFolderBackups by remember { mutableStateOf(false) }
    var importPreview by remember { mutableStateOf<BackupPreview?>(null) }
    var showInitialImportChoice by remember { mutableStateOf(false) }
    var initialImportMode by remember { mutableStateOf(false) }
    var addProfileImportMode by remember { mutableStateOf(false) }
    var showImportRoleChoice by remember { mutableStateOf(false) }
    var importNewProfileAsAdmin by remember { mutableStateOf(false) }
    var passwordExportProfileId by remember { mutableStateOf<Long?>(null) }
    var exportPassword by remember { mutableStateOf("") }
    var exportPasswordConfirm by remember { mutableStateOf("") }
    var pendingExportPassword by remember { mutableStateOf<String?>(null) }
    var pendingEncryptedImportUri by remember { mutableStateOf<Uri?>(null) }
    var importPasswordEntry by remember { mutableStateOf("") }
    var importBackupPassword by remember { mutableStateOf<String?>(null) }

    val saveSecondCopyLauncher = rememberLauncherForActivityResult(CreateDocument("application/octet-stream")) { uri ->
        val file = pendingExportFile
        if (uri != null && file != null) {
            scope.launch {
                exportStatus = runCatching {
                    withContext(Dispatchers.IO) { SifahaneBackupManager.copyBackupToUri(context, file, uri) }
                    "Yedek iki konuma başarıyla kaydedildi."
                }.getOrElse { "İkinci kopya kaydedilemedi: ${it.message}" }
                pendingExportFile = null
            }
        } else if (file != null) {
            exportStatus = "Otomatik yedek kaydedildi; ikinci kopya oluşturulmadı."
            pendingExportFile = null
        }
    }

    if (showSecondCopyQuestion) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    "İkinci yedek kopyası",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "İlk kopya Şifahane Yedek klasörüne parola korumalı AES-GCM .sifbak biçiminde kaydedildi. Dosya adı ve hedef uygulama paylaşım ekranında görünür; içerik doğru parola olmadan açılamaz. İkinci kopyayı paylaşabilir veya başka bir konuma kaydedebilirsiniz."
                )
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            showSecondCopyQuestion = false
                            pendingExportFile?.let { file ->
                                val uri = FileProvider.getUriForFile(
                                    context, "${context.packageName}.fileprovider", file
                                )
                                context.startActivity(Intent.createChooser(
                                    Intent(Intent.ACTION_SEND).apply {
                                        type = "application/octet-stream"
                                        putExtra(Intent.EXTRA_STREAM, uri)
                                        putExtra(Intent.EXTRA_SUBJECT, "Şifahane kişi verileri yedeği")
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        clipData = android.content.ClipData.newUri(
                                            context.contentResolver, file.name, uri
                                        )
                                    },
                                    "Yedek kopyasını paylaş"
                                ))
                                exportStatus = "İkinci yedek kopyası paylaşım ekranına gönderildi."
                                pendingExportFile = null
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
                    ) { Text("UYGULAMALARLA PAYLAŞ", textAlign = TextAlign.Center) }
                    OutlinedButton(
                        onClick = {
                            showSecondCopyQuestion = false
                            pendingExportFile?.let { saveSecondCopyLauncher.launch(it.name) }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
                    ) { Text("DOSYA KONUMU SEÇ", textAlign = TextAlign.Center) }
                    TextButton(
                        onClick = {
                            showSecondCopyQuestion = false
                            pendingExportFile = null
                            exportStatus = "Yedek Şifahane Yedek klasörüne kaydedildi."
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
                    ) { Text("HAYIR", textAlign = TextAlign.Center) }
                }
            },
            dismissButton = {}
        )
    }

    fun openBackupPreview(uri: Uri, password: String?) {
        scope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    SifahaneBackupManager.preview(context, uri, password?.toCharArray())
                }
            }
                .onSuccess {
                    importBackupPassword = password
                    importPreview = it
                }
                .onFailure {
                    importBackupPassword = null
                    exportStatus = it.message ?: "Yedek okunamadı."
                }
        }
    }

    fun previewBackup(uri: Uri) {
        val encrypted = runCatching { SifahaneBackupManager.isEncrypted(context, uri) }
            .getOrDefault(false)
        if (encrypted) {
            pendingEncryptedImportUri = uri
            importPasswordEntry = ""
        } else {
            importBackupPassword = null
            openBackupPreview(uri, null)
        }
    }

    pendingEncryptedImportUri?.let { encryptedUri ->
        AlertDialog(
            onDismissRequest = {
                pendingEncryptedImportUri = null
                importPasswordEntry = ""
            },
            title = { Text("Şifreli yedek parolası") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Bu yedek AES-GCM ile şifrelenmiştir. Oluştururken belirlediğiniz en az 8 karakterli parolayı girin.")
                    OutlinedTextField(
                        value = importPasswordEntry,
                        onValueChange = { if (it.length <= 128) importPasswordEntry = it },
                        label = { Text("Yedek parolası") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = importPasswordEntry.length >= 8,
                    onClick = {
                        val password = importPasswordEntry
                        pendingEncryptedImportUri = null
                        importPasswordEntry = ""
                        openBackupPreview(encryptedUri, password)
                    }
                ) { Text("YEDEĞİ DOĞRULA") }
            },
            dismissButton = {
                TextButton(onClick = {
                    pendingEncryptedImportUri = null
                    importPasswordEntry = ""
                }) { Text("İptal") }
            }
        )
    }

    val importLauncher = rememberLauncherForActivityResult(OpenDocument()) { uri ->
        if (uri != null) previewBackup(uri)
        else if (initialImportMode) {
            initialImportMode = false
            showInitialImportChoice = true
        }
    }

    if (showInitialImportChoice) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Kişi verilerini içeri aktar") },
            text = { Text("Daha önce dışarı aktardığınız kişi verilerini şimdi içeri aktarmak ister misiniz?") },
            confirmButton = {
                Button(onClick = {
                    showInitialImportChoice = false
                    initialImportMode = true
                    showImportSourceDialog = true
                }) { Text("İÇERİ AKTAR") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showInitialImportChoice = false
                    initialImportMode = false
                    profiles.firstOrNull()?.let {
                        activeProfileId = it.id
                        pendingPatternProfile = it
                        forcePatternCreate = true
                    }
                    AppPreferences.markInstallSetupDone(context)
                }) { Text("ŞİMDİLİK HAYIR") }
            }
        )
    }

    val treeLauncher = rememberLauncherForActivityResult(OpenDocumentTree()) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            SifahaneBackupManager.saveTreeUri(context, uri)
            if (treeAction == "export") {
                pendingExportProfileId?.let { profileId ->
                    scope.launch {
                        runCatching {
                            val file = withContext(Dispatchers.IO) {
                                val password = pendingExportPassword
                                    ?: error("Yedek parolası bulunamadı.")
                                SifahaneBackupManager.createBackup(context, db, profileId, password.toCharArray()).also {
                                    SifahaneBackupManager.copyBackupToTree(context, uri, it)
                                }
                            }
                            pendingExportFile = file
                            exportStatus = "Otomatik kopya Şifahane Yedek klasörüne kaydedildi."
                            showSecondCopyQuestion = true
                        }.onFailure { exportStatus = "Yedek oluşturulamadı: ${it.message}" }
                        pendingExportProfileId = null
                        pendingExportPassword = null
                    }
                }
            } else {
                scope.launch {
                    folderBackups = withContext(Dispatchers.IO) { SifahaneBackupManager.listBackups(context, uri) }
                    showFolderBackups = true
                }
            }
        } else if (initialImportMode) {
            initialImportMode = false
            showInitialImportChoice = true
        }
    }

    fun performExportProfile(profileId: Long, password: String) {
        val tree = SifahaneBackupManager.savedTreeUri(context)
        if (tree == null) {
            pendingExportProfileId = profileId
            pendingExportPassword = password
            treeAction = "export"
            exportStatus = "Şifahane Yedek klasörünü seçiniz veya oluşturunuz."
            treeLauncher.launch(null)
        } else {
            scope.launch {
                runCatching {
                    val file = withContext(Dispatchers.IO) {
                        SifahaneBackupManager.createBackup(context, db, profileId, password.toCharArray()).also {
                            SifahaneBackupManager.copyBackupToTree(context, tree, it)
                        }
                    }
                    pendingExportFile = file
                    exportStatus = "Otomatik kopya Şifahane Yedek klasörüne kaydedildi."
                    showSecondCopyQuestion = true
                }.onFailure {
                    exportStatus = "Yedek klasörüne erişilemedi. Klasörü yeniden seçiniz."
                    pendingExportProfileId = profileId
                    pendingExportPassword = password
                    treeAction = "export"
                    treeLauncher.launch(null)
                }
            }
        }
    }

    fun exportProfile(profileId: Long) {
        passwordExportProfileId = profileId
        exportPassword = ""
        exportPasswordConfirm = ""
    }

    passwordExportProfileId?.let { profileId ->
        AlertDialog(
            onDismissRequest = {
                passwordExportProfileId = null
                exportPassword = ""
                exportPasswordConfirm = ""
            },
            title = { Text("Şifreli yedek oluştur") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Yedek kişisel sağlık verileri içerir ve AES-GCM ile şifrelenir. Parola uygulamada saklanmaz; kaybedilirse yedek açılamaz.")
                    OutlinedTextField(
                        value = exportPassword,
                        onValueChange = { if (it.length <= 128) exportPassword = it },
                        label = { Text("Yedek parolası (en az 8 karakter)") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = exportPasswordConfirm,
                        onValueChange = { if (it.length <= 128) exportPasswordConfirm = it },
                        label = { Text("Yedek parolasını tekrar girin") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (exportPasswordConfirm.isNotEmpty() && exportPassword != exportPasswordConfirm) {
                        Text("Parolalar eşleşmiyor.", color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = exportPassword.length >= 8 && exportPassword == exportPasswordConfirm,
                    onClick = {
                        val password = exportPassword
                        passwordExportProfileId = null
                        exportPassword = ""
                        exportPasswordConfirm = ""
                        performExportProfile(profileId, password)
                    }
                ) { Text("ŞİFRELİ YEDEK OLUŞTUR") }
            },
            dismissButton = {
                TextButton(onClick = {
                    passwordExportProfileId = null
                    exportPassword = ""
                    exportPasswordConfirm = ""
                }) { Text("İptal") }
            }
        )
    }

    if (showImportSourceDialog) {
        AlertDialog(
            onDismissRequest = {
                showImportSourceDialog = false
                if (initialImportMode) {
                    initialImportMode = false
                    showInitialImportChoice = true
                }
            },
            title = { Text("Yedek kaynağını seçin") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Şifahane Yedek klasöründeki parola korumalı .sifbak dosyalarından seçim yapabilir veya eski uyumlu ZIP yedeğini açabilirsiniz.")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PlainImportOption(
                            text = "Başka Konum",
                            onClick = {
                                showImportSourceDialog = false
                                importLauncher.launch(arrayOf("application/zip", "application/octet-stream"))
                            },
                            modifier = Modifier.weight(1f)
                        )
                        PlainImportOption(
                            text = "Şifahane Yedek",
                            onClick = {
                                showImportSourceDialog = false
                                val tree = SifahaneBackupManager.savedTreeUri(context)
                                if (tree == null) { treeAction = "import"; treeLauncher.launch(null) }
                                else scope.launch {
                                    folderBackups = withContext(Dispatchers.IO) { SifahaneBackupManager.listBackups(context, tree) }
                                    showFolderBackups = true
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PlainImportOption(
                            text = "İptal",
                            onClick = {
                                showImportSourceDialog = false
                                initialImportMode = false
                                addProfileImportMode = false
                            },
                            modifier = Modifier.weight(1f)
                        )
                        PlainImportOption(
                            text = "Geri",
                            onClick = {
                                showImportSourceDialog = false
                                when {
                                    addProfileImportMode -> showImportRoleChoice = true
                                    initialImportMode -> showInitialImportChoice = true
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }

    if (showFolderBackups) {
        AlertDialog(
            onDismissRequest = { showFolderBackups = false },
            title = { Text("Şifahane yedekleri") },
            text = {
                if (folderBackups.isEmpty()) Text("Seçili klasörde .sifbak veya uyumlu eski ZIP yedeği bulunamadı.")
                else LazyColumn(modifier = Modifier.heightIn(max = 360.dp)) {
                    items(folderBackups) { item ->
                        ListItem(
                            headlineContent = { Text(item.name) },
                            supportingContent = { Text("${item.size / 1024} KB • ${formatDateTime(item.modifiedAt)}") },
                            modifier = Modifier.clickable { showFolderBackups = false; previewBackup(item.uri) }
                        )
                        HorizontalDivider()
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showFolderBackups = false }) { Text("Kapat") } }
        )
    }

    importPreview?.let { preview ->
        AlertDialog(
            onDismissRequest = { importPreview = null; importBackupPassword = null },
            title = { Text("Şifahane yedeği doğrulandı") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Kullanıcı: ${preview.profileName}", fontWeight = FontWeight.Bold)
                    preview.birthDate?.let { Text("Doğum tarihi: $it") }
                    Text("Kan grubu: ${preview.bloodGroup}")
                    Text("Yedek tarihi: ${preview.createdAt}")
                    Text("${preview.medicationCount} ilaç • ${preview.doseLogCount} doz kaydı")
                    Text("${preview.bloodPressureCount} tansiyon • ${preview.glucoseCount} şeker kaydı")
                    Text("${preview.reportGroupCount} rapor grubu • ${preview.appointmentCount} randevu")
                    Text("${preview.photoCount} fotoğraf")
                    Spacer(Modifier.height(8.dp))
                    Text("Bu yedek kişisel sağlık verileri içerebilir.", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    importPreview = null
                    val passwordForImport = importBackupPassword
                    importBackupPassword = null
                    scope.launch {
                        exportStatus = runCatching {
                            val importResult = withContext(Dispatchers.IO) {
                                val imported = SifahaneBackupManager.importBackup(
                                    context, db, preview.uri, null,
                                    passwordForImport?.toCharArray()
                                )
                                if (initialImportMode) {
                                    db.profileDao().updateRole(imported.profileId, UserRoles.ADMIN, UserRoles.ADMIN_PERMISSIONS)
                                    db.profileDao().allProfiles().filter { it.id != imported.profileId }.forEach {
                                        db.profileDao().deleteProfileCascade(it.id)
                                        PatternStore.clear(context, it.id)
                                        BiometricPreferences.clear(context, it.id)
                                    }
                                } else if (addProfileImportMode) {
                                    val role = if (importNewProfileAsAdmin) UserRoles.ADMIN else UserRoles.STANDARD
                                    val permissions = if (importNewProfileAsAdmin) UserRoles.ADMIN_PERMISSIONS else UserRoles.STANDARD_PERMISSIONS
                                    db.profileDao().updateRole(imported.profileId, role, permissions)
                                }
                                imported
                            }
                            val newId = importResult.profileId
                            activeProfileId = newId
                            if (initialImportMode) {
                                db.profileDao().byId(newId)?.let {
                                    pendingPatternProfile = it
                                    forcePatternCreate = true
                                }
                                initialImportMode = false
                                AppPreferences.markInstallSetupDone(context)
                            } else if (addProfileImportMode) {
                                db.profileDao().byId(newId)?.let {
                                    pendingPatternProfile = it
                                    forcePatternCreate = true
                                }
                                addProfileImportMode = false
                            }
                            "Kişi verileri yeni kullanıcı olarak içeri aktarıldı. ${importResult.summary()} Alarmlar yenilendi."
                        }.getOrElse { it.message ?: "İçe aktarma başarısız." }
                    }
                }) { Text("Yeni kullanıcı") }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = {
                        importPreview = null
                        val passwordForImport = importBackupPassword
                        importBackupPassword = null
                        if (activeProfileId <= 0L) exportStatus = "Birleştirmek için önce mevcut bir kullanıcı seçiniz."
                        else scope.launch {
                            exportStatus = runCatching {
                                val result = withContext(Dispatchers.IO) {
                                    SifahaneBackupManager.importBackup(
                                        context, db, preview.uri, activeProfileId,
                                        passwordForImport?.toCharArray()
                                    )
                                }
                                "Yedek aktif kullanıcıyla birleştirildi. ${result.summary()} Alarmlar yenilendi."
                            }.getOrElse { it.message ?: "İçe aktarma başarısız." }
                        }
                    }) { Text("Aktif kişiyle birleştir") }
                    TextButton(onClick = { importPreview = null; importBackupPassword = null }) { Text("İptal") }
                }
            }
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                while (true) {
                    awaitPointerEventScope {
                        awaitPointerEvent()
                    }
                    restartIdleTimer()
                }
            }
    ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val p = profiles.firstOrNull { it.id == activeProfileId }
                    SifahanePageTitle(
                        activeUser = listOfNotNull(
                            p?.name,
                            p?.surname?.takeIf { it.isNotBlank() }
                        ).joinToString(" "),
                        pageKey = screen
                    )
                },
                actions = {
                    when (screen) {
                        Screen.PROFILES -> IconButton({ showAddProfileChoice = true }) {
                            OutlinedLogoIcon(Icons.Default.PersonAdd, "Kişi ekle", size = 28.dp)
                        }
                        else -> Unit
                    }
                }
            )
        },
        bottomBar = {
            val scrollState = rememberScrollState()
            val density = LocalDensity.current
            val bottomMenuDragScope = rememberCoroutineScope()
            val itemWidth = when (bottomMenuSize) {
                BottomMenuSize.SMALL -> 62.dp
                BottomMenuSize.MEDIUM -> 74.dp
                BottomMenuSize.LARGE -> 88.dp
            }
            val iconSize = when (bottomMenuSize) {
                BottomMenuSize.SMALL -> 21.dp
                BottomMenuSize.MEDIUM -> 25.dp
                BottomMenuSize.LARGE -> 29.dp
            }
            val menuTextSize = when (bottomMenuSize) {
                BottomMenuSize.SMALL -> 9.sp
                BottomMenuSize.MEDIUM -> 10.sp
                BottomMenuSize.LARGE -> 12.sp
            }
            val contentHeight = when (bottomMenuSize) {
                BottomMenuSize.SMALL -> 56.dp
                BottomMenuSize.MEDIUM -> 64.dp
                BottomMenuSize.LARGE -> 74.dp
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LogoColorSoft.copy(alpha = 0.50f))
                    .navigationBarsPadding()
            ) {
                PartnerLogoBanner()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    LogoColorSoft.copy(alpha = 0.52f),
                                    LogoColorSoft.copy(alpha = 0.48f)
                                )
                            )
                        )
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Transparent,
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp
                    ) {
                        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                            val menuButtonWidth = when (bottomMenuSize) {
                                BottomMenuSize.SMALL -> 50.dp
                                BottomMenuSize.MEDIUM -> 54.dp
                                BottomMenuSize.LARGE -> 60.dp
                            }
                            val dividerWidth = 0.5.dp
                            val scrollViewportWidth = (maxWidth - menuButtonWidth - dividerWidth)
                                .coerceAtLeast(itemWidth)
                            val visibleItemCount = max(
                                1,
                                kotlin.math.floor(
                                    with(density) { scrollViewportWidth.toPx() / itemWidth.toPx() }
                                ).toInt()
                            )
                            val fittedItemWidth = scrollViewportWidth / visibleItemCount
                            val fittedItemWidthPx = with(density) { fittedItemWidth.toPx() }

                            LaunchedEffect(screen, bottomMenuOrder, bottomMenuSize, fittedItemWidthPx, scrollState.maxValue, bottomMenuEditing) {
                                // Düzenleme sırasında her sıra değişiminde seçili ekrana geri kaymak,
                                // parmakla sürüklemeyi kesiyordu. Otomatik görünür kılma yalnızca
                                // normal gezinme modunda çalışır.
                                if (!bottomMenuEditing) {
                                    val index = bottomMenuOrder.indexOf(screen).coerceAtLeast(0)
                                    val target = (fittedItemWidthPx * index).toInt()
                                    scrollState.animateScrollTo(target.coerceIn(0, scrollState.maxValue))
                                }
                            }

                            LaunchedEffect(scrollState, fittedItemWidthPx) {
                                snapshotFlow { scrollState.isScrollInProgress }.collect { isScrolling ->
                                    if (!isScrolling && fittedItemWidthPx > 0f) {
                                        val snappedIndex = (scrollState.value / fittedItemWidthPx).roundToInt()
                                        val snappedOffset = (snappedIndex * fittedItemWidthPx)
                                            .roundToInt()
                                            .coerceIn(0, scrollState.maxValue)
                                        if (kotlin.math.abs(scrollState.value - snappedOffset) > 1) {
                                            scrollState.animateScrollTo(snappedOffset)
                                        }
                                    }
                                }
                            }

                            Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(contentHeight),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .horizontalScroll(scrollState),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                bottomMenuOrder.forEach { menuScreen ->
                                    // Sıra değiştiğinde remember durumlarının listedeki konumla değil,
                                    // gerçek menü öğesiyle birlikte taşınmasını sağlar. Bu olmadan
                                    // sürükleme durumu ilk komşu takasından sonra başka öğeye geçiyordu.
                                    key(menuScreen) {
                                    val icon = when (menuScreen) {
                                        Screen.PROFILES -> Icons.Default.People
                                        Screen.TODAY -> Icons.Default.Today
                                        Screen.MEDICINES -> Icons.Default.NightlightRound
                                        Screen.APPOINTMENTS -> Icons.Default.Event
                                        Screen.MEASUREMENTS -> Icons.Default.MonitorHeart
                                        Screen.REPORTS -> Icons.Default.Assessment
                                        Screen.SETTINGS -> Icons.Default.Settings
                                    }
                                    val label = when (menuScreen) {
                                        Screen.PROFILES -> "Kişiler"
                                        Screen.TODAY -> "Bugün"
                                        Screen.MEDICINES -> "İlaçlar"
                                        Screen.APPOINTMENTS -> "Randevular"
                                        Screen.MEASUREMENTS -> "Ölçümler"
                                        Screen.REPORTS -> "Raporlar"
                                        Screen.SETTINGS -> "Ayarlar"
                                    }
                                    var dragTranslationX by remember(menuScreen, bottomMenuEditing) {
                                        mutableFloatStateOf(0f)
                                    }
                                    var isBeingDragged by remember(menuScreen, bottomMenuEditing) {
                                        mutableStateOf(false)
                                    }
                                    val editJiggle = if (bottomMenuEditing && systemAnimationsEnabled) {
                                        val editJiggleTransition = rememberInfiniteTransition(
                                            label = "bottomMenuEditJiggle"
                                        )
                                        val value by editJiggleTransition.animateFloat(
                                            initialValue = -0.45f,
                                            targetValue = 0.45f,
                                            animationSpec = infiniteRepeatable(
                                                animation = tween(
                                                    durationMillis = 1200,
                                                    easing = LinearEasing
                                                ),
                                                repeatMode = RepeatMode.Reverse
                                            ),
                                            label = "bottomMenuEditJiggleAngle"
                                        )
                                        value
                                    } else {
                                        0f
                                    }
                                    val selected = screen == menuScreen
                                    val selectedScale by animateFloatAsState(
                                        targetValue = when {
                                            isBeingDragged -> 1.03f
                                            selected -> 1.03f
                                            else -> 1.0f
                                        },
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMediumLow
                                        ),
                                        label = "bottomMenuScale"
                                    )
                                    val selectedElevation by animateFloatAsState(
                                        targetValue = when {
                                            isBeingDragged -> 5f
                                            selected -> 4f
                                            else -> 0f
                                        },
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioNoBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        ),
                                        label = "bottomMenuElevation"
                                    )
                                    val foregroundColor by animateColorAsState(
                                        targetValue = Color.Black.copy(
                                            alpha = if (selected) 0.70f else 0.50f
                                        ),
                                        animationSpec = tween(durationMillis = 140),
                                        label = "bottomMenuForeground"
                                    )
                                    val selectionColor by animateColorAsState(
                                        targetValue = if (selected) {
                                            LogoColor.copy(alpha = 0.24f)
                                        } else {
                                            Color.Transparent
                                        },
                                        animationSpec = tween(durationMillis = 140),
                                        label = "bottomMenuSelection"
                                    )
                                    val reorderModifier = if (bottomMenuEditing) {
                                        Modifier.pointerInput(
                                            menuScreen,
                                            bottomMenuEditing,
                                            fittedItemWidthPx,
                                            scrollViewportWidth
                                        ) {
                                            detectDragGesturesAfterLongPress(
                                                onDragStart = {
                                                    dragTranslationX = 0f
                                                    isBeingDragged = true
                                                },
                                                onDragEnd = {
                                                    dragTranslationX = 0f
                                                    isBeingDragged = false
                                                },
                                                onDragCancel = {
                                                    dragTranslationX = 0f
                                                    isBeingDragged = false
                                                }
                                            ) { change, dragAmount ->
                                                change.consume()
                                                dragTranslationX += dragAmount.x

                                                var currentIndex = bottomMenuOrder.indexOf(menuScreen)
                                                if (currentIndex < 0 || fittedItemWidthPx <= 0f) return@detectDragGesturesAfterLongPress

                                                // Parmağın geçtiği her tam yuvada öğeyi taşı; kalan ofset,
                                                // sürüklenen simgenin parmak altında kesintisiz kalmasını sağlar.
                                                while (dragTranslationX > fittedItemWidthPx * 0.50f &&
                                                    currentIndex < bottomMenuOrder.lastIndex
                                                ) {
                                                    val reordered = bottomMenuOrder.toMutableList()
                                                    val moved = reordered.removeAt(currentIndex)
                                                    reordered.add(currentIndex + 1, moved)
                                                    persistBottomMenuOrder(reordered)
                                                    dragTranslationX -= fittedItemWidthPx
                                                    currentIndex += 1
                                                }
                                                while (dragTranslationX < -fittedItemWidthPx * 0.50f &&
                                                    currentIndex > 0
                                                ) {
                                                    val reordered = bottomMenuOrder.toMutableList()
                                                    val moved = reordered.removeAt(currentIndex)
                                                    reordered.add(currentIndex - 1, moved)
                                                    persistBottomMenuOrder(reordered)
                                                    dragTranslationX += fittedItemWidthPx
                                                    currentIndex -= 1
                                                }

                                                // Sürüklenen öğe görünür alanın kenarına yaklaştığında
                                                // kaydırılabilir bölümü otomatik olarak hareket ettir.
                                                val viewportPx = with(density) { scrollViewportWidth.toPx() }
                                                val draggedCenterInViewport =
                                                    currentIndex * fittedItemWidthPx +
                                                        fittedItemWidthPx / 2f +
                                                        dragTranslationX -
                                                        scrollState.value
                                                val edgeZone = min(fittedItemWidthPx * 0.72f, viewportPx * 0.22f)
                                                val autoScrollDelta = when {
                                                    draggedCenterInViewport < edgeZone ->
                                                        -min(18f, edgeZone - draggedCenterInViewport)
                                                    draggedCenterInViewport > viewportPx - edgeZone ->
                                                        min(18f, draggedCenterInViewport - (viewportPx - edgeZone))
                                                    else -> 0f
                                                }
                                                if (autoScrollDelta != 0f) {
                                                    bottomMenuDragScope.launch {
                                                        scrollState.scrollBy(autoScrollDelta)
                                                    }
                                                }
                                            }
                                        }
                                    } else Modifier

                                    Column(
                                        modifier = Modifier
                                            .width(fittedItemWidth)
                                            .fillMaxHeight()
                                            .graphicsLayer {
                                                scaleX = selectedScale
                                                scaleY = selectedScale
                                                translationX = if (isBeingDragged) dragTranslationX else 0f
                                                // Üst/alt geçişlerden bağımsız optik merkez düzeltmesi.
                                                translationY = with(density) { 1.dp.toPx() }
                                                rotationZ = if (bottomMenuEditing && !isBeingDragged) {
                                                    editJiggle
                                                } else {
                                                    0f
                                                }
                                            }
                                            .zIndex(if (isBeingDragged) 2f else 0f)
                                            .then(reorderModifier)
                                            .clickable {
                                                if (!bottomMenuEditing) {
                                                    openMainScreen(menuScreen)
                                                }
                                            }
                                            .padding(horizontal = 2.dp, vertical = 2.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .shadow(
                                                    elevation = selectedElevation.dp,
                                                    shape = RoundedCornerShape(16.dp),
                                                    ambientColor = Color.Black.copy(alpha = 0.06f),
                                                    spotColor = Color.Black.copy(alpha = 0.08f)
                                                )
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(selectionColor)
                                                .padding(horizontal = 7.dp, vertical = 2.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(14.dp))
                                                    .background(Color.Black.copy(alpha = 0.10f))
                                                    .then(
                                                        if (bottomMenuEditing) {
                                                            Modifier.border(
                                                                width = if (isBeingDragged) 2.dp else 1.dp,
                                                                color = Color.Black.copy(
                                                                    alpha = if (isBeingDragged) 0.20f else 0.10f
                                                                ),
                                                                shape = RoundedCornerShape(14.dp)
                                                            )
                                                        } else {
                                                            Modifier
                                                        }
                                                    )
                                                    .padding(
                                                        horizontal = if (isBeingDragged) 6.dp else 5.dp,
                                                        vertical = if (isBeingDragged) 3.dp else 2.dp
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = label,
                                                    modifier = Modifier.size(iconSize),
                                                    tint = foregroundColor
                                                )
                                            }
                                        }
                                        Spacer(Modifier.height(0.5.dp))
                                        Text(
                                            label,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontSize = menuTextSize,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                            color = foregroundColor
                                        )
                                    }
                                    }
                                }
                            }

                            VerticalDivider(
                                modifier = Modifier
                                    .fillMaxHeight(0.58f)
                                    .width(0.5.dp),
                                color = Color.Black.copy(alpha = 0.10f)
                            )

                            Box(
                                modifier = Modifier
                                    .width(menuButtonWidth)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .combinedClickable(
                                            onClick = {
                                                if (bottomMenuEditing) {
                                                    bottomMenuEditing = false
                                                } else {
                                                    bottomMenuPopupVisible = true
                                                }
                                            },
                                            onLongClick = {
                                                if (!bottomMenuEditing) {
                                                    bottomMenuEditing = true
                                                    bottomMenuPopupVisible = false
                                                }
                                            }
                                        )
                                        .padding(vertical = 2.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = if (bottomMenuEditing) Icons.Default.Check
                                        else Icons.Outlined.Edit,
                                        contentDescription = if (bottomMenuEditing) {
                                            "Menü düzenlemeyi bitir"
                                        } else {
                                            "Alt menü seçenekleri"
                                        },
                                        modifier = Modifier.size(
                                            if (bottomMenuSize == BottomMenuSize.LARGE) 28.dp else 24.dp
                                        ),
                                        tint = Color.Black.copy(alpha = 0.60f)
                                    )
                                    Spacer(Modifier.height(0.5.dp))
                                    Text(
                                        if (bottomMenuEditing) "Bitti" else "Menü",
                                        fontSize = if (bottomMenuSize == BottomMenuSize.LARGE) 10.sp else 9.sp,
                                        color = Color.Black.copy(alpha = 0.60f),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                DropdownMenu(
                                    expanded = bottomMenuPopupVisible,
                                    onDismissRequest = { bottomMenuPopupVisible = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Menüyü düzenle") },
                                        leadingIcon = { Icon(Icons.Default.DragIndicator, null) },
                                        onClick = {
                                            bottomMenuPopupVisible = false
                                            bottomMenuEditing = true
                                        }
                                    )
                                    HorizontalDivider()
                                    Text(
                                        "Menü buton boyutu",
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = LogoColorDark
                                    )
                                    listOf(
                                        BottomMenuSize.SMALL to "Küçük",
                                        BottomMenuSize.MEDIUM to "Orta",
                                        BottomMenuSize.LARGE to "Büyük"
                                    ).forEach { (size, title) ->
                                        DropdownMenuItem(
                                            text = { Text(title) },
                                            leadingIcon = {
                                                Icon(
                                                    if (bottomMenuSize == size) {
                                                        Icons.Default.RadioButtonChecked
                                                    } else {
                                                        Icons.Default.RadioButtonUnchecked
                                                    },
                                                    null
                                                )
                                            },
                                            onClick = {
                                                changeBottomMenuSize(size)
                                                bottomMenuPopupVisible = false
                                            }
                                        )
                                    }
                                    HorizontalDivider()
                                    DropdownMenuItem(
                                        text = { Text("Varsayılan sıraya dön") },
                                        leadingIcon = { Icon(Icons.Default.Restore, null) },
                                        onClick = {
                                            persistBottomMenuOrder(defaultMenuOrder)
                                            bottomMenuPopupVisible = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                }

                BottomShellSoftTransition(mirrored = false)
            }
        }
    ) { padding ->
        val swipeThresholdPx = with(LocalDensity.current) { 96.dp.toPx() }
        WatermarkContainer {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(
                        screen,
                        bottomMenuOrder,
                        bottomMenuEditing,
                        retainedSettingsPage,
                        unlockedProfileId
                    ) {
                        if (!bottomMenuEditing &&
                            (screen != Screen.SETTINGS || retainedSettingsPage == SettingsPage.HOME)
                        ) {
                            var totalDrag = 0f
                            detectHorizontalDragGestures(
                                onDragStart = { totalDrag = 0f },
                                onHorizontalDrag = { change, dragAmount ->
                                    totalDrag += dragAmount
                                    if (kotlin.math.abs(totalDrag) > 24f) change.consume()
                                },
                                onDragCancel = { totalDrag = 0f },
                                onDragEnd = {
                                    if (kotlin.math.abs(totalDrag) >= swipeThresholdPx) {
                                        val currentIndex = bottomMenuOrder.indexOf(screen)
                                        if (currentIndex >= 0) {
                                            val targetIndex = if (totalDrag < 0f) {
                                                currentIndex + 1
                                            } else {
                                                currentIndex - 1
                                            }
                                            bottomMenuOrder.getOrNull(targetIndex)?.let(::openMainScreen)
                                        }
                                    }
                                    totalDrag = 0f
                                }
                            )
                        }
                    }
            ) {
            NavHost(
                navController = navController,
                startDestination = Screen.TODAY.route,
                modifier = Modifier.fillMaxSize()
            ) {
                Screen.entries.forEach { destination ->
                    composable(
                        route = destination.route,
                        deepLinks = listOf(
                            navDeepLink { uriPattern = "sifahane://app/${destination.route}" }
                        )
                    ) {
                        when (destination) {
            Screen.PROFILES -> ProfilesScreen(
                profiles = profiles,
                activeId = activeProfileId,
                modifier = Modifier.padding(padding),
                onSelect = {
                    activeProfileId = it.id
                    unlockedProfileId = null
                    if (PatternStore.hasPattern(context, it.id)) {
                        pendingPatternProfile = it
                        forcePatternCreate = false
                    } else {
                        adminTargetProfile = it
                        adminPinPurpose = AdminPinPurpose.UNLOCK_PROFILE
                    }
                },
                onEdit = { editProfile = it },
                onDelete = { profile ->
                    scope.launch {
                        val lastAdmin = profile.role == UserRoles.ADMIN &&
                            withContext(Dispatchers.IO) { db.profileDao().enabledAdminCount() } <= 1
                        if (lastAdmin) {
                            exportStatus = "Tek kalan yönetici profili silinemez. Önce başka bir yönetici atayın."
                        } else {
                            adminDeleteProfile = profile
                            adminTargetProfile = profile
                            adminPinPurpose = AdminPinPurpose.DELETE_PROFILE
                        }
                    }
                },
                onChangePattern = { profile ->
                    activeProfileId = profile.id
                    unlockedProfileId = null
                    patternChangeProfileId = profile.id
                    if (PatternStore.hasPattern(context, profile.id)) {
                        pendingPatternProfile = profile
                        forcePatternCreate = false
                    } else {
                        adminTargetProfile = profile
                        adminPinPurpose = AdminPinPurpose.CHANGE_PATTERN
                    }
                }
            )
            Screen.TODAY -> TodayScreen(
                meds = meds,
                logs = logs,
                appointments = appointments,
                modifier = Modifier.padding(padding),
                onOpenAppointments = { screen = Screen.APPOINTMENTS }
            )
            Screen.APPOINTMENTS -> AppointmentsScreen(
                appointments = appointments,
                profileId = activeProfileId,
                db = db,
                modifier = Modifier.padding(padding)
            )
            Screen.MEDICINES -> MedicineScreen(
                active = meds,
                archive = archive,
                reportGroups = reportGroups,
                modifier = Modifier.padding(padding),
                onEditMedication = { editMedication = it },
                onNewReportGroup = { newReportGroup = true },
                onEditReportGroup = { editReportGroup = it },
                selectedTab = medicinesTab,
                onTabChange = { medicinesTab = it },
                onAdd = {
                    if (medicinesTab == 2) {
                        newReportGroup = true
                    } else {
                        newMedication = true
                    }
                },
                onDeleteReportGroup = { group ->
                    scope.launch(Dispatchers.IO) {
                        db.reportGroupDao().unlinkMedications(group.id)
                        db.reportGroupDao().delete(group)
                    }
                }
            )
            Screen.MEASUREMENTS -> MeasurementsScreen(
                tab = measureTab,
                onTab = { measureTab = it },
                bp = bp,
                glucose = glucose,
                modifier = Modifier.padding(padding),
                onEditBp = { editBp = it },
                onEditGlucose = { editGlucose = it },
                onAddMeasurement = {
                    if (measureTab == MeasureTab.BP) addBp = true else addGlucose = true
                }
            )
            Screen.REPORTS -> ReportsScreen(activeProfileId, db, Modifier.padding(padding))
            Screen.SETTINGS -> SettingsScreen(
                modifier = Modifier.padding(padding),
                initialPage = retainedSettingsPage,
                onPageChanged = { retainedSettingsPage = it },
                alarmRefreshBusy = alarmRefreshBusy,
                alarmRefreshResult = alarmRefreshResult,
                onAlarmRefresh = {
                    alarmRefreshBusy = true
                    scope.launch {
                        alarmRefreshResult = withContext(Dispatchers.IO) {
                            AlarmRescheduler.refreshAll(context)
                        }
                        alarmRefreshBusy = false
                        showAlarmStatus = true
                    }
                },
                onClearStaleAlarms = {
                    alarmRefreshBusy = true
                    scope.launch {
                        val result = withContext(Dispatchers.IO) {
                            AlarmRescheduler.clearStaleAndRefresh(context)
                        }
                        alarmRefreshResult = result.second
                        exportStatus = "${result.first} eski alarm isteği temizlendi; geçerli alarmlar yeniden oluşturuldu."
                        alarmRefreshBusy = false
                        showAlarmStatus = true
                    }
                },
                onAlarmStatus = { showAlarmStatus = true },
                onTestAlarm = {
                    AlarmScheduler.scheduleTest(
                        context,
                        System.currentTimeMillis() + 10_000L
                    )
                },
                hasActiveProfile = profiles.any { it.id == activeProfileId },
                backupStatusText = exportStatus,
                profiles = profiles,
                activeProfileId = activeProfileId,
                canManageSecurity = activeIsAdmin,
                databaseVersion = 11,
                bottomMenuSize = bottomMenuSize,
                onBottomMenuSizeChange = {
                    changeBottomMenuSize(it)
                    Toast.makeText(context, "Alt menü boyutu kaydedildi.", Toast.LENGTH_SHORT).show()
                },
                onResetBottomMenuOrder = {
                    persistBottomMenuOrder(defaultMenuOrder)
                    Toast.makeText(context, "Alt menü sırası varsayılana döndürüldü.", Toast.LENGTH_SHORT).show()
                },
                onExportPersonData = {
                    profiles.firstOrNull { it.id == activeProfileId }?.let {
                        exportProfile(it.id)
                    }
                },
                onImportPersonData = {
                    if (activeIsAdmin) showImportSourceDialog = true
                    else exportStatus = "Veri içeri aktarma işlemi için yönetici yetkisi gereklidir."
                },
                onSetRole = { profileId, makeAdmin ->
                    scope.launch {
                        val target = profiles.firstOrNull { it.id == profileId } ?: return@launch
                        if (!makeAdmin && target.role == UserRoles.ADMIN) {
                            val count = withContext(Dispatchers.IO) { db.profileDao().enabledAdminCount() }
                            if (count <= 1) {
                                exportStatus = "Son yönetici standart kullanıcıya dönüştürülemez."
                                return@launch
                            }
                        }
                        withContext(Dispatchers.IO) {
                            db.profileDao().updateRole(
                                profileId,
                                if (makeAdmin) UserRoles.ADMIN else UserRoles.STANDARD,
                                if (makeAdmin) UserRoles.ADMIN_PERMISSIONS else UserRoles.STANDARD_PERMISSIONS
                            )
                        }
                        exportStatus = if (makeAdmin) "${target.name} yönetici olarak atandı."
                        else "${target.name} standart kullanıcı olarak ayarlandı."
                    }
                },
                onSetAdminPin = { profileId, pin ->
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            db.profileDao().updateAdminPinHash(
                                profileId,
                                AdminCredentialHasher.hash(profileId, pin)
                            )
                        }
                        exportStatus = "Yönetici şifresi güvenli biçimde kaydedildi."
                    }
                },
                onNavigate = { target ->
                    if (target != screen) screen = target
                }
            )
                        }
                    }
                }
            }
            }
        }
    }

    }

    IdleHeartOverlay(
        visible = idleHeartVisible,
        animationKey = idleHeartKey
    )

    if (showAlarmStatus) {
        AlarmStatusDialog(
            refreshResult = alarmRefreshResult,
            onDismiss = { showAlarmStatus = false }
        )
    }

    if (newReportGroup || editReportGroup != null) {
        ReportGroupEditorDialog(
            existing = editReportGroup,
            profileId = activeProfileId,
            onDismiss = {
                newReportGroup = false
                editReportGroup = null
            },
            onSave = { group ->
                scope.launch(Dispatchers.IO) {
                    if (group.id == 0L) {
                        db.reportGroupDao().insert(group)
                    } else {
                        db.reportGroupDao().update(group)
                        db.medicationDao().updateReportGroupDates(
                            group.id,
                            group.startDate,
                            group.endDate,
                            group.warningDays
                        )
                    }
                }
                newReportGroup = false
                editReportGroup = null
            }
        )
    }

    if (newMedication || editMedication != null) {
        MedicationEditorDialog(
            existing = editMedication,
            profileId = activeProfileId,
            suggestions = allMedications,
            reportGroups = reportGroups,
            onCreateReportGroup = { group ->
                scope.launch(Dispatchers.IO) {
                    db.reportGroupDao().insert(group)
                }
            },
            onUpdateReportGroup = { group ->
                scope.launch(Dispatchers.IO) {
                    db.reportGroupDao().update(group)
                    db.medicationDao().updateReportGroupDates(
                        group.id,
                        group.startDate,
                        group.endDate,
                        group.warningDays
                    )
                }
            },
            onDismiss = { newMedication = false; editMedication = null },
            onSave = { med ->
                scope.launch(Dispatchers.IO) {
                    if (med.id == 0L) {
                        db.medicationDao().insert(med)
                    } else {
                        AlarmScheduler.cancelMedication(context, med.id)
                        db.medicationDao().update(med)
                    }
                    ManagedPhotoCleaner.cleanup(context, db)
                    AlarmRescheduler.refreshAll(context)
                }
                newMedication = false
                editMedication = null
            },
            onDelete = { med ->
                scope.launch {
                    val message = withContext(Dispatchers.IO) {
                        AlarmScheduler.cancelMedication(context, med.id)
                        val historyCount = db.medicationDao().doseLogCount(med.id)
                        val resultMessage = if (historyCount > 0) {
                            db.medicationDao().archiveForHistory(med.id)
                            "Bu ilaca bağlı $historyCount doz geçmişi korundu. İlaç silinmek yerine arşive taşındı."
                        } else {
                            db.medicationDao().delete(med)
                            "İlaç kaydı silindi."
                        }
                        ManagedPhotoCleaner.cleanup(context, db)
                        AlarmRescheduler.refreshAll(context)
                        resultMessage
                    }
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
                editMedication = null
            }
        )
    }

    if (newProfile || editProfile != null) {
        ProfileEditorDialog(
            existing = editProfile,
            onDismiss = { newProfile = false; editProfile = null },
            onSave = { p ->
                scope.launch {
                    val savedId = withContext(Dispatchers.IO) {
                        if (p.id == 0L) db.profileDao().insert(p)
                        else {
                            db.profileDao().update(p)
                            ManagedPhotoCleaner.cleanup(context, db)
                            p.id
                        }
                    }
                    if (p.id == 0L) {
                        activeProfileId = savedId
                        unlockedProfileId = null
                        pendingPatternProfile = p.copy(id = savedId)
                        forcePatternCreate = true
                    }
                }
                newProfile = false
                editProfile = null
            }
        )
    }

    if (showAddProfileChoice) {
        AlertDialog(
            onDismissRequest = { showAddProfileChoice = false },
            title = { Text("Yeni kişi nasıl oluşturulsun?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Kişi bilgilerini elle girebilir veya parola korumalı .sifbak / uyumlu eski ZIP yedeğinden içe aktarabilirsiniz.")
                    GlassLogoButton("Bilgileri Gir", {
                        showAddProfileChoice = false
                        newProfile = true
                    }, Modifier.fillMaxWidth())
                    GlassLogoButton("Yedekten İçe Aktar", {
                        showAddProfileChoice = false
                        showImportRoleChoice = true
                    }, Modifier.fillMaxWidth())
                    GlassLogoButton("İptal", {
                        showAddProfileChoice = false
                        addProfileImportMode = false
                    }, Modifier.fillMaxWidth())
                    GlassLogoButton("Geri", {
                        showAddProfileChoice = false
                    }, Modifier.fillMaxWidth())
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }

    if (showImportRoleChoice) {
        AlertDialog(
            onDismissRequest = { showImportRoleChoice = false },
            title = { Text("Yeni kullanıcının yetkisi") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("İçe aktarılacak kişi yönetici mi, standart kullanıcı mı olacak?")
                    GlassLogoButton("Standart Kullanıcı", {
                        importNewProfileAsAdmin = false
                        addProfileImportMode = true
                        showImportRoleChoice = false
                        showImportSourceDialog = true
                    }, Modifier.fillMaxWidth())
                    GlassLogoButton("Yönetici", {
                        importNewProfileAsAdmin = true
                        addProfileImportMode = true
                        showImportRoleChoice = false
                        showImportSourceDialog = true
                    }, Modifier.fillMaxWidth())
                    GlassLogoButton("İptal", {
                        showImportRoleChoice = false
                        addProfileImportMode = false
                    }, Modifier.fillMaxWidth())
                    GlassLogoButton("Geri", {
                        showImportRoleChoice = false
                        showAddProfileChoice = true
                    }, Modifier.fillMaxWidth())
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }

    if (addBp) BpDialog(
        onDismiss = { addBp = false },
        onSave = { s, d, p, note ->
            scope.launch(Dispatchers.IO) {
                db.vitalsDao().insertBp(BloodPressure(profileId = activeProfileId, systolic = s, diastolic = d, pulse = p, measuredAt = System.currentTimeMillis(), note = note))
            }
            addBp = false
        }
    )

    if (addGlucose) GlucoseDialog(
        onDismiss = { addGlucose = false },
        onSave = { v, t, note ->
            scope.launch(Dispatchers.IO) {
                db.vitalsDao().insertGlucose(BloodGlucose(profileId = activeProfileId, valueMgDl = v, measurementType = t, measuredAt = System.currentTimeMillis(), note = note))
            }
            addGlucose = false
        }
    )

    editBp?.let { item ->
        EditBpDialog(
            item = item,
            onDismiss = { editBp = null },
            onSave = { updated ->
                scope.launch(Dispatchers.IO) { db.vitalsDao().updateBp(updated) }
                editBp = null
            },
            onDelete = {
                scope.launch(Dispatchers.IO) { db.vitalsDao().deleteBp(item) }
                editBp = null
            }
        )
    }

    editGlucose?.let { item ->
        EditGlucoseDialog(
            item = item,
            onDismiss = { editGlucose = null },
            onSave = { updated ->
                scope.launch(Dispatchers.IO) { db.vitalsDao().updateGlucose(updated) }
                editGlucose = null
            },
            onDelete = {
                scope.launch(Dispatchers.IO) { db.vitalsDao().deleteGlucose(item) }
                editGlucose = null
            }
        )
    }

    pendingPatternProfile?.let { profile ->
        ProfilePatternGate(
            profile = profile,
            forceCreate = forcePatternCreate,
            onUnlocked = {
                if (
                    patternChangeProfileId == profile.id &&
                    !forcePatternCreate
                ) {
                    PatternStore.clear(context, profile.id)
                    unlockedProfileId = null
                    pendingPatternProfile = profile
                    forcePatternCreate = true
                } else {
                    activeProfileId = profile.id
                    forcePatternCreate = false
                    pendingPatternProfile = null
                    unlockedProfileId = profile.id
                    patternChangeProfileId = null
                    screen = Screen.TODAY
                }
            },
            onCancel = {
                pendingPatternProfile = null
                forcePatternCreate = false
                patternChangeProfileId = null
                unlockedProfileId = null
                screen = Screen.PROFILES
            },
            onAdminRequested = {
                adminTargetProfile = profile
                adminPinPurpose =
                    if (patternChangeProfileId == profile.id)
                        AdminPinPurpose.CHANGE_PATTERN
                    else
                        AdminPinPurpose.UNLOCK_PROFILE
            }
        )
    }

    adminPinPurpose?.let { purpose ->
        AdminPinDialog(
            purpose = purpose,
            profileName = adminTargetProfile?.let {
                "${it.name} ${it.surname}".trim()
            },
            verificationProfiles = profiles,
            onDismiss = {
                if (
                    purpose == AdminPinPurpose.UNLOCK_PROFILE ||
                    purpose == AdminPinPurpose.CHANGE_PATTERN
                ) {
                    unlockedProfileId = null
                    pendingPatternProfile = null
                    forcePatternCreate = false
                    patternChangeProfileId = null
                    screen = Screen.PROFILES
                }
                adminPinPurpose = null
                adminTargetProfile = null
                adminDeleteProfile = null
            },
            onVerified = {
                when (purpose) {
                    AdminPinPurpose.INITIAL_SETUP -> {
                        AppPreferences.markVersionVerified(context, BuildConfig.VERSION_CODE)
                        adminPinPurpose = null
                        adminSessionUnlocked = true
                        pendingPatternProfile = null
                        forcePatternCreate = false
                        initialScreenSet = true
                        screen = Screen.PROFILES
                        showInitialImportChoice = true
                    }
                    AdminPinPurpose.UPDATE_VERIFICATION -> {
                        AppPreferences.markVersionVerified(context, BuildConfig.VERSION_CODE)
                        adminPinPurpose = null
                        adminSessionUnlocked = false
                        unlockedProfileId = null
                        screen = Screen.PROFILES
                    }
                    AdminPinPurpose.UNLOCK_PROFILE -> {
                        adminTargetProfile?.let { profile ->
                            PatternStore.clear(context, profile.id)
                            activeProfileId = profile.id
                            unlockedProfileId = null
                            pendingPatternProfile = profile
                            forcePatternCreate = true
                        }
                        adminPinPurpose = null
                        adminTargetProfile = null
                    }
                    AdminPinPurpose.CHANGE_PATTERN -> {
                        adminTargetProfile?.let { profile ->
                            PatternStore.clear(context, profile.id)
                            activeProfileId = profile.id
                            unlockedProfileId = null
                            patternChangeProfileId = profile.id
                            pendingPatternProfile = profile
                            forcePatternCreate = true
                        }
                        adminPinPurpose = null
                        adminTargetProfile = null
                    }
                    AdminPinPurpose.DELETE_PROFILE -> {
                        adminDeleteProfile?.let { profile ->
                            scope.launch(Dispatchers.IO) {
                                db.profileDao().deleteProfileCascade(profile.id)
                                ManagedPhotoCleaner.cleanup(context, db)
                                PatternStore.clear(context, profile.id)
                                BiometricPreferences.clear(context, profile.id)
                            }
                            if (activeProfileId == profile.id) {
                                unlockedProfileId = null
                                pendingPatternProfile = null
                                patternChangeProfileId = null
                            }
                        }
                        adminDeleteProfile = null
                        adminTargetProfile = null
                        adminPinPurpose = null
                        screen = Screen.PROFILES
                    }
                    AdminPinPurpose.ADMIN_SETTINGS -> {
                        adminSessionUnlocked = true
                        adminPinPurpose = null
                    }
                }
            }
        )
    }
}
