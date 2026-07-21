# Şifahane Güvenlik Mimarisi — v4.0

## Kimlik ve ilk kurulum
- Varsayılan `12345` veya başka bir arka kapı PIN yoktur.
- Temiz kurulumda ilk kullanıcı yönetici rolüyle; ad, 4–12 haneli kullanıcı şifresi ve en az dört farklı noktadan oluşan desenle tek akışta oluşturulur.
- Yönetici şifresi ve profil desenleri PBKDF2-HMAC-SHA256, benzersiz 128 bit salt ve sabit zamanlı karşılaştırma kullanır.
- Eski SHA-256 kayıtları yalnız kontrollü geçiş için doğrulanır ve ilk başarılı girişte yükseltilir.
- Hatalı şifre/desen denemelerinde artan, yeniden başlatmadan etkilenmeyen kilit süresi vardır.
- Biyometri profil bazında varsayılan kapalıdır; yalnız açık kullanıcı onayı ve Android BiometricPrompt doğrulamasıyla etkinleşir.

## Veritabanı
- Room v11, SQLCipher `SupportOpenHelperFactory` üzerinden açılır.
- 256 bit rastgele veri anahtarı Android Keystore AES-GCM anahtarıyla sarılır; açık anahtar tercihlerde tutulmaz.
- Düz SQLite algılanırsa geçici şifreli veritabanına `sqlcipher_export`, doğrulama ve atomik ad değiştirme uygulanır.
- Foreign-key/silme davranışları ve sorgu indeksleri şema/migrasyon testleriyle tanımlanmıştır.

## Yedek ve geri yükleme
- Yeni dış yedek biçimi `.sifbak`: `SIFAHANE-AESGCM1` başlığı, PBKDF2 ve AES-256-GCM şifreli ZIP akışı.
- Parola 8–128 karakterdir; uygulama parolayı kalıcı saklamaz.
- Yanlış parola, GCM etiketi bozulması, zip-slip, mutlak/kötü yol, yinelenen giriş, aşırı sıkıştırma, giriş sayısı ve boyut sınırları reddedilir.
- Eski `.sifbackup` ve standart ZIP yalnız geriye dönük içe aktarma için desteklenir.
- Uygulama içi geri yükleme noktaları şifrelidir, profil başına en fazla 10 adet tutulur ve geri dönüşten önce otomatik güvenlik noktası oluşturulur.

## Platform ve gizlilik
- `allowBackup=false`; Android veri çıkarım kuralları uygulama alanlarını hariç tutar.
- Hassas ekranlar `FLAG_SECURE` ile ekran görüntüsü/son uygulamalar önizlemesine karşı korunur.
- Kilit ekranı bildirimlerinin genel sürümünde kişi, ilaç, doktor veya ölçüm verisi bulunmaz.
- Açık rapor/paylaşım öncesinde şifrelenmemiş sağlık verisi uyarısı gösterilir.
- Destek tanılaması yalnız sürüm, cihaz, izin ve anonim alarm durumunu içerir.

## Dış kabul
Gerçek v3.6 verisiyle SQLCipher/Room 10→11 geçişi, Keystore kaybı, OEM izin/alarm davranışı, biyometri, erişilebilirlik ve release imza/bağımlılık taraması Android Studio/CI/fiziksel cihazda doğrulanmalıdır.
