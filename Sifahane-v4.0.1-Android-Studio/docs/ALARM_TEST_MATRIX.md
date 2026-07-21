# Manuel Alarm Test Matrisi — Şifahane v4.0

Her satır için cihaz, Android/OEM, ekran/kilit/uygulama durumu, izinler, beklenen/gerçek sonuç, ekran görüntüsü/video ve test eden kişi kaydedilir.

| No | Senaryo | Beklenen |
|---:|---|---|
| 1 | Ön plan, tekli ilaç | Tek aktif ekran, tek ses, tek bildirim kimliği |
| 2 | Ön plan, aynı kullanıcı/saatte iki ilaç | Tek grup alarmı; ilaç başına ayrı bildirim yok |
| 3 | Farklı kullanıcı/same time | Kullanıcılar aynı gruba karışmaz |
| 4 | Arka plan + ekran açık/kilitsiz | Tek sesli/titreşimli bildirim; sessiz ikinci bildirim yok |
| 5 | Arka plan + ekran kapalı/kilitli | Yetkiler uygunsa tek tam ekran alarm ve ses; değilse belgeli fallback |
| 6 | Erteleme | Eski ses/bildirim/PendingIntent kapanır; yalnız seçilen sürede bir kez gelir |
| 7 | Deneme alarmı | Stok/doz/rapor değişmez; yalnız kullanıcı seçerse bir kez ertelenir |
| 8 | Onay/Geri Al | Bildirim temizlenir; doz geçmişi ve stok idempotent güncellenir |
| 9 | Geçmiş yedi doz | Yalnız bir toplu bildirim, benzersiz ilaç sayısı; tekil/grup geçmiş bildirimi yok |
| 10 | Randevu | Doğru randevu ve zamanda tek bildirim; ilaç temizliği randevuyu silmez |
| 11 | Exact alarm izni kapalı | Alarm kaybolmaz; gecikmeli fallback ve tanılama türü görünür |
| 12 | Exact alarm izni açılıp dönüş | Gelecek ilaç/randevu alarmları tek kez yeniden planlanır |
| 13 | Boot / uygulama güncelleme | Yalnız gelecek geçerli alarmlar tek kez kurulur |
| 14 | Saat/tarih ileri-geri | Geçmiş alarm çoğalmaz; gelecek yerel saate göre kurulur |
| 15 | Saat dilimi / DST | Mükerrer yok; `java.time` politikasıyla yerel saat tutarlı |
| 16 | Eski istekleri temizle | Önizleme/onay; yalnız Şifahane istekleri iptal; geçerli ilaç+randevu yeniden kurulur |
| 17 | Alarm Durumu | Dikey kaydırma, son/sonraki alarm, planlama türü, yeniden planlama nedeni/sonucu, tanılama temizliği |

Minimum matris: Android 12, 13, 14, 15 ve erişilebiliyorsa 16; Samsung, Xiaomi/benzeri OEM, düşük bellekli cihaz; gesture/3-button; bildirim/exact/full-screen izin kombinasyonları.
