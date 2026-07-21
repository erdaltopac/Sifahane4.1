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

internal data class HelpTopic(
    val title: String,
    val body: String,
    val target: Screen? = null,
    val targetLabel: String? = null
)

internal data class SettingsNotice(
    val id: String,
    val type: String,
    val title: String,
    val body: String,
    val timestamp: Long
)


internal suspend fun dbAppointmentRefresh(context: Context) {
    val db = AppDatabase.get(context)
    val appointments = db.appointmentDao().activeFuture(System.currentTimeMillis())
    appointments.forEach {
        AppointmentAlarmScheduler.cancel(context, it.id)
        AppointmentAlarmScheduler.schedule(context, it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    modifier: Modifier,
    initialPage: SettingsPage,
    onPageChanged: (SettingsPage) -> Unit,
    alarmRefreshBusy: Boolean,
    alarmRefreshResult: AlarmRefreshResult?,
    onAlarmRefresh: () -> Unit,
    onClearStaleAlarms: () -> Unit,
    onAlarmStatus: () -> Unit,
    onTestAlarm: () -> Unit,
    hasActiveProfile: Boolean,
    backupStatusText: String,
    profiles: List<UserProfile>,
    activeProfileId: Long,
    canManageSecurity: Boolean,
    databaseVersion: Int,
    bottomMenuSize: BottomMenuSize,
    onBottomMenuSizeChange: (BottomMenuSize) -> Unit,
    onResetBottomMenuOrder: () -> Unit,
    onExportPersonData: () -> Unit,
    onImportPersonData: () -> Unit,
    onSetRole: (Long, Boolean) -> Unit,
    onSetAdminPin: (Long, String) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val settingsScope = rememberCoroutineScope()
    val settingsDb = remember { AppDatabase.get(context) }
    val notificationMedications by (if (activeProfileId > 0L) settingsDb.medicationDao().observeForProfile(activeProfileId) else flowOf(emptyList()))
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val notificationDoseLogs by (if (activeProfileId > 0L) settingsDb.doseLogDao().observeForProfile(activeProfileId) else flowOf(emptyList()))
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val notificationAppointments by (if (activeProfileId > 0L) settingsDb.appointmentDao().observeForProfile(activeProfileId) else flowOf(emptyList()))
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val accessibilityPrefs = remember {
        context.getSharedPreferences("sifahane_accessibility", Context.MODE_PRIVATE)
    }

    var page by remember(initialPage) { mutableStateOf(initialPage) }
    LaunchedEffect(page) { onPageChanged(page) }
    fun openSettings(target: SettingsPage) {
        page = target
        AppPreferences.recordSettingsUse(context, target.name)
    }
    fun settingsLabel(target: SettingsPage) = when (target) {
        SettingsPage.NOTIFICATIONS -> "Bildirim Merkezi"
        SettingsPage.ALARMS -> "Alarm ve Bildirimler"
        SettingsPage.APPOINTMENTS -> "Randevu Hatırlatıcıları"
        SettingsPage.BACKUP -> "Yedekleme ve Geri Yükleme"
        SettingsPage.SECURITY -> "Güvenlik ve Yetkilendirme"
        SettingsPage.PERMISSIONS -> "Android İzinleri"
        SettingsPage.ACCESSIBILITY -> "Görünüm ve Erişilebilirlik"
        SettingsPage.THEME -> "Tema Ayarları"
        SettingsPage.DATABASE -> "Veritabanı ve Sistem"
        SettingsPage.HELP -> "Yardım ve Kullanım"
        SettingsPage.ABOUT -> "Uygulama Hakkında"
        SettingsPage.HOME -> "Ayarlar"
    }
    var query by remember { mutableStateOf("") }
    var expandedHelpTitle by remember { mutableStateOf<String?>(null) }
    var showComprehensiveGuide by remember { mutableStateOf(false) }
    var guideQuery by remember { mutableStateOf("") }
    var largeSettingsText by remember {
        mutableStateOf(accessibilityPrefs.getBoolean("large_settings_text", false))
    }
    var highContrastSettings by remember {
        mutableStateOf(accessibilityPrefs.getBoolean("high_contrast_settings", false))
    }
    var permissionRefresh by remember { mutableIntStateOf(0) }
    var noticeFilter by rememberSaveable { mutableStateOf("Tümü") }
    var noticeReadRevision by remember { mutableIntStateOf(0) }
    val noticePrefs = remember { context.getSharedPreferences("sifahane_notification_center", Context.MODE_PRIVATE) }
    val notificationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionRefresh++ }
    val cameraPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionRefresh++ }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) permissionRefresh++
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    var previousExactAlarmAllowed by remember { mutableStateOf<Boolean?>(null) }
    LaunchedEffect(permissionRefresh) {
        val manager = context.getSystemService(AlarmManager::class.java)
        val currentAllowed = Build.VERSION.SDK_INT < 31 || manager.canScheduleExactAlarms()
        if (previousExactAlarmAllowed == false && currentAllowed) {
            onAlarmRefresh()
            Toast.makeText(
                context,
                "Kesin alarm izni açıldı; alarmlar yeniden planlandı.",
                Toast.LENGTH_SHORT
            ).show()
        }
        previousExactAlarmAllowed = currentAllowed
    }

    var pinTarget by remember { mutableStateOf<UserProfile?>(null) }
    var newAdminPin by remember { mutableStateOf("") }
    var confirmAdminPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }
    var biometricPreferenceRevision by remember { mutableIntStateOf(0) }
    var snoozeValues by remember { mutableStateOf(AppPreferences.snoozeMinutes(context).map(Int::toString)) }
    var snoozeError by remember { mutableStateOf<String?>(null) }
    var alarmRingDuration by remember { mutableLongStateOf(AppPreferences.alarmRingDurationMillis(context)) }
    var alarmDurationMenuExpanded by remember { mutableStateOf(false) }
    var confirmClearAlarms by remember { mutableStateOf(false) }
    var lockTimeout by remember { mutableLongStateOf(AppPreferences.lockTimeoutMillis(context)) }
    var lockTimeoutMenuExpanded by remember { mutableStateOf(false) }
    val storedTheme = remember { ThemePreferences.load(context) }
    var themeMode by remember { mutableStateOf(storedTheme.mode) }
    var themePreset by remember { mutableStateOf(storedTheme.preset) }
    var themeFont by remember { mutableStateOf(storedTheme.font) }
    var themeScale by remember { mutableFloatStateOf(storedTheme.fontScale) }
    var themeOpacity by remember { mutableFloatStateOf(storedTheme.accentOpacity) }
    var cardInnerAccentOpacity by remember { mutableFloatStateOf(storedTheme.cardInnerAccentOpacity) }
    var dynamicColorEnabled by remember { mutableStateOf(storedTheme.dynamicColor) }
    var themeModeExpanded by remember { mutableStateOf(false) }
    var themePresetExpanded by remember { mutableStateOf(false) }
    var themeFontExpanded by remember { mutableStateOf(false) }
    var restorePointRevision by remember { mutableIntStateOf(0) }
    var restorePointBusy by remember { mutableStateOf(false) }
    var restorePointStatus by remember { mutableStateOf("") }
    var restorePreview by remember { mutableStateOf<Pair<RestorePoint, BackupPreview>?>(null) }
    var restoreConfirmStep by remember { mutableIntStateOf(0) }
    var deleteRestorePoint by remember { mutableStateOf<RestorePoint?>(null) }
    val restorePoints = remember(activeProfileId, restorePointRevision) {
        if (activeProfileId > 0L) RestorePointManager.list(context, activeProfileId) else emptyList()
    }

    val titleSize = if (largeSettingsText) 21.sp else 18.sp
    val bodySize = if (largeSettingsText) 17.sp else 14.sp
    val supportingSize = if (largeSettingsText) 15.sp else 13.sp
    val settingsBackground = if (highContrastSettings) Color.White else Color.Transparent
    val cardBackground = if (highContrastSettings) Color.White else Vantablack05
    val cardBorder = if (highContrastSettings) LogoColorDark else LogoColor.copy(alpha = 0.65f)

    val helpTopics = remember {
        listOf(
            HelpTopic(
                "İlaç ekleme ve düzenleme",
                "İlaçlar sayfasındaki artı düğmesiyle yeni ilaç ekleyin. Kayıtlı ilaca dokunarak doz, saat, stok, doktor ve rapor bilgilerini düzenleyin.",
                Screen.MEDICINES,
                "İlaçlara Git"
            ),
            HelpTopic(
                "Alarm çalışmıyorsa",
                "Alarm Yenile işlemini çalıştırın. Ardından bildirim, kesin alarm, tam ekran alarm ve pil optimizasyonu izinlerini Alarm Durumu bölümünden kontrol edin."
            ),
            HelpTopic(
                "Bugünün geçmiş ilaç saatleri",
                "Bugün saati geçmiş ve henüz cevaplanmamış dozlar Alarm Yenile sonrasında sırayla gösterilir. İlacın alınıp alınmadığı ve alındıysa gerçek saat kaydedilir.",
                Screen.TODAY,
                "Bugüne Git"
            ),
            HelpTopic(
                "Raporlu ilaçlar",
                "İlaç kaydında Raporlu İlaç seçeneğini açın. Rapor başlangıç-bitiş tarihlerini veya bağlı rapor grubunu belirleyin.",
                Screen.MEDICINES,
                "İlaçlara Git"
            ),
            HelpTopic(
                "Tansiyon ve şeker ölçümü",
                "Ölçümler sayfasında ilgili sekmeyi seçip artı düğmesine dokunun. Kaydı daha sonra üzerine dokunarak düzenleyebilirsiniz.",
                Screen.MEASUREMENTS,
                "Ölçümlere Git"
            ),
            HelpTopic(
                "Rapor ve Excel işlemleri",
                "Raporlar sayfasında tarih aralığını seçin, grafik rapor oluşturun veya kayıtları Excel dosyası olarak dışa aktarın.",
                Screen.REPORTS,
                "Raporlara Git"
            ),
            HelpTopic(
                "Kullanıcı profili ve desen",
                "Kişiler sayfasında kullanıcı ekleyebilir, profil bilgilerini düzenleyebilir, desen şifresini değiştirebilir ve aktif kişiyi seçebilirsiniz.",
                Screen.PROFILES,
                "Kişilere Git"
            ),
            HelpTopic(
                "Yedekleme ve geri yükleme",
                "Yedekleme ve Geri Yükleme bölümünden parola korumalı AES-GCM .sifbak dosyası oluşturabilir veya yeni/eski uyumlu bir Şifahane yedeğini içeri aktarabilirsiniz."
            ),
            HelpTopic(
                "Kullanıcı rolleri ve yöneticiler",
                "Güvenlik ve Yetkilendirme bölümünden kullanıcıları yönetici veya standart kullanıcı olarak atayabilir, her yönetici için ayrı şifre belirleyebilirsiniz."
            ),
            HelpTopic(
                "Gizlilik ve veri güvenliği",
                "Sağlık kayıtları cihazdaki uygulama veritabanında tutulur. Cihaz kilidi ve kullanıcı desenlerini etkin tutun; dışa aktarılan dosyaları güvenli yerde saklayın."
            ),
            HelpTopic(
                "Tıbbi kapsam ve güvenli kullanım",
                "Şifahane tanı koymaz, tedavi veya doz değişikliği önermez ve acil durum hizmeti değildir. Kaçırılan dozda çift doz almayın; ilacın prospektüsünü izleyin ve hekim ya da eczacınıza danışın. Acil durumda 112'yi arayın."
            ),
            HelpTopic(
                "İlk kurulum ve güncelleme doğrulaması",
                "İlk kurulumda kullanıcı kendi 4–12 haneli yönetici şifresini oluşturur; uygulamada varsayılan veya arka kapı şifresi bulunmaz. PIN ve profil desenleri salt’lı PBKDF2 ile korunur; art arda hatalı denemelerde kalıcı ve artan bekleme süresi uygulanır."
            ),
            HelpTopic(
                "Grup ilaç alarmı",
                "Aynı dakikadaki ilaçlar tek ses ve tek kartta gösterilir. Farklı kişilerin ilaçları kullanıcı sekmeleriyle ayrılır; ilaçlar tek tek veya seçili kullanıcı için topluca cevaplanabilir."
            ),
            HelpTopic(
                "Erteleme sürelerini değiştirme",
                "Ayarlar > Alarm ve Bildirimler bölümünde üç farklı erteleme süresi belirleyin. Alarm kartındaki Ertele seçimi bu üç süreyi gösterir."
            ),
            HelpTopic(
                "Otomatik desen kilidi",
                "Ayarlar > Güvenlik ve Yetkilendirme bölümünden işlem yapılmadığında kilitlenme süresini seçin. Alarm ekranı bu otomatik kilitten etkilenmez."
            ),
            HelpTopic(
                "Yedek alma ve ikinci kopya",
                "İlk dışa aktarmada dahili depolama konumunu bir kez seçin. Şifahane Yedek klasörü otomatik oluşturulur; ilk kopyadan sonra isteğe bağlı ikinci hedef seçilebilir."
            ),
            HelpTopic(
                "Telefon değiştirirken",
                "Eski telefonda kişi verilerini dışarı aktarın, ZIP dosyasını güvenli biçimde yeni telefona taşıyın ve ilk kurulumdaki içe aktarma seçeneğini kullanın."
            ),
            HelpTopic(
                "Grafik raporları okumak",
                "Raporlar bölümünde tarih aralığını seçip grafik oluşturun. Yatay eksen tarihleri, dikey eksen ölçüm değerlerini gösterir; açıklamalar veri serilerini belirtir."
            )
        )
    }

    val filteredHelp = helpTopics.filter {
        query.isBlank() ||
            it.title.contains(query, ignoreCase = true) ||
            it.body.contains(query, ignoreCase = true)
    }

    if (showComprehensiveGuide) {
        val guideSections = comprehensiveUserGuideSections().filter { (title, body) ->
            guideQuery.isBlank() || title.contains(guideQuery, true) || body.contains(guideQuery, true)
        }
        AlertDialog(
            onDismissRequest = { showComprehensiveGuide = false },
            title = { Text("Kapsamlı Kullanıcı Kılavuzu") },
            text = {
                Column(Modifier.fillMaxWidth().heightIn(max = 620.dp)) {
                    OutlinedTextField(
                        value = guideQuery,
                        onValueChange = { guideQuery = it },
                        label = { Text("Kılavuzda ara") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        item { Text("Uygulama ve kılavuz sürümü: ${BuildConfig.VERSION_NAME}", fontWeight = FontWeight.Bold) }
                        items(guideSections, key = { it.first }) { (title, body) ->
                            Column {
                                Text(title, fontWeight = FontWeight.Bold, color = LogoColorDark)
                                Text(body, color = LogoColorDark)
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showComprehensiveGuide = false }) { Text("KAPAT") } }
        )
    }

    androidx.activity.compose.BackHandler(enabled = page != SettingsPage.HOME) {
        page = SettingsPage.HOME
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(settingsBackground)
    ) {
        SettingsPageHeader(
            title = when (page) {
                SettingsPage.HOME -> "Ayarlar"
                SettingsPage.ALARMS -> "Alarm ve Bildirimler"
                SettingsPage.APPOINTMENTS -> "Randevu Hatırlatıcıları"
                SettingsPage.BACKUP -> "Yedekleme ve Geri Yükleme"
                SettingsPage.SECURITY -> "Güvenlik ve Yetkilendirme"
                SettingsPage.PERMISSIONS -> "Android İzinleri"
                SettingsPage.ACCESSIBILITY -> "Görünüm ve Erişilebilirlik"
                SettingsPage.THEME -> "Tema Ayarları"
                SettingsPage.DATABASE -> "Veritabanı ve Sistem"
                SettingsPage.HELP -> "Yardım ve Kullanım"
                SettingsPage.ABOUT -> "Uygulama Hakkında"
            },
            showBack = page != SettingsPage.HOME,
            titleSize = titleSize,
            onBack = { page = SettingsPage.HOME }
        )

        when (page) {
            SettingsPage.HOME -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            "Değiştirmek veya incelemek istediğiniz bölümü seçin.",
                            modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp),
                            textAlign = TextAlign.Center,
                            color = LogoColorDark,
                            fontSize = bodySize
                        )
                    }
                    AppPreferences.lastUsedSetting(context)
                        ?.let { runCatching { SettingsPage.valueOf(it) }.getOrNull() }
                        ?.takeIf { it != SettingsPage.HOME }
                        ?.let { recent ->
                            item {
                                SettingsCategoryCard(
                                    title = "Son kullanılan: ${settingsLabel(recent)}",
                                    description = "En son açtığınız ayara hızlı erişim",
                                    icon = Icons.Default.PushPin,
                                    titleSize = titleSize, bodySize = supportingSize,
                                    cardBackground = cardBackground, borderColor = cardBorder
                                ) { openSettings(recent) }
                            }
                        }
                    val settingKeys = SettingsPage.entries.filter { it != SettingsPage.HOME }.map { it.name }
                    val lastKey = AppPreferences.lastUsedSetting(context)
                    AppPreferences.frequentlyUsedSettings(context, settingKeys)
                        .filter { it != lastKey }
                        .mapNotNull { runCatching { SettingsPage.valueOf(it) }.getOrNull() }
                        .forEach { frequent ->
                            item {
                                SettingsCategoryCard(
                                    title = "Sık kullanılan: ${settingsLabel(frequent)}",
                                    description = "Kullanım sıklığınıza göre önerildi",
                                    icon = Icons.Default.Star,
                                    titleSize = titleSize, bodySize = supportingSize,
                                    cardBackground = cardBackground, borderColor = cardBorder
                                ) { openSettings(frequent) }
                            }
                        }
                    item {
                        SettingsCategoryCard(
                            title = "Bildirim Merkezi",
                            description = "Kaçırılan doz, yaklaşan randevu, düşük stok ve rapor uyarıları",
                            icon = Icons.Default.Notifications,
                            titleSize = titleSize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) { openSettings(SettingsPage.NOTIFICATIONS) }
                    }
                    item {
                        SettingsCategoryCard(
                            title = "Alarm ve Bildirimler",
                            description = "Alarm yenileme, izin kontrolü ve test alarmı",
                            icon = Icons.Default.NotificationsActive,
                            titleSize = titleSize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) { openSettings(SettingsPage.ALARMS) }
                    }
                    item {
                        SettingsCategoryCard(
                            title = "Randevu Hatırlatıcıları",
                            description = "Randevu alarmları ve varsayılan hatırlatma zamanları",
                            icon = Icons.Default.EventNote,
                            titleSize = titleSize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) { openSettings(SettingsPage.APPOINTMENTS) }
                    }
                    item {
                        SettingsCategoryCard(
                            title = "Yedekleme ve Geri Yükleme",
                            description = "Kişi verilerini dışa aktarın veya yedekten geri yükleyin",
                            icon = Icons.Default.Backup,
                            titleSize = titleSize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) { openSettings(SettingsPage.BACKUP) }
                    }
                    item {
                        SettingsCategoryCard(
                            title = "Güvenlik ve Yetkilendirme",
                            description = "Kullanıcı rolleri, yöneticiler ve yönetici şifreleri",
                            icon = Icons.Default.AdminPanelSettings,
                            titleSize = titleSize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) { openSettings(SettingsPage.SECURITY) }
                    }
                    item {
                        SettingsCategoryCard(
                            title = "Android İzinleri",
                            description = "Bildirim, alarm, pil ve kamera izinlerini tek merkezden yönetin",
                            icon = Icons.Default.Security,
                            titleSize = titleSize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) { openSettings(SettingsPage.PERMISSIONS) }
                    }
                    item {
                        SettingsCategoryCard(
                            title = "Görünüm ve Erişilebilirlik",
                            description = "Ayarlar ekranında büyük yazı ve yüksek kontrast",
                            icon = Icons.Default.AccessibilityNew,
                            titleSize = titleSize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) { openSettings(SettingsPage.ACCESSIBILITY) }
                    }
                    item {
                        SettingsCategoryCard(
                            title = "Tema Ayarları",
                            description = "Renk düzeni, yazı tipi ve yazı büyüklüğü",
                            icon = Icons.Default.Palette,
                            titleSize = titleSize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) { openSettings(SettingsPage.THEME) }
                    }
                    item {
                        SettingsCategoryCard(
                            title = "Veritabanı ve Sistem",
                            description = "Veritabanı sürümü ve veri geçiş durumu",
                            icon = Icons.Default.Storage,
                            titleSize = titleSize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) { openSettings(SettingsPage.DATABASE) }
                    }
                    item {
                        SettingsCategoryCard(
                            title = "Yardım ve Kullanım",
                            description = "Arama yapılabilen kullanım açıklamaları",
                            icon = Icons.Default.HelpOutline,
                            titleSize = titleSize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) { openSettings(SettingsPage.HELP) }
                    }
                    item {
                        SettingsCategoryCard(
                            title = "Uygulama Hakkında",
                            description = "Şifahane sürüm ve uygulama bilgileri",
                            icon = Icons.Default.Info,
                            titleSize = titleSize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) { openSettings(SettingsPage.ABOUT) }
                    }
                }
            }

            SettingsPage.NOTIFICATIONS -> {
                val now = System.currentTimeMillis()
                val sevenDays = 7L * 24L * 60L * 60L * 1000L
                val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("tr", "TR")) }
                val notices = remember(
                    notificationMedications,
                    notificationDoseLogs,
                    notificationAppointments,
                    now / 60_000L
                ) {
                    buildList {
                        notificationMedications
                            .filter { it.active && !it.archived && it.stock <= it.lowStockLimit }
                            .forEach {
                                add(SettingsNotice("stock:${it.id}:${it.stock}", "Düşük stok", "Düşük stok: ${it.name}", "Kalan stok ${it.stock}; tanımlı uyarı sınırı ${it.lowStockLimit}.", now))
                            }
                        notificationAppointments
                            .filter { it.active && AppointmentStatus.fromStorage(it.status) == AppointmentStatus.PLANNED && it.appointmentDateTime in now..(now + sevenDays) }
                            .forEach {
                                add(SettingsNotice("appointment:${it.id}:${it.appointmentDateTime}", "Randevu", "Yaklaşan randevu", listOf(it.doctorName, it.branch, it.institution).filter(String::isNotBlank).joinToString(" · ").ifBlank { "Randevu" }, it.appointmentDateTime))
                            }
                        notificationDoseLogs
                            .filter { DoseAction.fromStorage(it.action) == DoseAction.SKIPPED && it.timestamp >= now - sevenDays }
                            .take(30)
                            .forEach {
                                add(SettingsNotice("dose:${it.id}", "Doz", "İşlem yapılmayan doz", "${it.medicationName} için kayıtlı doz alınmadı. Çift doz yönlendirmesi yapılmaz; gerektiğinde hekim veya eczacıya danışın.", it.timestamp))
                            }
                        notificationMedications
                            .filter { it.isReported && !it.reportEndDate.isNullOrBlank() }
                            .forEach { medication ->
                                val endMillis = runCatching {
                                    java.time.LocalDate.parse(medication.reportEndDate.orEmpty(), java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)
                                        .atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                                }.getOrNull()
                                if (endMillis != null && endMillis in now..(now + medication.reportWarningDays * 24L * 60L * 60L * 1000L)) {
                                    add(SettingsNotice("report:${medication.id}:${medication.reportEndDate}", "Rapor", "Rapor süresi yaklaşıyor", "${medication.name} rapor bitiş tarihi: ${medication.reportEndDate}", endMillis))
                                }
                            }
                    }.sortedByDescending(SettingsNotice::timestamp)
                }
                val readIds = remember(noticeReadRevision) {
                    noticePrefs.getStringSet("read_ids", emptySet()).orEmpty().toSet()
                }
                val visibleNotices = notices.filter { noticeFilter == "Tümü" || it.type == noticeFilter }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Tümü", "Doz", "Randevu", "Düşük stok", "Rapor").forEach { filter ->
                                FilterChip(
                                    selected = noticeFilter == filter,
                                    onClick = { noticeFilter = filter },
                                    label = { Text(filter) }
                                )
                            }
                        }
                    }
                    item {
                        OutlinedButton(
                            onClick = {
                                noticePrefs.edit().putStringSet("read_ids", notices.mapTo(mutableSetOf(), SettingsNotice::id)).apply()
                                noticeReadRevision++
                                Toast.makeText(context, "Tüm bildirimler okundu olarak işaretlendi.", Toast.LENGTH_SHORT).show()
                            },
                            enabled = notices.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        ) { Text("TÜMÜNÜ OKUNDU İŞARETLE") }
                    }
                    if (visibleNotices.isEmpty()) {
                        item {
                            SettingsInformationCard(
                                title = "Bildirim bulunmuyor",
                                body = "Seçili kullanıcı için bu filtrede gösterilecek yeni bir doz, randevu, stok veya rapor uyarısı yok.",
                                titleSize = bodySize,
                                bodySize = supportingSize,
                                cardBackground = cardBackground,
                                borderColor = cardBorder
                            )
                        }
                    } else {
                        items(visibleNotices, key = SettingsNotice::id) { notice ->
                            val read = notice.id in readIds
                            Card(
                                modifier = Modifier.fillMaxWidth().sifahaneSoftBoundary(2.dp),
                                border = BorderStroke(1.dp, if (read) cardBorder.copy(alpha = 0.45f) else cardBorder),
                                colors = CardDefaults.cardColors(containerColor = cardBackground.copy(alpha = if (read) 0.70f else 1f))
                            ) {
                                Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                    Text("${notice.type} · ${if (read) "Okundu" else "Yeni"}", fontWeight = FontWeight.Bold, color = LogoColorDark)
                                    Text(notice.title, fontWeight = FontWeight.Bold)
                                    Text(notice.body)
                                    Text(dateFormat.format(Date(notice.timestamp)), style = MaterialTheme.typography.labelMedium)
                                    if (!read) {
                                        TextButton(
                                            onClick = {
                                                val updated = readIds.toMutableSet().apply { add(notice.id) }
                                                noticePrefs.edit().putStringSet("read_ids", updated).apply()
                                                noticeReadRevision++
                                            },
                                            modifier = Modifier.heightIn(min = 48.dp)
                                        ) { Text("Okundu İşaretle") }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            SettingsPage.ALARMS -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SettingsInformationCard(
                            title = "Alarm erteleme süreleri",
                            body = "Alarm kartındaki üç erteleme seçeneğini dakika olarak belirleyin (1–180).",
                            titleSize = bodySize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().sifahaneSoftBoundary(2.dp),
                            border = BorderStroke(1.dp, cardBorder),
                            colors = CardDefaults.cardColors(containerColor = cardBackground)
                        ) {
                            Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                snoozeValues.forEachIndexed { index, value ->
                                    OutlinedTextField(
                                        value = value,
                                        onValueChange = { input ->
                                            if (input.length <= 3 && input.all(Char::isDigit)) {
                                                snoozeValues = snoozeValues.toMutableList().also { it[index] = input }
                                            }
                                        },
                                        label = { Text("${index + 1}. erteleme süresi (dk)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = standardFieldColors()
                                    )
                                }
                                snoozeError?.let { Text(it, color = Color(0xFFE30A17)) }
                                Button(onClick = {
                                    val parsed = snoozeValues.mapNotNull(String::toIntOrNull)
                                    if (parsed.size != 3 || parsed.any { it !in 1..180 } || parsed.distinct().size != 3) {
                                        snoozeError = "Üç farklı süreyi 1–180 dakika arasında girin."
                                    } else {
                                        AppPreferences.saveSnoozeMinutes(context, parsed)
                                        snoozeError = null
                                        Toast.makeText(context, "Erteleme süreleri kaydedildi.", Toast.LENGTH_SHORT).show()
                                    }
                                }, modifier = Modifier.fillMaxWidth()) { Text("ERTELEME SÜRELERİNİ KAYDET") }
                            }
                        }
                    }
                    item {
                        val durationOptions = listOf(
                            30_000L to "30 saniye", 60_000L to "1 dakika",
                            120_000L to "2 dakika", 180_000L to "3 dakika",
                            300_000L to "5 dakika", 600_000L to "10 dakika"
                        )
                        ExposedDropdownMenuBox(
                            expanded = alarmDurationMenuExpanded,
                            onExpandedChange = { alarmDurationMenuExpanded = !alarmDurationMenuExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = durationOptions.firstOrNull { it.first == alarmRingDuration }?.second ?: "2 dakika",
                                onValueChange = {}, readOnly = true,
                                label = { Text("Alarm çalma süresi") },
                                supportingText = { Text("Süre dolunca bekleyen alarm 5 dakika ertelenir.") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(alarmDurationMenuExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = alarmDurationMenuExpanded,
                                onDismissRequest = { alarmDurationMenuExpanded = false }
                            ) {
                                durationOptions.forEach { (value, label) ->
                                    DropdownMenuItem(text = { Text(label) }, onClick = {
                                        alarmRingDuration = value
                                        AppPreferences.saveAlarmRingDurationMillis(context, value)
                                        alarmDurationMenuExpanded = false
                                        Toast.makeText(context, "Alarm çalma süresi kaydedildi.", Toast.LENGTH_SHORT).show()
                                    })
                                }
                            }
                        }
                    }
                    item {
                        AccessibleSettingsButton(
                            text = if (alarmRefreshBusy) "Alarmlar Yenileniyor" else "Alarmları Yenile",
                            description = "Aktif ilaçların alarm planını yeniden oluşturur.",
                            icon = Icons.Default.Refresh,
                            enabled = !alarmRefreshBusy,
                            titleSize = bodySize,
                            supportingSize = supportingSize,
                            onClick = onAlarmRefresh
                        )
                    }
                    item {
                        AccessibleSettingsButton(
                            text = if (alarmRefreshBusy) "Alarm İstekleri İşleniyor" else "Önceden Kalmış Alarm İsteklerini Sil",
                            description = "Temizlenecek kayıtlı istek sayısını gösterir; ilaç, randevu ve deneme alarmlarını temizleyip geçerli planları yeniden kurar.",
                            icon = Icons.Default.DeleteSweep,
                            enabled = !alarmRefreshBusy,
                            titleSize = bodySize,
                            supportingSize = supportingSize,
                            onClick = { confirmClearAlarms = true }
                        )
                    }
                    item {
                        AccessibleSettingsButton(
                            text = "Alarm Durumunu Kontrol Et",
                            description = "Bildirim, kesin alarm ve tam ekran alarm izinlerini gösterir.",
                            icon = Icons.Default.NotificationsActive,
                            titleSize = bodySize,
                            supportingSize = supportingSize,
                            onClick = onAlarmStatus
                        )
                    }
                    item {
                        AccessibleSettingsButton(
                            text = "10 Saniye Sonra Test Alarmı",
                            description = "Alarm ekranını ve sesini kısa süre içinde denemenizi sağlar.",
                            icon = Icons.Default.Timer,
                            titleSize = bodySize,
                            supportingSize = supportingSize,
                            onClick = onTestAlarm
                        )
                    }
                    alarmRefreshResult?.let { result ->
                        item {
                            SettingsInformationCard(
                                title = "Son alarm yenileme sonucu",
                                body = "Aktif ilaç: ${result.medicationCount}\nGelecek alarm: ${result.futureAlarmCount}\nGeçmiş yanıtsız doz: ${result.catchUpAlarmCount}",
                                titleSize = bodySize,
                                bodySize = supportingSize,
                                cardBackground = cardBackground,
                                borderColor = cardBorder
                            )
                        }
                    }
                }
            }

            SettingsPage.APPOINTMENTS -> {
                var alarmsEnabled by remember {
                    mutableStateOf(AppointmentPreferences.alarmsEnabled(context))
                }
                var defaultReminderSet by remember {
                    mutableStateOf(
                        AppointmentPreferences.defaultRemindersCsv(context)
                            .split(",").mapNotNull { it.toIntOrNull() }.toSet()
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        AccessibilitySwitchCard(
                            title = "Randevu alarmları",
                            description = "Kayıtlı tüm kullanıcıların yaklaşan randevu alarmlarını etkinleştirir.",
                            checked = alarmsEnabled,
                            titleSize = bodySize,
                            supportingSize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) {
                            alarmsEnabled = it
                            AppointmentPreferences.setAlarmsEnabled(context, it)
                            kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
                                dbAppointmentRefresh(context)
                            }
                        }
                    }
                    item {
                        SettingsInformationCard(
                            title = "Yeni randevular için varsayılan hatırlatmalar",
                            body = "Seçimler yeni oluşturulan randevulara uygulanır. Her randevuda ayrıca değiştirilebilir.",
                            titleSize = bodySize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().sifahaneSoftBoundary(2.dp),
                            border = BorderStroke(1.dp, cardBorder),
                            colors = CardDefaults.cardColors(containerColor = cardBackground)
                        ) {
                            Column(Modifier.fillMaxWidth().padding(12.dp)) {
                                listOf(
                                    10080 to "1 hafta önce",
                                    4320 to "3 gün önce",
                                    1440 to "1 gün önce",
                                    180 to "3 saat önce",
                                    60 to "1 saat önce"
                                ).forEach { (minutes, label) ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().clickable {
                                            defaultReminderSet = if (minutes in defaultReminderSet)
                                                defaultReminderSet - minutes else defaultReminderSet + minutes
                                            AppointmentPreferences.setDefaultRemindersCsv(
                                                context,
                                                defaultReminderSet.sortedDescending().joinToString(",")
                                            )
                                        },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = minutes in defaultReminderSet,
                                            onCheckedChange = {
                                                defaultReminderSet = if (minutes in defaultReminderSet)
                                                    defaultReminderSet - minutes else defaultReminderSet + minutes
                                                AppointmentPreferences.setDefaultRemindersCsv(
                                                    context,
                                                    defaultReminderSet.sortedDescending().joinToString(",")
                                                )
                                            }
                                        )
                                        Text(label, fontSize = bodySize)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            SettingsPage.BACKUP -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        AccessibleSettingsButton(
                            text = "Kişi Verilerini Dışarı Aktar",
                            description = "Aktif kişiye ait verileri ve fotoğrafları parola korumalı .sifbak dosyasına kaydeder.",
                            icon = Icons.Default.FileUpload,
                            enabled = hasActiveProfile,
                            titleSize = bodySize,
                            supportingSize = supportingSize,
                            onClick = onExportPersonData
                        )
                    }
                    item {
                        AccessibleSettingsButton(
                            text = "Kişi Verilerini İçeri Aktar",
                            description = if (canManageSecurity)
                                "Şifreli .sifbak veya uyumlu eski ZIP yedeğini doğrulayarak içeri aktarır."
                            else
                                "Bu işlem yalnızca yöneticiler tarafından yapılabilir.",
                            icon = Icons.Default.FileDownload,
                            enabled = canManageSecurity,
                            titleSize = bodySize,
                            supportingSize = supportingSize,
                            onClick = onImportPersonData
                        )
                    }
                    item {
                        AccessibleSettingsButton(
                            text = if (restorePointBusy) "Geri Yükleme Noktası İşleniyor" else "Geri Yükleme Noktası Oluştur",
                            description = "Aktif kişinin mevcut verilerini cihaz içinde şifreli bir geri dönüş noktası olarak saklar.",
                            icon = Icons.Default.Restore,
                            enabled = hasActiveProfile && !restorePointBusy,
                            titleSize = bodySize,
                            supportingSize = supportingSize,
                            onClick = {
                                restorePointBusy = true
                                restorePointStatus = ""
                                settingsScope.launch {
                                    runCatching {
                                        withContext(Dispatchers.IO) {
                                            RestorePointManager.create(context, settingsDb, activeProfileId)
                                        }
                                    }.onSuccess {
                                        restorePointRevision++
                                        restorePointStatus = "Geri yükleme noktası oluşturuldu."
                                    }.onFailure {
                                        restorePointStatus = "Geri yükleme noktası oluşturulamadı: ${it.message ?: "Bilinmeyen hata"}"
                                    }
                                    restorePointBusy = false
                                }
                            }
                        )
                    }
                    item {
                        SettingsInformationCard(
                            title = "Kullanıcı geri yükleme noktaları",
                            body = "Kullanıcı başına en fazla ${RestorePointManager.MAX_POINTS} şifreli nokta tutulur. Geri dönmeden hemen önce otomatik güvenlik noktası oluşturulur. Geri yükleme iki ayrı onay gerektirir.",
                            titleSize = bodySize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                    if (restorePointStatus.isNotBlank()) {
                        item {
                            Text(
                                restorePointStatus,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                                textAlign = TextAlign.Center,
                                color = if (restorePointStatus.startsWith("Geri yükleme noktası oluşturuldu") || restorePointStatus.startsWith("Geri yükleme tamamlandı")) LogoColorDark else MaterialTheme.colorScheme.error,
                                fontSize = supportingSize
                            )
                        }
                    }
                    if (restorePoints.isEmpty()) {
                        item {
                            Text(
                                "Henüz geri yükleme noktası yok.",
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                textAlign = TextAlign.Center,
                                fontSize = supportingSize
                            )
                        }
                    } else {
                        items(restorePoints, key = { it.file.absolutePath }) { point ->
                            val pointLabel = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale("tr", "TR")).format(Date(point.createdAt))
                            Card(
                                modifier = Modifier.fillMaxWidth().sifahaneSoftBoundary(2.dp),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, cardBorder),
                                colors = CardDefaults.cardColors(containerColor = cardBackground)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        if (point.automatic) "Otomatik güvenlik noktası" else "Kullanıcı geri yükleme noktası",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = bodySize
                                    )
                                    Text(pointLabel, fontSize = supportingSize)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            enabled = !restorePointBusy,
                                            onClick = {
                                                restorePointBusy = true
                                                settingsScope.launch {
                                                    runCatching {
                                                        withContext(Dispatchers.IO) { RestorePointManager.preview(context, point) }
                                                    }.onSuccess { restorePreview = point to it }
                                                        .onFailure { restorePointStatus = "Geri yükleme noktası okunamadı: ${it.message ?: "Bilinmeyen hata"}" }
                                                    restorePointBusy = false
                                                }
                                            },
                                            modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                                        ) { Text("İNCELE") }
                                        TextButton(
                                            enabled = !restorePointBusy,
                                            onClick = { deleteRestorePoint = point },
                                            modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                                        ) { Text("SİL", color = MaterialTheme.colorScheme.error) }
                                    }
                                }
                            }
                        }
                    }
                    item {
                        SettingsInformationCard(
                            title = "Yedekleme bilgisi",
                            body = buildString {
                                append("Yeni yedekler fotoğraf ve kayıtları parola korumalı AES-GCM .sifbak kapsayıcısında saklar. Eski standart ZIP yedekleri yalnız geriye dönük içe aktarma için desteklenir.")
                                if (backupStatusText.isNotBlank()) {
                                    append("\n\n")
                                    append(backupStatusText)
                                }
                            },
                            titleSize = bodySize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                }
            }

            SettingsPage.SECURITY -> {
                val active = profiles.firstOrNull { it.id == activeProfileId }
                val biometricEnabled = active?.let {
                    biometricPreferenceRevision
                    BiometricPreferences.isEnabled(context, it.id)
                } == true
                val biometricActivity = context as? FragmentActivity
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        SettingsInformationCard(
                            title = "Otomatik ekran kilidi",
                            body = "Uygulamada işlem yapılmadığında aktif kullanıcı yeniden desen kilidine alınır.",
                            titleSize = bodySize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                    item {
                        val lockOptions = listOf(
                            30_000L to "30 saniye", 60_000L to "1 dakika",
                            120_000L to "2 dakika", 300_000L to "5 dakika",
                            600_000L to "10 dakika", 1_800_000L to "30 dakika",
                            0L to "Hiçbir zaman"
                        )
                        ExposedDropdownMenuBox(
                            expanded = lockTimeoutMenuExpanded,
                            onExpandedChange = { lockTimeoutMenuExpanded = !lockTimeoutMenuExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = lockOptions.firstOrNull { it.first == lockTimeout }?.second ?: "2 dakika",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Hareketsizlik sonrası kilitle") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(lockTimeoutMenuExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = lockTimeoutMenuExpanded,
                                onDismissRequest = { lockTimeoutMenuExpanded = false }
                            ) {
                                lockOptions.forEach { (value, label) ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = {
                                            lockTimeout = value
                                            AppPreferences.saveLockTimeoutMillis(context, value)
                                            lockTimeoutMenuExpanded = false
                                            Toast.makeText(context, "Otomatik kilit süresi kaydedildi.", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            }
                        }
                    }
                    item {
                        SifahaneCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Biyometrik giriş", fontSize = titleSize, fontWeight = FontWeight.Bold)
                                Text(
                                    if (active == null) "Önce bir kullanıcı seçin."
                                    else if (biometricEnabled) "${active.name} için biyometrik giriş açık. Kilit ekranındaki Biyometrik Giriş düğmesi kullanılabilir."
                                    else "Başlangıçta kapalıdır. Açmak için sistem biyometri penceresinde doğrulama gerekir.",
                                    fontSize = supportingSize
                                )
                                OutlinedButton(
                                    enabled = active != null,
                                    onClick = {
                                        val profile = active ?: return@OutlinedButton
                                        if (biometricEnabled) {
                                            BiometricPreferences.setEnabled(context, profile.id, false)
                                            biometricPreferenceRevision++
                                            Toast.makeText(context, "Biyometrik giriş kapatıldı.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            when {
                                                biometricActivity == null -> Toast.makeText(context, "Biyometri bu ekranda başlatılamadı.", Toast.LENGTH_SHORT).show()
                                                biometricAvailability(context) != BiometricManager.BIOMETRIC_SUCCESS -> Toast.makeText(context, "Cihazda kullanılabilir biyometri bulunamadı veya tanımlanmadı.", Toast.LENGTH_LONG).show()
                                                else -> startBiometricAuthentication(
                                                    activity = biometricActivity,
                                                    profileName = "${profile.name} ${profile.surname}".trim(),
                                                    onSuccess = {
                                                        BiometricPreferences.setEnabled(context, profile.id, true)
                                                        biometricPreferenceRevision++
                                                        Toast.makeText(context, "Biyometrik giriş etkinleştirildi.", Toast.LENGTH_SHORT).show()
                                                    },
                                                    onMessage = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
                                ) {
                                    Icon(Icons.Default.Fingerprint, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text(if (biometricEnabled) "BİYOMETRİK GİRİŞİ KAPAT" else "BİYOMETRİK GİRİŞİ ETKİNLEŞTİR")
                                }
                            }
                        }
                    }
                    item {
                        SettingsInformationCard(
                            title = "Aktif kullanıcı rolü",
                            body = if (active?.role == UserRoles.ADMIN) "Yönetici" else "Standart Kullanıcı",
                            titleSize = bodySize,
                            bodySize = bodySize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }

                    if (canManageSecurity) {
                        items(profiles, key = { it.id }) { profile ->
                            Card(
                                modifier = Modifier.fillMaxWidth().sifahaneSoftBoundary(2.dp),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, cardBorder),
                                colors = CardDefaults.cardColors(containerColor = cardBackground)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        "${profile.name} ${profile.surname}".trim(),
                                        modifier = Modifier.fillMaxWidth(),
                                        fontSize = titleSize,
                                        fontWeight = FontWeight.Bold,
                                        color = LogoColorDark
                                    )
                                    Text(
                                        if (profile.role == UserRoles.ADMIN) "Yönetici" else "Standart Kullanıcı",
                                        modifier = Modifier.fillMaxWidth(),
                                        fontSize = supportingSize,
                                        color = LogoColorDark
                                    )
                                    OutlinedButton(
                                        onClick = { onSetRole(profile.id, profile.role != UserRoles.ADMIN) },
                                        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
                                    ) {
                                        Text(
                                            if (profile.role == UserRoles.ADMIN)
                                                "Standart Kullanıcı Yap"
                                            else
                                                "Yönetici Olarak Ata",
                                            fontSize = bodySize
                                        )
                                    }
                                    if (profile.role == UserRoles.ADMIN) {
                                        OutlinedButton(
                                            onClick = {
                                                pinTarget = profile
                                                newAdminPin = ""
                                                confirmAdminPin = ""
                                                pinError = null
                                            },
                                            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
                                        ) {
                                            Text(
                                                if (profile.adminPinHash.isNullOrBlank())
                                                    "Yönetici Şifresi Belirle"
                                                else
                                                    "Yönetici Şifresini Değiştir",
                                                fontSize = bodySize
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Text(
                                "Son yönetici kaldırılamaz. Yönetici şifreleri benzersiz salt ve yavaş PBKDF2 özetiyle saklanır.",
                                modifier = Modifier.fillMaxWidth().padding(6.dp),
                                textAlign = TextAlign.Center,
                                color = LogoColorDark,
                                fontSize = supportingSize
                            )
                        }
                    } else {
                        item {
                            SettingsInformationCard(
                                title = "Yetki gerekli",
                                body = "Rol ve yönetici hesaplarını değiştirmek için yönetici hesabıyla oturum açın.",
                                titleSize = bodySize,
                                bodySize = supportingSize,
                                cardBackground = cardBackground,
                                borderColor = cardBorder
                            )
                        }
                    }
                }
            }

            SettingsPage.PERMISSIONS -> {
                val ignoredRefresh = permissionRefresh
                val notificationGranted = Build.VERSION.SDK_INT < 33 ||
                    androidx.core.content.ContextCompat.checkSelfPermission(
                        context, android.Manifest.permission.POST_NOTIFICATIONS
                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                val cameraGranted = androidx.core.content.ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.CAMERA
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                val alarmManager = context.getSystemService(AlarmManager::class.java)
                val exactGranted = Build.VERSION.SDK_INT < 31 || alarmManager.canScheduleExactAlarms()
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                val fullScreenGranted = Build.VERSION.SDK_INT < 34 || notificationManager.canUseFullScreenIntent()
                val powerManager = context.getSystemService(android.os.PowerManager::class.java)
                val batteryGranted = powerManager.isIgnoringBatteryOptimizations(context.packageName)
                fun openAppSettings() {
                    context.startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    })
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        SettingsInformationCard(
                            title = "Android izinleri merkezi",
                            body = "Android bazı özel izinleri uygulama içinden doğrudan değiştirmeye izin vermez. Bu izinlerde ilgili güvenli sistem ekranı açılır.",
                            titleSize = bodySize, bodySize = supportingSize,
                            cardBackground = cardBackground, borderColor = cardBorder
                        )
                    }
                    item { PermissionStatusCard("Bildirimler", "Alarm ve hatırlatma bildirimlerini gösterir.", notificationGranted, cardBackground, cardBorder) {
                        if (Build.VERSION.SDK_INT >= 33) notificationPermissionRequest.launch(android.Manifest.permission.POST_NOTIFICATIONS) else openAppSettings()
                    } }
                    item { PermissionStatusCard("Kesin alarm", "İlaç alarmını belirlenen dakikada çalıştırır.", exactGranted, cardBackground, cardBorder) {
                        if (Build.VERSION.SDK_INT >= 31) context.startActivity(Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:${context.packageName}")))
                    } }
                    item { PermissionStatusCard("Tam ekran alarm", "Kilitli veya açık ekranda alarm kartını gösterebilir.", fullScreenGranted, cardBackground, cardBorder) {
                        if (Build.VERSION.SDK_INT >= 34) context.startActivity(Intent(android.provider.Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT, Uri.parse("package:${context.packageName}")))
                    } }
                    item { PermissionStatusCard("Pil optimizasyonu muafiyeti", "Üretici güç yönetiminin alarmı geciktirmesini önlemeye yardımcı olur.", batteryGranted, cardBackground, cardBorder) {
                        context.startActivity(Intent(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:${context.packageName}")))
                    } }
                    item { PermissionStatusCard("Kamera", "İlaç barkodu taraması için kullanılır.", cameraGranted, cardBackground, cardBorder) {
                        cameraPermissionRequest.launch(android.Manifest.permission.CAMERA)
                    } }
                    item {
                        PermissionStatusCard(
                            title = "Dosya ve medya",
                            description = "Yedek, rapor ve fotoğraf işlemleri Android sistem dosya/fotoğraf seçicisiyle yapılır. Şifahane geniş depolama izni istemez.",
                            granted = true,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                    item {
                        OutlinedButton(
                            onClick = { openAppSettings() },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("SİSTEM AYARLARINA GİT")
                        }
                        Text(
                            "Bildirim ve uygulama düzeyindeki izinleri tek ekrandan incelemek için Android sistem ayarlarını açar. Kesin alarm, tam ekran alarm ve pil optimizasyonu gibi özel erişimler yukarıdaki ilgili kartlardan yönetilir.",
                            fontSize = supportingSize
                        )
                    }
                }
            }

            SettingsPage.ACCESSIBILITY -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        AccessibilitySwitchCard(
                            title = "Büyük ayar metinleri",
                            description = "Ayarlar bölümündeki başlık ve açıklama yazılarını büyütür.",
                            checked = largeSettingsText,
                            titleSize = bodySize,
                            supportingSize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) {
                            largeSettingsText = it
                            accessibilityPrefs.edit().putBoolean("large_settings_text", it).apply()
                        }
                    }
                    item {
                        AccessibilitySwitchCard(
                            title = "Yüksek kontrastlı ayarlar",
                            description = "Ayar kartlarının zemin ve kenar ayrımını belirginleştirir.",
                            checked = highContrastSettings,
                            titleSize = bodySize,
                            supportingSize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) {
                            highContrastSettings = it
                            accessibilityPrefs.edit().putBoolean("high_contrast_settings", it).apply()
                        }
                    }
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().sifahaneSoftBoundary(2.dp),
                            border = BorderStroke(1.dp, cardBorder),
                            colors = CardDefaults.cardColors(containerColor = cardBackground)
                        ) {
                            Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Alt menü buton boyutu", fontSize = bodySize, fontWeight = FontWeight.Bold, color = LogoColorDark)
                                Text("Alt menü simgeleri, yazıları ve menü yüksekliği için standart boyutu seçin.",
                                    fontSize = supportingSize, color = LogoColorDark.copy(alpha = 0.82f))
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf(BottomMenuSize.SMALL to "Küçük", BottomMenuSize.MEDIUM to "Orta", BottomMenuSize.LARGE to "Büyük")
                                        .forEach { (size, title) ->
                                            if (bottomMenuSize == size) Button(
                                                onClick = { onBottomMenuSizeChange(size) },
                                                modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                                            ) { Text(title, maxLines = 1) }
                                            else OutlinedButton(
                                                onClick = { onBottomMenuSizeChange(size) },
                                                modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                                            ) { Text(title, maxLines = 1) }
                                        }
                                }
                                OutlinedButton(onClick = onResetBottomMenuOrder,
                                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                                    Icon(Icons.Default.Restore, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Alt menüyü varsayılan sıraya döndür")
                                }
                            }
                        }
                    }
                    item {
                        SettingsInformationCard(
                            title = "Erişilebilir tasarım",
                            body = "Ayar kategorileri ayrı ekranlara bölünmüştür. Alt menü yatay kaydırılabilir, alt menüdeki Menü düğmesinden sürükle-bırak düzenlenebilir ve Küçük, Orta veya Büyük boyutta kullanılabilir. İşlem alanları en az 48 dp olacak şekilde korunur.",
                            titleSize = bodySize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                }
            }

            SettingsPage.THEME -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SettingsInformationCard(
                            title = "Tema ve okunabilirlik",
                            body = "Açık, standart koyu, OLED saf siyah veya sistem görünümünü seçebilirsiniz. Dinamik renk kapalıyken Şifahane logo yeşili korunur. Sistem yazı ölçeği ayrıca uygulanır.",
                            titleSize = bodySize, bodySize = supportingSize,
                            cardBackground = cardBackground, borderColor = cardBorder
                        )
                    }
                    item {
                        val modeOptions = listOf(
                            ThemeMode.SYSTEM to "Sistem ayarını kullan",
                            ThemeMode.LIGHT to "Açık",
                            ThemeMode.DARK to "Standart koyu",
                            ThemeMode.OLED to "OLED saf siyah"
                        )
                        ExposedDropdownMenuBox(
                            expanded = themeModeExpanded,
                            onExpandedChange = { themeModeExpanded = !themeModeExpanded }
                        ) {
                            OutlinedTextField(
                                value = modeOptions.first { it.first == themeMode }.second,
                                onValueChange = {}, readOnly = true,
                                label = { Text("Görünüm modu") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(themeModeExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(themeModeExpanded, { themeModeExpanded = false }) {
                                modeOptions.forEach { (value, label) ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = { themeMode = value; themeModeExpanded = false }
                                    )
                                }
                            }
                        }
                    }
                    item {
                        val colorOptions = listOf(
                            ThemePreset.CALM to "Sakin",
                            ThemePreset.VIVID to "Canlı",
                            ThemePreset.HIGH_CONTRAST to "Yüksek Kontrast"
                        )
                        ExposedDropdownMenuBox(
                            expanded = themePresetExpanded,
                            onExpandedChange = { themePresetExpanded = !themePresetExpanded }
                        ) {
                            OutlinedTextField(
                                value = colorOptions.first { it.first == themePreset }.second,
                                onValueChange = {}, readOnly = true,
                                label = { Text("Renk görünümü") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(themePresetExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(themePresetExpanded, { themePresetExpanded = false }) {
                                colorOptions.forEach { (value, label) ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = { themePreset = value; themePresetExpanded = false }
                                    )
                                }
                            }
                        }
                    }
                    item {
                        AccessibilitySwitchCard(
                            title = "Android dinamik renkleri",
                            description = "Android 12 ve üzerindeki cihazlarda sistem duvar kâğıdı renklerini kullanır. Kapalıyken Şifahane kurumsal paleti uygulanır.",
                            checked = dynamicColorEnabled,
                            titleSize = bodySize,
                            supportingSize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        ) { dynamicColorEnabled = it }
                    }
                    item {
                        val fontOptions = listOf(
                            ThemeFont.ROBOTO to "Roboto",
                            ThemeFont.NOTO_SANS to "Noto Sans",
                            ThemeFont.ATKINSON to "Atkinson Hyperlegible",
                            ThemeFont.LEXEND to "Lexend",
                            ThemeFont.NOTO_SERIF to "Noto Serif"
                        )
                        ExposedDropdownMenuBox(
                            expanded = themeFontExpanded,
                            onExpandedChange = { themeFontExpanded = !themeFontExpanded }
                        ) {
                            OutlinedTextField(
                                value = fontOptions.first { it.first == themeFont }.second,
                                onValueChange = {}, readOnly = true,
                                label = { Text("Yazı tipi") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(themeFontExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(themeFontExpanded, { themeFontExpanded = false }) {
                                fontOptions.forEach { (value, label) ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = { themeFont = value; themeFontExpanded = false }
                                    )
                                }
                            }
                        }
                    }
                    item {
                        SettingsSectionCard("Ek yazı büyüklüğü: %${(themeScale * 100).roundToInt()}") {
                            Slider(
                                value = themeScale,
                                onValueChange = { themeScale = it },
                                valueRange = .85f..2.0f,
                                steps = 8
                            )
                            Text(
                                "Bu değer Android'in sistem yazı büyüklüğüne ek olarak uygulanır; sistem erişilebilirlik ölçeğini sınırlandırmaz.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    item {
                        SettingsSectionCard("Vurgu opaklığı: %${(themeOpacity * 100).roundToInt()}") {
                            Slider(value = themeOpacity, onValueChange = { themeOpacity = it }, valueRange = .35f..1f)
                        }
                    }
                    item {
                        SettingsSectionCard("Kart iç vurgusu: %${(cardInnerAccentOpacity * 100).roundToInt()}") {
                            Slider(
                                value = cardInnerAccentOpacity,
                                onValueChange = { cardInnerAccentOpacity = it },
                                valueRange = 0.10f..0.70f
                            )
                        }
                    }
                    item {
                        Button(
                            onClick = {
                                ThemePreferences.save(
                                    context,
                                    ThemeConfiguration(
                                        mode = themeMode,
                                        preset = themePreset,
                                        font = themeFont,
                                        fontScale = themeScale,
                                        accentOpacity = themeOpacity,
                                        cardInnerAccentOpacity = cardInnerAccentOpacity,
                                        dynamicColor = dynamicColorEnabled
                                    )
                                )
                                (context as? Activity)?.recreate()
                            },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        ) { Text("TEMAYI UYGULA") }
                    }
                    item {
                        OutlinedButton(
                            onClick = {
                                ThemePreferences.reset(context)
                                (context as? Activity)?.recreate()
                            },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        ) { Text("VARSAYILAN TEMAYA DÖN") }
                    }
                }
            }

            SettingsPage.DATABASE -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SettingsInformationCard(
                            title = "Room veritabanı sürümü",
                            body = databaseVersion.toString(),
                            titleSize = bodySize,
                            bodySize = titleSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                    item {
                        SettingsInformationCard(
                            title = "Veri geçiş durumu",
                            body = "Desteklenen eski veritabanları adım adım güncellenir. Profil ilişkileri foreign key ile korunur; sık kullanılan ilaç, doz, ölçüm, rapor grubu ve randevu sorguları indekslidir. Geçiş sırasında yalnız gerçekten yetim kayıtlar güvenli biçimde ayıklanır.",
                            titleSize = bodySize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                }
            }

            SettingsPage.HELP -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                            placeholder = { Text("Yardım konularında ara", fontSize = bodySize) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            singleLine = true,
                            shape = StandardFieldShape,
                            colors = standardFieldColors()
                        )
                    }
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().sifahaneSoftBoundary(2.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, cardBorder),
                            colors = CardDefaults.cardColors(containerColor = cardBackground)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "Kapsamlı Kullanıcı Kılavuzu",
                                    color = LogoColorDark,
                                    fontSize = bodySize,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Uygulamanın tüm temel işlevlerini tek belgede okuyun veya kişisel veri içermeyen erişilebilir Word belgesini bir yapay zekâ uygulamasıyla paylaşın.",
                                    color = LogoColorDark,
                                    fontSize = supportingSize
                                )
                                OutlinedButton(
                                    onClick = { showComprehensiveGuide = true },
                                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                                ) {
                                    Icon(Icons.Default.MenuBook, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("KILAVUZU OKU VE ARA", fontSize = bodySize)
                                }
                                Button(
                                    onClick = {
                                        val plainGuide = comprehensiveUserGuideSections()
                                            .joinToString("\n\n") { (title, body) -> "$title\n$body" }
                                        runCatching {
                                            val guide = createComprehensiveUserGuide(context)
                                            val uri = FileProvider.getUriForFile(
                                                context, "${context.packageName}.fileprovider", guide
                                            )
                                            context.startActivity(Intent.createChooser(
                                                Intent(Intent.ACTION_SEND).apply {
                                                    type = "*/*"
                                                    putExtra(Intent.EXTRA_STREAM, uri)
                                                    putExtra(Intent.EXTRA_SUBJECT, "Şifahane Kapsamlı Kullanıcı Kılavuzu")
                                                    putExtra(Intent.EXTRA_TEXT, "Bu kılavuzu esas alarak Şifahane uygulamasıyla ilgili sorularımı Türkçe cevapla.\n\n$plainGuide")
                                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                    clipData = android.content.ClipData.newUri(
                                                        context.contentResolver, guide.name, uri
                                                    )
                                                },
                                                "Kılavuzu yapay zekâ ile paylaş"
                                            ))
                                        }.onFailure {
                                            context.startActivity(Intent.createChooser(
                                                Intent(Intent.ACTION_SEND).apply {
                                                    type = "text/plain"
                                                    putExtra(Intent.EXTRA_SUBJECT, "Şifahane Kapsamlı Kullanıcı Kılavuzu")
                                                    putExtra(Intent.EXTRA_TEXT, plainGuide)
                                                }, "Kılavuzu yapay zekâ ile paylaş"
                                            ))
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                                ) {
                                    Icon(Icons.Default.Share, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("YAPAY ZEKÂYA SOR", fontSize = bodySize)
                                }
                            }
                        }
                    }
                    items(filteredHelp, key = { it.title }) { topic ->
                        val expanded = expandedHelpTitle == topic.title
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .sifahaneSoftBoundary(2.dp)
                                .clickable {
                                    expandedHelpTitle = if (expanded) null else topic.title
                                },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, cardBorder),
                            colors = CardDefaults.cardColors(containerColor = cardBackground)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        topic.title,
                                        modifier = Modifier.weight(1f),
                                        color = LogoColorDark,
                                        fontSize = bodySize,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = if (expanded) "Açıklamayı kapat" else "Açıklamayı aç",
                                        tint = LogoColorDark
                                    )
                                }
                                if (expanded) {
                                    Text(
                                        topic.body,
                                        modifier = Modifier.fillMaxWidth(),
                                        color = LogoColorDark,
                                        fontSize = supportingSize
                                    )
                                    if (topic.target != null && topic.targetLabel != null) {
                                        OutlinedButton(
                                            onClick = { onNavigate(topic.target) },
                                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                                        ) {
                                            Text(topic.targetLabel, fontSize = bodySize)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (filteredHelp.isEmpty()) {
                        item {
                            Text(
                                "Aramanızla eşleşen yardım konusu bulunamadı.",
                                modifier = Modifier.fillMaxWidth().padding(18.dp),
                                textAlign = TextAlign.Center,
                                color = LogoColorDark,
                                fontSize = bodySize
                            )
                        }
                    }
                }
            }

            SettingsPage.ABOUT -> {
                val supportReport = remember(permissionRefresh) { SupportDiagnostics.create(context) }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SettingsInformationCard(
                            title = "Şifahane",
                            body = "Sağlık Takip Uygulaması\nVersion Name: ${BuildConfig.VERSION_NAME}\nVersion Code: ${BuildConfig.VERSION_CODE}\nDerleme türü: ${BuildConfig.BUILD_TYPE}\nDerleme zamanı: ${if (BuildConfig.BUILD_TIME_MILLIS == 0L) "Tekrarlanabilir derleme" else SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("tr", "TR")).format(Date(BuildConfig.BUILD_TIME_MILLIS))}",
                            titleSize = titleSize,
                            bodySize = bodySize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                    item {
                        SettingsInformationCard(
                            title = "Tıbbi kapsam ve güvenli kullanım",
                            body = "Şifahane; ilaç, ölçüm ve randevu kayıtlarını takip eden çevrimdışı bir yardımcıdır. Tanı koymaz, tedavi veya doz değişikliği önermez ve acil durum hizmeti değildir. Kaçırılan dozda çift doz almayın; prospektüsü izleyin ve hekim ya da eczacınıza danışın. Acil durumda 112'yi arayın.",
                            titleSize = bodySize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = MaterialTheme.colorScheme.error.copy(alpha = .65f)
                        )
                    }
                    item {
                        OutlinedButton(
                            onClick = {
                                val buildTime = if (BuildConfig.BUILD_TIME_MILLIS == 0L) "Tekrarlanabilir derleme" else SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("tr", "TR")).format(Date(BuildConfig.BUILD_TIME_MILLIS))
                                val info = "Şifahane ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE}) - ${BuildConfig.BUILD_TYPE} - $buildTime"
                                context.getSystemService(android.content.ClipboardManager::class.java)
                                    .setPrimaryClip(android.content.ClipData.newPlainText("Şifahane sürüm bilgisi", info))
                            },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        ) { Text("SÜRÜM BİLGİSİNİ KOPYALA") }
                    }
                    item {
                        SettingsInformationCard(
                            title = "Açık kaynak ve üçüncü taraf bildirimleri",
                            body = "AndroidX, Jetpack Compose, Navigation, Room, CameraX, ExifInterface, Biometric ve Kotlin: Apache License 2.0. Coil: Apache License 2.0. SQLCipher Community: BSD tarzı açık kaynak lisansı. Roboto, Noto Sans, Noto Serif, Atkinson Hyperlegible ve Lexend yazı tipleri: SIL Open Font License 1.1. ML Kit barkod tarama bileşeni Google hizmet koşullarına tabidir. Tam yazı tipi lisans metinleri uygulama paketinin assets/font_licenses klasöründedir.",
                            titleSize = bodySize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                    item {
                        SettingsInformationCard(
                            title = "Kişisel verisiz destek tanılaması",
                            body = "Aşağıdaki raporda ad, ilaç, doktor, ölçüm veya dosya yolu bulunmaz. Yalnız siz kopyalar veya paylaşırsanız cihazdan çıkar.",
                            titleSize = bodySize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                    item {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = {
                                    context.getSystemService(android.content.ClipboardManager::class.java)
                                        .setPrimaryClip(android.content.ClipData.newPlainText("Şifahane tanılaması", supportReport))
                                    android.widget.Toast.makeText(context, "Tanılama panoya kopyalandı.", android.widget.Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f).heightIn(min = 52.dp)
                            ) { Text("KOPYALA") }
                            Button(
                                onClick = {
                                    context.startActivity(
                                        Intent.createChooser(
                                            Intent(Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(Intent.EXTRA_SUBJECT, "Şifahane kişisel verisiz tanılama")
                                                putExtra(Intent.EXTRA_TEXT, supportReport)
                                            },
                                            "Tanılamayı paylaş"
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f).heightIn(min = 52.dp)
                            ) { Text("PAYLAŞ") }
                        }
                    }
                    item {
                        SettingsInformationCard(
                            title = "Bu sürümde",
                            body = "İlk kullanıcı güvenliği, profil doğrulaması, biyometri, şifreli veritabanı ve yedek, alarm yaşam döngüsü, kaydırılabilir tanılama, randevu kartları, tema/erişilebilirlik ve veri bütünlüğü katmanları yenilendi.",
                            titleSize = bodySize,
                            bodySize = supportingSize,
                            cardBackground = cardBackground,
                            borderColor = cardBorder
                        )
                    }
                }
            }
        }
    }

    restorePreview?.let { (point, preview) ->
        AlertDialog(
            onDismissRequest = {
                if (!restorePointBusy) {
                    restorePreview = null
                    restoreConfirmStep = 0
                }
            },
            title = {
                Text(
                    "Geri yükleme noktasını incele",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Kişi: ${preview.profileName}")
                    Text("İlaç: ${preview.medicationCount}")
                    Text("Doz geçmişi: ${preview.doseLogCount}")
                    Text("Tansiyon: ${preview.bloodPressureCount}")
                    Text("Kan şekeri: ${preview.glucoseCount}")
                    Text("Randevu: ${preview.appointmentCount}")
                    Text("Fotoğraf: ${preview.photoCount}")
                    Text("Oluşturulma: ${SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale("tr", "TR")).format(Date(point.createdAt))}")
                }
            },
            confirmButton = {
                Button(
                    enabled = !restorePointBusy,
                    onClick = { restoreConfirmStep = 1 }
                ) { Text("GERİ DÖN") }
            },
            dismissButton = {
                TextButton(
                    enabled = !restorePointBusy,
                    onClick = {
                        restorePreview = null
                        restoreConfirmStep = 0
                    }
                ) { Text("Kapat") }
            }
        )
    }

    if (restoreConfirmStep == 1 && restorePreview != null) {
        AlertDialog(
            onDismissRequest = { restoreConfirmStep = 0 },
            title = { Text("Geri yüklemeyi onayla") },
            text = { Text("Aktif kişinin mevcut ilaç, ölçüm, doz geçmişi, randevu ve fotoğraf bağlantıları seçilen noktanın durumuyla değiştirilecek. İşlem öncesinde otomatik güvenlik noktası oluşturulacak.") },
            confirmButton = {
                Button(onClick = { restoreConfirmStep = 2 }) { Text("DEVAM ET") }
            },
            dismissButton = { TextButton(onClick = { restoreConfirmStep = 0 }) { Text("İPTAL") } }
        )
    }

    if (restoreConfirmStep == 2 && restorePreview != null) {
        AlertDialog(
            onDismissRequest = { restoreConfirmStep = 1 },
            title = { Text("Son onay") },
            text = { Text("Bu işlem aktif kişinin mevcut verilerini değiştirecek. Seçilen geri yükleme noktasına dönmek istediğinizden emin misiniz?") },
            confirmButton = {
                Button(
                    enabled = !restorePointBusy,
                    onClick = {
                        val point = restorePreview!!.first
                        restorePointBusy = true
                        restoreConfirmStep = 0
                        settingsScope.launch {
                            runCatching {
                                withContext(Dispatchers.IO) {
                                    RestorePointManager.restore(context, settingsDb, activeProfileId, point)
                                }
                            }.onSuccess {
                                restorePointRevision++
                                restorePointStatus = "Geri yükleme tamamlandı. İşlem öncesi otomatik güvenlik noktası da oluşturuldu."
                                restorePreview = null
                                onAlarmRefresh()
                            }.onFailure {
                                restorePointStatus = "Geri yükleme başarısız: ${it.message ?: "Bilinmeyen hata"}"
                            }
                            restorePointBusy = false
                        }
                    }
                ) { Text("EVET, GERİ DÖN") }
            },
            dismissButton = { TextButton(onClick = { restoreConfirmStep = 1 }) { Text("VAZGEÇ") } }
        )
    }

    deleteRestorePoint?.let { point ->
        AlertDialog(
            onDismissRequest = { deleteRestorePoint = null },
            title = { Text("Geri yükleme noktasını sil") },
            text = { Text("Yalnız seçili geri yükleme noktası silinecek. Devam edilsin mi?") },
            confirmButton = {
                Button(onClick = {
                    restorePointStatus = if (RestorePointManager.delete(point)) "Seçili geri yükleme noktası silindi." else "Geri yükleme noktası silinemedi."
                    deleteRestorePoint = null
                    restorePointRevision++
                }) { Text("SİL") }
            },
            dismissButton = { TextButton(onClick = { deleteRestorePoint = null }) { Text("İPTAL") } }
        )
    }

    pinTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { pinTarget = null },
            title = {
                Text(
                    "${target.name} için yönetici şifresi",
                    color = LogoColorDark,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newAdminPin,
                        onValueChange = {
                            if (it.length <= 12 && it.all(Char::isDigit)) newAdminPin = it
                        },
                        label = { Text("Yeni şifre") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmAdminPin,
                        onValueChange = {
                            if (it.length <= 12 && it.all(Char::isDigit)) confirmAdminPin = it
                        },
                        label = { Text("Yeni şifreyi tekrar girin") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    pinError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            },
            confirmButton = {
                Button(onClick = {
                    pinError = when {
                        newAdminPin.length !in 4..12 -> "Şifre 4–12 haneli olmalıdır."
                        newAdminPin != confirmAdminPin -> "Şifreler eşleşmiyor."
                        else -> null
                    }
                    if (pinError == null) {
                        onSetAdminPin(target.id, newAdminPin)
                        pinTarget = null
                    }
                }) { Text("Kaydet") }
            },
            dismissButton = {
                TextButton(onClick = { pinTarget = null }) { Text("İptal") }
            }
        )
    }

    if (confirmClearAlarms) {
        val registeredAlarmCount = AlarmScheduler.registeredGroupCount(context) +
            AppointmentAlarmScheduler.registeredCount(context) +
            if (AlarmScheduler.hasPendingTest(context)) 1 else 0
        AlertDialog(
            onDismissRequest = { confirmClearAlarms = false },
            title = { Text("Eski alarm isteklerini temizle") },
            text = {
                Text(
                    "$registeredAlarmCount kayıtlı Şifahane alarm isteği, ilgili bildirimler ve açık alarm ekranı temizlenecek. Ardından yalnız güncel ilaç ve randevu planları yeniden kurulacak. Devam edilsin mi?"
                )
            },
            confirmButton = {
                Button(onClick = {
                    confirmClearAlarms = false
                    onClearStaleAlarms()
                }) { Text("TEMİZLE VE YENİLE") }
            },
            dismissButton = { TextButton(onClick = { confirmClearAlarms = false }) { Text("İPTAL") } }
        )
    }
}

@Composable
internal fun SettingsPageHeader(
    title: String,
    showBack: Boolean,
    titleSize: androidx.compose.ui.unit.TextUnit,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp).padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBack) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Ayarlar ana sayfasına dön")
            }
        } else {
            Spacer(Modifier.size(48.dp))
        }
        Text(
            title,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            color = LogoColorDark,
            fontSize = titleSize,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall.copy(shadow = LogoTextShadow)
        )
        Spacer(Modifier.size(48.dp))
    }
}

@Composable
internal fun SettingsCategoryCard(
    title: String,
    description: String,
    icon: ImageVector,
    titleSize: androidx.compose.ui.unit.TextUnit,
    bodySize: androidx.compose.ui.unit.TextUnit,
    cardBackground: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().heightIn(min = 82.dp).sifahaneSoftBoundary(2.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.7.dp, Color(0xFF00AEEF).copy(alpha = 0.50f), RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = LogoColor,
                modifier = Modifier.size(32.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                AutoSizeSettingsTitle(
                    title = title,
                    maximumSize = titleSize,
                    textAlign = TextAlign.Start
                )
                Text(
                    description,
                    color = LogoColorDark,
                    fontSize = bodySize
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Bölümü aç",
                tint = LogoColorDark,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
internal fun AccessibleSettingsButton(
    text: String,
    description: String,
    icon: ImageVector,
    enabled: Boolean = true,
    titleSize: androidx.compose.ui.unit.TextUnit,
    supportingSize: androidx.compose.ui.unit.TextUnit,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().heightIn(min = 72.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
    ) {
        OutlinedLogoIcon(icon, contentDescription = null, size = 28.dp)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text, fontSize = titleSize, fontWeight = FontWeight.Bold)
            Text(description, fontSize = supportingSize)
        }
    }
}

@Composable
internal fun SettingsInformationCard(
    title: String,
    body: String,
    titleSize: androidx.compose.ui.unit.TextUnit,
    bodySize: androidx.compose.ui.unit.TextUnit,
    cardBackground: Color,
    borderColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth().sifahaneSoftBoundary(2.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.7.dp, Color(0xFF00AEEF).copy(alpha = 0.50f), RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                title,
                modifier = Modifier.fillMaxWidth(),
                color = LogoColorDark,
                fontSize = titleSize,
                fontWeight = FontWeight.Bold
            )
            Text(
                body,
                modifier = Modifier.fillMaxWidth(),
                color = LogoColorDark,
                fontSize = bodySize
            )
        }
    }
}

@Composable
internal fun PermissionStatusCard(
    title: String,
    description: String,
    granted: Boolean,
    cardBackground: Color,
    borderColor: Color,
    manageLabel: String = "YÖNET",
    onManage: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth().sifahaneSoftBoundary(2.dp),
        border = BorderStroke(1.dp, if (granted) LogoColor.copy(alpha = 0.70f) else Color(0xFFE30A17).copy(alpha = 0.65f)),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.7.dp, Color(0xFF00AEEF).copy(alpha = 0.50f), RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                if (granted) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = if (granted) LogoColorDark else Color(0xFFE30A17)
            )
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = LogoColorDark)
                Text(description, style = MaterialTheme.typography.bodySmall)
                Text(if (granted) "Durum: Açık" else "Durum: Kapalı veya kullanıcı işlemi gerekli", style = MaterialTheme.typography.labelMedium)
            }
            onManage?.let { manage ->
                TextButton(onClick = manage, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text(manageLabel)
                }
            }
        }
    }
}

@Composable
internal fun AccessibilitySwitchCard(
    title: String,
    description: String,
    checked: Boolean,
    titleSize: androidx.compose.ui.unit.TextUnit,
    supportingSize: androidx.compose.ui.unit.TextUnit,
    cardBackground: Color,
    borderColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().sifahaneSoftBoundary(2.dp).clickable { onCheckedChange(!checked) },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.7.dp, Color(0xFF00AEEF).copy(alpha = 0.50f), RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    title,
                    color = LogoColorDark,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    description,
                    color = LogoColorDark,
                    fontSize = supportingSize
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
internal fun SettingsSectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().sifahaneSoftBoundary(2.dp),
        shape = StandardFieldShape,
        border = BorderStroke(1.5.dp, LogoColor),
        colors = CardDefaults.cardColors(containerColor = Vantablack05)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.7.dp, Color(0xFF00AEEF).copy(alpha = 0.50f), StandardFieldShape)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AutoSizeSettingsTitle(title)
            content()
        }
    }
}

@Composable
internal fun AutoSizeSettingsTitle(
    title: String,
    maximumSize: androidx.compose.ui.unit.TextUnit = 18.sp,
    textAlign: TextAlign = TextAlign.Center
) {
    var size by remember(title, maximumSize) { mutableStateOf(maximumSize) }
    Text(
        text = title,
        modifier = Modifier.fillMaxWidth(),
        textAlign = textAlign,
        color = LogoColorDark,
        fontSize = size,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Clip,
        style = MaterialTheme.typography.titleMedium.copy(shadow = LogoTextShadow),
        onTextLayout = { result ->
            if (result.hasVisualOverflow && size > 11.sp) size = (size.value - 1f).sp
        }
    )
}

@Composable
internal fun SettingsActionButton(
    text: String,
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = StandardFieldShape,
        border = BorderStroke(1.5.dp, LogoColor),
        colors = profileOutlinedButtonColors()
    ) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(6.dp))
        Text(
            text,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            color = LogoColorDark
        )
    }
}


