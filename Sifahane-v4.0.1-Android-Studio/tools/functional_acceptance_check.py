#!/usr/bin/env python3
"""Kullanıcının v4.0 işlevsel kontrol listesine ait kaynak sözleşmeleri."""
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
def read(path: str) -> str: return (ROOT / path).read_text(errors='strict')

gradle = read('app/build.gradle.kts')
manifest = read('app/src/main/AndroidManifest.xml')
main = read('app/src/main/java/com/hazerfen/sifahane/MainActivity.kt')
app_shell = read('app/src/main/java/com/hazerfen/sifahane/features/AppShell.kt')
settings = read('app/src/main/java/com/hazerfen/sifahane/features/SettingsFeature.kt')
reports = read('app/src/main/java/com/hazerfen/sifahane/features/ReportsFeature.kt')
backup = read('app/src/main/java/com/hazerfen/sifahane/backup/SifahaneBackupManager.kt')
restore = read('app/src/main/java/com/hazerfen/sifahane/backup/RestorePointManager.kt')
alarm_scheduler = read('app/src/main/java/com/hazerfen/sifahane/alarm/AlarmScheduler.kt')
alarm_receiver = read('app/src/main/java/com/hazerfen/sifahane/alarm/AlarmReceiver.kt')
alarm_status = read('app/src/main/java/com/hazerfen/sifahane/ui/AlarmStatusDialog.kt')
db = read('app/src/main/java/com/hazerfen/sifahane/data/AppDatabase.kt')
biometric = read('app/src/main/java/com/hazerfen/sifahane/security/BiometricPreferences.kt')
theme = read('app/src/main/java/com/hazerfen/sifahane/ui/SifahaneTheme.kt')
catalog = read('gradle/libs.versions.toml')
workflow = read('.github/workflows/android-quality.yml')

assert 'versionName = "4.0.1"' in gradle and 'versionCode = 400001' in gradle
assert 'version = 11' in db and 'MIGRATION_10_11' in db
assert 'first_run_setup' in main and 'first_run_pattern' in main and 'first_run_save' in main
assert 'FirstRunSecurityPolicy.validationError' in main
assert 'UserRoles.ADMIN' in main and 'PatternStore.save' in main
assert 'getBoolean(key(profileId), false)' in biometric, 'Biyometri varsayılan kapalı olmalı'
assert 'detectHorizontalDragGestures' in app_shell and 'bottomMenuOrder.getOrNull(targetIndex)' in app_shell
assert settings.count('SİSTEM AYARLARINA GİT') == 1
assert 'Bildirim Merkezi' in settings and 'TÜMÜNÜ OKUNDU İŞARETLE' in settings
assert 'Planlanan doz özeti' in reports and 'yargılayıcı başarı yüzdesi üretmez' in reports
assert 'POST_NOTIFICATIONS' in settings and 'ACTION_REQUEST_SCHEDULE_EXACT_ALARM' in settings
assert '.sifbak' in backup and '.sifbackup' in backup and '.zip' in backup
assert 'BackupCrypto.encryptFile' in backup and 'BackupCrypto.openInput' in backup
assert 'MAX_POINTS = 10' in restore and 'automatic' in restore
assert 'restoreConfirmStep' in settings and 'restoreConfirmStep = 2' in settings
assert 'require(doses.all { it.profileId == profileId })' in alarm_scheduler
assert 'uniqueMedicationCount(ids)' in alarm_receiver
assert 'TANILAMA GEÇMİŞİNİ TEMİZLE' in alarm_status and 'LazyColumn' in alarm_status
assert 'ThemeMode.OLED' in theme and 'dynamicDarkColorScheme' in theme and 'dynamicLightColorScheme' in theme
assert 'org.apache.poi' not in catalog and 'poi' not in gradle.lower()
assert 'allowBackup="false"' in manifest and 'FLAG_SECURE' in main
assert 'connectedDebugAndroidTest' in workflow and 'testDebugUnitTest' in workflow and 'lintDebug' in workflow
print('FUNCTIONAL_ACCEPTANCE_CHECK_OK')
