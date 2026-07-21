# Şifahane v4.0 Android 12–16 Cihaz Kabul Matrisi

Bu matris fiziksel/OEM davranışlarını doğrulamak içindir. Kaynak uygulama tamamlanmış olsa bile satırlar gerçek cihazda işaretlenmeden üreticiye özgü kabul tamamlanmış sayılmaz.

| Android | Cihaz/OEM sınıfı | Navigasyon | Yazı ölçeği | Tema | Zorunlu senaryolar |
|---|---|---|---|---|---|
| 12 | düşük bellekli referans | 3 tuş | %100 / %200 | Açık/Koyu | ilk kurulum, bildirim, exact alarm fallback |
| 13 | Samsung | gesture | %100 / %200 | Sistem/OLED | tam ekran alarm, pil optimizasyonu, biyometri |
| 14 | Xiaomi/benzeri OEM | gesture | %100 / %200 | Açık/Koyu | OEM arka plan kısıtı, boot yeniden planlama |
| 15 | Pixel/API 35 emülatör + fiziksel | gesture | %100 / %200 | tüm temalar | connectedAndroidTest, screenshot/accessibility |
| 16 | erişilebilen cihaz/emülatör | gesture | %100 / %200 | tüm temalar | exact/full-screen değişiklikleri, gerileme |

Her cihazda ayrıca: uygulama ön/arka/öldürülmüş; ekran açık/kapalı; kilitli/kilitsiz; bildirim/exact/full-screen izin kombinasyonları; tekli/grup/deneme/randevu; erteleme; saat/tarih/saat dilimi; güncelleme üzerine kurulum; TalkBack/Switch Access; portre/yatay ekran kaydedilir.
