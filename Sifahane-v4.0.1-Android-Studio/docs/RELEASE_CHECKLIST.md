# Şifahane v4.0 Teknik Release Kontrolü

## Kaynak kapısı
- [x] `python3 tools/source_quality_gate.py`
- [x] Room v3–v11 şema JSON'ları kaynakta
- [x] 10→11 SQLite migrasyon simülasyonu
- [x] Saf Kotlin güvenlik/yedek/alarm/doz/doğrulama/XLSX testleri
- [x] Git fark biçim kontrolü

## Android build ve test
- [ ] `./gradlew testDebugUnitTest`
- [ ] `./gradlew lintDebug`
- [ ] `./gradlew assembleDebug assembleRelease assembleDebugAndroidTest`
- [ ] `./gradlew connectedDebugAndroidTest`
- [ ] Debug/release APK veya AAB açılır ve SHA-256 kaydedilir

## Güncelleme/veri
- [ ] Mevcut v3.6/DB10 üzerine kurulum; kişiler, ilaçlar, ölçümler, dozlar, randevular, fotoğraflar ve ayarlar korunur
- [ ] Düz DB→SQLCipher ve Room 10→11 gerçek cihaz geçişi
- [ ] `.sifbak` round-trip, yanlış parola, bozulma, legacy ZIP ve büyük fotoğraflı yedek
- [ ] Restore point oluşturma, iki aşamalı geri dönüş ve otomatik güvenlik noktası

## Cihaz ve erişilebilirlik
- [ ] Android 12–16 alarm/izin/OEM matrisi
- [ ] Tekli, grup, deneme, randevu, erteleme, boot, güncelleme, saat/tarih/saat dilimi
- [ ] Biyometri/desen/yönetici şifresi ve ilk kullanıcı yolculuğu
- [ ] TalkBack, Switch Access, %200+ yazı, ekran büyütme, portre/yatay
- [ ] Açık/koyu/OLED/dinamik tema screenshot/kontrast ve Türkçe fontlar

## Yayın
- [ ] Üretim imzası ve Play Console işlemleri hesap sahibi tarafından tamamlandı
- [ ] Nihai hukuk/klinik metin onayı alındı
- [ ] İmzalı cihaz test raporu ve ekran görüntülü kullanıcı kılavuzu arşivlendi
