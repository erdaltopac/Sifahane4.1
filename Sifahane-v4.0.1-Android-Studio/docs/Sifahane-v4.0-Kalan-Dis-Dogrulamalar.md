# Şifahane v4.0 Kalan Dış Doğrulamalar

Kaynak tesliminin dışında kalan, Android SDK/OEM/hesap veya fiziksel cihaz gerektiren kapılar:

1. JDK 17 + Android SDK 35 ile `./gradlew testDebugUnitTest lintDebug assembleDebug assembleRelease assembleDebugAndroidTest`.
2. API 35 emülatörde `connectedDebugAndroidTest`; ardından Android 12–16 cihaz matrisi.
3. Mevcut gerçek v3.6/DB10 kurulumunun üzerine kurulum ve SQLCipher 10→11 geçişi; veri/fotoğraf/ayar korunması.
4. Samsung ve Xiaomi/benzeri OEM'de exact alarm, full-screen intent, bildirim, pil optimizasyonu, boot ve saat/tarih/saat dilimi senaryoları.
5. Biyometri, TalkBack, Switch Access, %200+ sistem yazısı, ekran büyütme ve portre/yatay ekran kabulü.
6. Açık/koyu/OLED/dinamik tema için referans screenshot/kontrast ve Türkçe font render kabulü.
7. Release APK/AAB boyut, SHA-256, imza ve yeniden üretilebilirlik karşılaştırması.
8. Kullanıcı kılavuzu için gerçek cihaz ekran görüntüleri; nihai hukuk/klinik metin onayı ve Play Console/üretim imzası.

Bu maddeler kod eksikliği olarak gizlenmez; cihaz/üretim kabul kayıtları olarak release kontrolünde açık tutulur.
