package com.hazerfen.sifahane

import android.content.Context
import java.io.File

fun comprehensiveUserGuideSections(): List<Pair<String, String>> = listOf(
        "1. Başlangıç ve güvenlik" to "İlk kurulumda kullanıcı 4–12 haneli kendi yönetici şifresini iki kez girerek oluşturur. Şifahane’de varsayılan veya arka kapı yönetici şifresi yoktur. Kullanıcı desenleri en az dört noktadan oluşturulur; art arda hatalı PIN veya desen denemelerinde giderek artan güvenlik beklemesi uygulanır.",
        "2. Kişiler ve yetkiler" to "Kişiler bölümünden yönetici veya standart kullanıcı profilleri yönetilir. İlk kurulumda ilk kullanıcı bilgi girişi, yönetici şifresi ve zorunlu desenle yönetici olarak oluşturulur. Tek kalan yönetici silinemez. Kullanıcı silme işlemleri yönetici şifresi gerektirir. Kişi eklerken elle giriş veya yedekten içe aktarma yolu seçilir.",
        "3. İlaç ekleme ve düzenleme" to "İlaçlar bölümünde ilaç adı, doz, kullanım amacı, başlangıç-bitiş tarihleri, saatler ve stok bilgileri kaydedilir. Aynı dakika için planlanan ilaçlar alarm sırasında tek grupta gösterilir.",
        "4. Bugün ekranı" to "Bugün ekranı seçili kullanıcının günlük ilaç planını gösterir. Kartlardan alınma durumu izlenir; geçmiş veya gelecek günlere gidilebilir.",
        "5. Alarm ve grup işlemleri" to "Aynı saatteki ilaçlar kullanıcı sekmeleriyle tek alarm kartında listelenir. Her ilaç için Aldım, Ertele ve Almayacağım işlemleri ayrı ayrı veya topluca uygulanabilir. Her grup için tek ses ve titreşim oturumu çalışır.",
        "6. Erteleme seçenekleri" to "Ayarlar > Alarm ve Bildirimler bölümünde üç erteleme süresi belirlenir. Alarm ekranında bireysel ve toplu erteleme için bu üç seçenek kullanılır. Yeni seçim aynı ilaç ve doz için önceki bekleyen ertelemeyi geçersiz kılar.",
        "7. Randevular" to "Randevular bölümünde doktor, kurum, tarih, saat ve not bilgileri eklenebilir; kayıtlar düzenlenebilir ve hatırlatıcı kurulabilir.",
        "8. Ölçümler" to "Tansiyon ve kan şekeri sekmelerinde yeni ölçüm eklenebilir, mevcut kayıtlar düzenlenebilir ve zaman sırasıyla incelenebilir.",
        "9. Raporlar" to "Tarih aralığı seçilerek kayıt ve grafik raporları görüntülenir. Grafiklerde eksen değerleri bulunur. Grafikler yüksek çözünürlüklü PNG olarak paylaşılabilir veya Şifahane Raporları albümüne kaydedilebilir.",
        "10. Yedekleme" to "Kişi verilerini dışa aktarma işlemi, kullanıcının belirlediği en az sekiz karakterli parola ile AES-GCM korumalı .sifbak dosyası oluşturur. Parola uygulamada saklanmaz; kaybedilirse yedek açılamaz. İkinci kopya Android paylaşım ekranıyla gönderilebilir veya seçilen dosya konumuna kaydedilebilir.",
        "11. Yedekten içe aktarma" to "Şifreli yedek seçildiğinde oluşturma parolası istenir; yanlış parola veya değiştirilmiş dosya reddedilir. Eski Şifahane ZIP yedekleri yalnız kontrollü içe aktarma uyumluluğu için desteklenir. Önizleme ve kullanıcı rolü kontrolünden sonra içe aktarma başlatılır.",
        "12. Güvenlik, biyometri ve otomatik kilit" to "Profil kilit ekranında Desen Kilidi, Biyometrik Giriş, Yönetici Şifresi, Geri ve İptal seçenekleri bu sırayla görünür. Biyometri profil bazında kapalı başlar ve yalnız Ayarlar > Güvenlik ve Yetkilendirme bölümünde sistem doğrulaması başarıyla tamamlanırsa açılır. Otomatik kilit için 30 saniye, 1, 2, 5, 10 veya 30 dakika ya da Hiçbir zaman seçilebilir.",
        "13. Görünüm ve tema" to "Sistem, açık, standart koyu ve OLED saf siyah görünüm seçenekleri bulunur. Android 12 ve üzerinde isteğe bağlı dinamik renk açılabilir. Beş yazı tipi, ek yazı ölçeği, vurgu ve kart iç vurgu opaklığı yeniden açılışta korunur.",
        "14. Geri gezinme" to "Telefonun sistem geri düğmesi veya geri hareketi önce açılır kartı ya da iletişim kutusunu kapatır; ardından kullanıcıyı gerçek gezinme geçmişindeki bir önceki ekrana döndürür.",
        "15. Yardım ve yapay zekâ" to "Yardım başlıkları uygulama içinde aranabilir. Yapay Zekâya Sor seçeneği yalnızca bu kapsamlı kılavuzu kişisel veri içermeyen Word belgesi olarak Android paylaşım ekranına gönderir.",
        "16. Android izinleri" to "Ayarlar > Android İzinleri bölümünde bildirim, kesin alarm, tam ekran alarm, pil optimizasyonu, kamera ve sistem dosya seçicisi durumları birlikte görülür. Android'in doğrudan değiştirmeye izin vermediği özel erişimlerde ilgili güvenli sistem ekranı açılır; geniş depolama izni istenmez.",
        "17. Eski alarm isteklerini temizleme" to "Alarm ve Bildirimler bölümündeki Önceden Kalmış Alarm İsteklerini Sil işlemi Şifahane'nin kayıtlı eski isteklerini temizler ve yalnız güncel ilaç planlarının alarmlarını yeniden kurar.",
        "18. Geri yükleme noktaları" to "Ayarlar > Yedekleme bölümünde seçili kullanıcı için şifreli geri yükleme noktası oluşturulabilir. En fazla 10 nokta tutulur. Geri dönüş iki ayrı onay ister ve işlemden hemen önce otomatik güvenlik noktası oluşturur. Noktalar uygulama içi depoda şifreli saklanır.",
        "19. Kayar Hazerfen bannerı" to "Alt menünün üstündeki banner, üst yumuşak geçiş ve cam katmanıyla birlikte yaklaşık 10 mm yüksekliğindedir. Tam opak Hazerfen logoları aynı yükseklikte, bitişik ve boşluksuz olarak beş dakikada bir ekran genişliği hızla akar. Bannerın üst sınırı, alt menü altındaki vantablack yumuşak geçişin düşey aynasıdır. Bannera dokunulduğunda Hazerfen internet sitesi açılır.",
        "20. Gizlilik" to "Şifrelerinizi, deseninizi ve sağlık yedeklerinizi güvenmediğiniz uygulamalarla paylaşmayın. Kılavuz belgesinde kişi veya sağlık verisi bulunmaz.",
        "21. Tıbbi kapsam ve güvenli kullanım" to "Şifahane yalnız kayıt ve hatırlatma aracıdır; tanı koymaz, tedavi veya doz değişikliği önermez ve acil durum hizmeti değildir. Kaçırılan dozda çift doz almayın; prospektüsü izleyin ve hekim ya da eczacınıza danışın. Acil durumda 112'yi arayın."
    )

/** Creates a selectable, searchable Word-compatible document without profile or health data. */
fun createComprehensiveUserGuide(context: Context): File {
    val sections = comprehensiveUserGuideSections()
    val html = buildString {
        append("<html><head><meta charset='utf-8'><title>Şifahane Kullanıcı Kılavuzu</title></head><body>")
        append("<h1>Şifahane Kapsamlı Kullanıcı Kılavuzu</h1>")
        append("<p><strong>Uygulama ve kılavuz sürümü: ${BuildConfig.VERSION_NAME}</strong></p>")
        append("<p>Bu belge aranabilir ve seçilebilir metin içerir. Kişisel sağlık veya profil verisi içermez.</p>")
        append("<h2>İçindekiler</h2><ol>")
        sections.forEach { append("<li>${it.first.substringAfter(". ")}</li>") }
        append("</ol>")
        sections.forEach { (title, body) -> append("<h2>$title</h2><p>$body</p>") }
        append("</body></html>")
    }
    val directory = File(context.cacheDir, "guides").apply { mkdirs() }
    return File(directory, "Sifahane_Kapsamli_Kullanici_Kilavuzu_v${BuildConfig.VERSION_NAME}.doc")
        .apply { writeText(html, Charsets.UTF_8) }
}
