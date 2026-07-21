# Değişiklik Günlüğü

## 4.0.1 — 2026-07-21

- `AppointmentScreen.kt` randevu tarih seçicisindeki `Calendar.time` / form `time` ad gölgelemesi giderildi.
- Tarih seçici başlangıç tarihi açık `Calendar.setTime(Date)` çağrısıyla atanıyor.
- `java.util.*` joker içe aktarımı açık `Calendar`, `Date` ve `Locale` içe aktarımlarına dönüştürüldü.
- Aynı derleme hatasının yeniden eklenmesini engelleyen statik kaynak kontrolü eklendi.

## 4.0 — 2026-07-21

### İlk kurulum ve erişim
- Otomatik boş “Kendim” profili kaldırıldı; ilk yönetici profilinin bilgi, şifre ve desenle oluşturulması zorunlu yapıldı.
- Eski boş placeholder için gerçek veriyi koruyan onarım eklendi.
- Desen, biyometri, yönetici şifresi, Geri ve İptal sıralaması; biyometri opt-in davranışı eklendi.

### Veri ve güvenlik
- Room v11 foreign key/indeks/migrasyon ve v3–v11 şema kayıtları eklendi.
- SQLCipher/Keystore, güvenli kimlik bilgileri, artan kilit süresi ve hassas ekran koruması tamamlandı.
- Sağlık, stok, tarih/saat domain doğrulamaları ve atomik/idempotent doz işlemleri eklendi.

### Yedekleme
- Yeni `.sifbak` AES-256-GCM biçimi, legacy `.sifbackup`/ZIP içe aktarma, arşiv güvenlik sınırları, akış tabanlı medya ve atomik geri alma tamamlandı.
- Özdeş kayıt atlama, randevu ve yetim eski doz geçmişi desteği eklendi.
- Şifreli geri yükleme noktaları, iki aşamalı geri dönüş ve otomatik güvenlik noktası eklendi.

### Alarm ve izinler
- Kullanıcı bazlı tek/grup alarmı, tek-sefer teslim, gerçek erteleme, toplu geçmiş doz, exact alarm fallback ve kişisel verisiz kaydırılabilir tanılama eklendi.
- Android izin ekranı tek `SİSTEM AYARLARINA GİT` düğmesine indirildi.

### UI, erişilebilirlik ve mimari
- Alt menü sırasına bağlı yatay kaydırma, ortak randevu kartı ve güvenli form iptali eklendi.
- Açık/koyu/OLED/sistem/dinamik tema, yazı ölçeği ve kritik 48 dp hedefler eklendi.
- MainActivity özellik dosyalarına ayrıldı; Navigation Compose, ViewModel/SavedState ve repository/domain temelleri eklendi.
- Apache POI kaldırıldı; hafif akış tabanlı XLSX üreticisi eklendi.
- Unit/instrumentation/accessibility/screenshot test kaynakları, source kalite kapısı ve GitHub Actions CI genişletildi.

Önceki sürüm notları `docs/archive/` altında korunur.
