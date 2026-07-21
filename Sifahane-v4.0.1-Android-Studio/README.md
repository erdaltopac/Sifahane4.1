# Şifahane v4.0.1

Şifahane, sağlık ve ilaç takibini cihaz üzerinde çevrimdışı yürüten Android uygulamasıdır. Bu paket kullanıcının yüklediği v3.6 kaynak tabanına v4.0 güvenlik, ilk kurulum, veri bütünlüğü, alarm, yedekleme, navigasyon, tema, erişilebilirlik, test ve mimari geliştirmelerini uygular.

## v4.0.1 öne çıkanlar

- İlk açılışta boş “Kendim” hesabı yerine zorunlu yönetici profili, kullanıcı şifresi ve desen kurulumu.
- Profil kapısında Desen, Biyometri, Yönetici Şifresi, Geri ve İptal; biyometri açık kullanıcı onayına bağlıdır.
- Alt menü sırasına bağlı yatay sayfa kaydırma ve tek Android sistem ayarı düğmesi.
- SQLCipher + Android Keystore, Room v11 migrasyonu/şemaları, AES-GCM `.sifbak`, şifreli restore point ve güvenli birleştirme.
- Tekli/grup/deneme/randevu alarm yaşam döngüsü, erteleme, exact fallback, toplu geçmiş doz ve kişisel verisiz tanılama.
- Sistem/açık/koyu/OLED/dinamik tema, %200 yazı ölçeği, erişilebilirlik/UI test altyapısı.
- Özellik bazlı kaynak ayrıştırması, Navigation Compose, ViewModel/repository/domain katmanı ve CI kalite kapıları.

## Android Studio'da açma ve doğrulama

1. ZIP'i ayrı bir klasöre çıkarın ve kök klasörü Android Studio ile açın.
2. JDK 17 ve Android SDK 35 seçin; Gradle Sync'in tamamlanmasını bekleyin.
3. `gradlew.bat testDebugUnitTest lintDebug assembleDebug` çalıştırın.
4. Sonra `gradlew.bat assembleRelease assembleDebugAndroidTest` ve uygun emülatörde `connectedDebugAndroidTest` çalıştırın.
5. Veri koruma testi için uygulamayı kaldırmadan mevcut Şifahane'nin üzerine kurun. Uygulamayı kaldırmak yerel verileri ve Android Keystore kaydını silebilir.
6. `docs/RELEASE_CHECKLIST.md`, `docs/ALARM_TEST_MATRIX.md` ve `docs/ANDROID_12_16_CIHAZ_MATRISI.md` ile cihaz kabulünü kaydedin.

## Sürüm

- `versionName`: `4.0.1`
- `versionCode`: `400001`
- `applicationId`: `com.hazerfen.sifahane`
- Room: `11`
- Minimum Android: API 26
- Hedef/derleme Android: API 35

Bu çalışma ortamında internet/Android SDK bulunmadığından APK üretilmemiştir. Kaynak kalite sonuçları ve Android build denemesi `test-results/` altında, ayrıntılı durum matrisleri `docs/` altındadır.
