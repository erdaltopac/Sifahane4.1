# Şifahane v4.0 Değişiklik ve Test Raporu

**Kaynak tabanı:** Kullanıcının yüklediği Şifahane v3.6 Android Studio paketi  
**Hedef:** Şifahane 4.0  
**applicationId:** `com.hazerfen.sifahane`  
**Room:** v11

## Uygulanan ana işlevler

- Temiz kurulumda zorunlu ilk yönetici kullanıcısı; ad, yönetici şifresi ve desen aynı atomik akışta oluşturulur.
- Legacy boş “Kendim” profili yalnız hiçbir gerçek veri/kimlik bilgisi yoksa güvenli biçimde onarılır.
- Desen, biyometri, yönetici şifresi, Geri ve İptal sıralı güvenli profil kapısı; biyometri varsayılan kapalı/opt-in.
- Alt menü sayfaları arasında kontrollü yatay kaydırma; alt menü sırasına uyum ve doğrulama/alt sayfalarda kilitleme.
- Android izinlerinde tek `SİSTEM AYARLARINA GİT` düğmesi ve ayrı durum açıklamaları.
- Room v11 foreign-key/indeks/migrasyon, kaynak kontrollü şemalar, domain doğrulamaları ve atomik doz/stok işlemleri.
- SQLCipher/Keystore veritabanı koruması, `.sifbak` AES-GCM yedekleri, legacy içe aktarma, arşiv sınırları, akış tabanlı medya, özdeş kayıt atlama ve randevu/yetim doz desteği.
- Şifreli geri yükleme noktaları, iki aşamalı geri dönüş ve otomatik güvenlik noktası.
- Alarm tekilleştirme, kullanıcı bazlı gruplama, gerçek erteleme, geçmiş doz toplu bildirimi, exact alarm fallback, temizleme ve kaydırılabilir kişisel verisiz tanılama.
- Açık/koyu/OLED/sistem/dinamik tema, %200 yazı ölçeği, 48 dp kritik hedefler, erişilebilirlik ve screenshot smoke testleri.
- Ağır Apache POI yerine akış tabanlı Excel uyumlu `.xlsx` üreticisi.
- Özellik bazlı dosya ayrıştırması, Navigation Compose, SavedStateHandle, repository/ViewModel/domain tipleri.
- Sağlık güvenliği metinleri, şeffaf doz özeti, doğrulanmamış barkod/prospektüs otomasyonunun kaldırılması ve kişisel verisiz destek raporu.

## Bu ortamda geçen doğrulamalar

`python3 tools/source_quality_gate.py`:

- Room v3–v11 şema anlık görüntüleri
- 10→11 SQLite migrasyon simülasyonu
- erişilebilirlik statik sözleşmeleri
- Kotlin kaynak kuralları ve mimari boyut bütçesi
- işlevsel kabul sözleşmeleri
- PBKDF2, AES-GCM, yanlış parola/veri bozulması, zip-slip/zip-bomb, ilk kurulum, alarm gruplama/zaman, doz idempotensi, sağlık verisi sınırları ve OOXML/XLSX saf Kotlin testleri
- `git diff --check`

## Çalıştırılamayan Android kapıları

Gradle 8.9 dağıtımı bu çalışma ortamında `downloads.gradle.org` alanına erişilemediği için indirilemedi. Bu nedenle burada `testDebugUnitTest`, `lintDebug`, `assembleDebug`, `assembleRelease` ve `connectedDebugAndroidTest` tamamlanamadı. Ayrıntılı hata kaydı `test-results/ANDROID_BUILD_ATTEMPT.txt` içindedir. CI ve Android Studio komutları projede hazırdır.

## Son kabul

Kaynak uygulama, test kaynakları, CI, şemalar, raporlar ve teslim paketi tamamlanmıştır. APK/AAB, gerçek cihaz alarm matrisi, OEM izin davranışı, biyometri/TalkBack/Switch Access ve görsel referans kabulü Android Studio/CI/fiziksel cihazda tamamlanmalıdır; bu doğrulamalar yapılmadan cihaz kabulü “geçti” olarak beyan edilmez.
