# Şifahane v4.0 Mimari Notları

## Uygulama kabuğu ve gezinme

`MainActivity.kt` artık ilk kurulum, kimlik doğrulama ve ortak UI kabuğunu taşır. Ana özellikler `features/` altında ayrılmıştır:

- `AppShell.kt`: Navigation Compose, alt menü, yatay kaydırma, güvenlik kapısı ve ortak sayfa kabuğu.
- `ProfilesFeature.kt`: kişi/profil listesi ve profil işlemleri.
- `TodayFeature.kt`: Bugün zaman çizelgesi ve doz eylemleri.
- `MedicationFeature.kt`: ilaç listesi ve ilaç yaşam döngüsü.
- `ReportsFeature.kt`: raporlar, uyum özeti ve dışa aktarma.
- `SettingsFeature.kt`: izinler, güvenlik, yedekleme, geri yükleme noktaları, tanılama, tema ve lisanslar.
- `EditorsFeature.kt` ve `MeasurementDialogs.kt`: güvenli form akışları.

Gezinme `NavHost` ve kararlı rota dizgileri kullanır. Alt menü sırası aynı zamanda yatay kaydırma sırasıdır; doğrulama ekranı veya Ayarlar alt sayfası açıkken kaydırma devre dışıdır.

## Durum ve veri katmanı

- `SifahaneViewModel`: aktif profil durumunu `SavedStateHandle` üzerinden korur.
- `repository/SifahaneRepositories.kt`: profil, ilaç, ölçüm, randevu ve doz işlem sınırlarını tanımlar.
- Room şeması v11'dir. Şema JSON'ları v3–v11 için `app/schemas/` altında tutulur.
- 10→11 migrasyonu foreign-key ve sorgu indekslerini kurar; migrasyon kaynak kalite kapısında SQLite ile doğrulanır.
- Doz alma/Geri Al işlemleri `DoseActionRepository` içinde atomik ve idempotent transaction olarak uygulanır.

## Güvenlik

- İlk çalıştırmada otomatik boş profil oluşturulmaz. İlk kullanıcı yönetici rolü, kullanıcı tarafından belirlenen yönetici şifresi ve zorunlu desenle tek akışta oluşturulur.
- PIN/desen PBKDF2-HMAC-SHA256 ve benzersiz salt ile saklanır; artan kalıcı kilit süreleri uygulanır.
- Biyometri profil bazında, varsayılan kapalı ve açık kullanıcı onayıyla etkinleştirilir.
- Room SQLCipher ile, veri anahtarı Android Keystore ile korunur. Düz veritabanından kontrollü geçiş kodu korunmuştur.
- Hassas ekranlar `FLAG_SECURE`; Android otomatik yedekleme kapalıdır.

## Yedekleme

- Yeni dış yedek biçimi `.sifbak`; PBKDF2 + AES-256-GCM kullanır.
- Eski `.sifbackup` ve standart ZIP yalnız içe aktarma uyumluluğu için kabul edilir.
- Zip-slip, giriş sayısı, tek girdi/çıktı boyutu, toplam açılmış boyut ve sıkıştırma oranı sınırları vardır.
- Medya geçici dosya/akış üzerinden işlenir; başarısız geri yükleme atomik olarak geri alınır.
- Özdeş kayıt birleştirmede atlanır; randevular ve yetim eski doz geçmişleri korunur.
- Uygulama içi geri yükleme noktaları şifrelidir, profil başına en fazla 10 adet tutulur ve geri dönmeden önce otomatik güvenlik noktası oluşturulur.

## Alarm yaşam döngüsü

Tekli, grup, deneme ve randevu alarmları çakışmaya dayanıklı kalıcı istek kodları ve tek-sefer teslim kapısı kullanır. Gruplama kullanıcı bazında ayrılır. Erteleme eski alarm oturumunu kapatıp yalnız seçilen sürede tek yeni istek kurar. Boot, güncelleme, saat/tarih ve saat dilimi olayları merkezi yeniden planlayıcıdan geçer.

## Kalite kapıları

- `tools/source_quality_gate.py`: şema, migrasyon, erişilebilirlik, Kotlin kaynak kuralları, işlevsel sözleşmeler ve saf Kotlin testleri.
- GitHub Actions: unit test, lint, debug/release build, Android test APK, API 35 emülatör testleri, secret scan ve dependency review.
- Android Studio/SDK bulunmayan ortamlarda kaynak kalite kapısı çalışır; tam Android kabulü Android Studio veya CI üzerinde yapılır.
