# Şifahane v4.0.1 Derleme Düzeltme Raporu

**Tarih:** 21 Temmuz 2026  
**Taban:** Şifahane v4.0  
**Bakım sürümü:** 4.0.1 (`versionCode 400001`)

## Bildirilen hata

Android Studio `Assemble Project` aşamasında `AppointmentScreen.kt` randevu tarih seçicisinde üç tür uyuşmazlığı tanısı gösterdi:

- `java.util.Date?` ile formdaki `String` saat durumu arasında tür uyuşmazlığı,
- `java.util.Date` atamasında assignment type mismatch,
- aynı ifadenin null/non-null türev tanısı.

## Kök neden

Randevu formunda `time` adında bir Compose metin durumu bulunuyordu. `Calendar.getInstance().apply { time = ... }` ifadesindeki Java sentetik `Calendar.time` özelliği, dış kapsamda bulunan `time: String` değişkeniyle ad çakışmasına girdi. Derleyici tarih nesnesini formun metin saat değişkenine atama olarak yorumladı.

## Uygulanan düzeltme

- `Calendar.apply { time = ... }` yerine açık `Calendar.setTime(selectedDate)` çağrısı kullanıldı.
- Parse sonucu önce null güvenli `selectedDate: java.util.Date` değişkenine alındı.
- `java.util.*` joker içe aktarımı kaldırılarak `Calendar`, `Date` ve `Locale` açıkça içe aktarıldı.
- Aynı riskli `Calendar...apply { time = ... }` desenini reddeden kaynak kalite kontrolü eklendi.

## Doğrulama

- Dış kapsamda `time: String` varken `Calendar.setTime(Date)` kullanımını içeren bağımsız Kotlin regresyon testi: **GEÇTİ**.
- Room şema ve 10→11 migrasyon doğrulaması: **GEÇTİ**.
- Erişilebilirlik kaynak kontrolü: **GEÇTİ**.
- Kotlin statik kaynak kontrolü: **GEÇTİ**.
- İşlevsel kabul sözleşmesi: **GEÇTİ**.
- Saf Kotlin kalite paketi: **GEÇTİ**.
- ZIP bütünlük testi: teslim sırasında doğrulandı.

Bu çalışma ortamında Android SDK/Gradle dağıtımı indirilemediği için tam Android `assembleDebug` yeniden çalıştırılamadı. Bildirilen derleme ifadesi doğrudan düzeltilmiş ve bağımsız Kotlin derleyicisiyle doğrulanmıştır.
