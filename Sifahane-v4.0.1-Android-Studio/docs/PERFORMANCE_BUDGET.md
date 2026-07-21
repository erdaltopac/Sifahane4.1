# Şifahane v4.0 Performans ve Kaynak Bütçesi

| Alan | Hedef / kapı | Doğrulama |
|---|---:|---|
| Release APK | en fazla 40 MiB | CI `Release APK size budget` adımı |
| Soğuk açılış | referans cihazda ilk kullanılabilir ekran ≤ 2,5 sn | Android Studio Macrobenchmark/Profiler |
| Ilık açılış | ≤ 1,2 sn | cihaz ölçümü |
| Ana iş parçacığı disk işi | sıfır kritik ihlal | debug `StrictMode`, profiler |
| Büyük fotoğraf decode | en fazla 4096 px örneklenmiş decode | örnek fotoğraf testi |
| Yedek açılmış toplam veri | politika sınırları içinde | `BackupArchivePolicy` testleri |
| Restore point | profil başına en fazla 10 | `RestorePointManager.MAX_POINTS` |
| Liste kimlikleri | kararlı `key` | Compose kaynak incelemesi/UI testi |
| Banner animasyonu | arka planda durur, sistem animasyon azaltmasına uyar | yaşam döngüsü + cihaz profiler |

Apache POI kaldırılmış, Excel uyumlu `.xlsx` çıktısı akış tabanlı `SimpleXlsxWriter` ile korunmuştur. Dosya, JSON, ZIP, fotoğraf ve rapor işleri `Dispatchers.IO` sınırlarında tutulur. CI APK bütçesi aşılırsa release paketi reddedilir.
