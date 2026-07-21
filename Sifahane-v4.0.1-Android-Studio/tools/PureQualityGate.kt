package com.hazerfen.sifahane.quality

import com.hazerfen.sifahane.alarm.AlarmDeliveryPolicy
import com.hazerfen.sifahane.alarm.AlarmGroupingPolicy
import com.hazerfen.sifahane.alarm.MedicationAlarmKind
import com.hazerfen.sifahane.alarm.AlarmTimePolicy
import com.hazerfen.sifahane.backup.BackupArchivePolicy
import com.hazerfen.sifahane.backup.BackupCipherCore
import com.hazerfen.sifahane.data.DoseActionPolicy
import com.hazerfen.sifahane.export.SimpleXlsxWriter
import com.hazerfen.sifahane.security.FirstRunSecurityPolicy
import com.hazerfen.sifahane.validation.HealthDataValidator
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.zip.ZipInputStream
import javax.xml.parsers.DocumentBuilderFactory

private fun checkThat(condition: Boolean, message: String) {
    if (!condition) error(message)
}

fun main() {
    checkThat(
        FirstRunSecurityPolicy.validationError("Erdal", "2468", "2468", listOf(1, 2, 5, 8)) == null,
        "İlk kurulum geçerli veriyi reddetti"
    )
    checkThat(
        FirstRunSecurityPolicy.validationError("", "2468", "2468", listOf(1, 2, 5, 8)) != null,
        "İlk kurulum boş adı kabul etti"
    )
    checkThat(AlarmDeliveryPolicy.isDuplicate(false, 1_000L, 2_000L), "Alarm yinelenme penceresi çalışmıyor")
    checkThat(!AlarmDeliveryPolicy.isDuplicate(false, 1_000L, 31_001L), "Alarm yinelenme penceresi gereğinden uzun")
    checkThat(
        AlarmGroupingPolicy.groupKey(MedicationAlarmKind.NORMAL, 1L, 60_000L) !=
            AlarmGroupingPolicy.groupKey(MedicationAlarmKind.NORMAL, 2L, 60_000L),
        "Farklı kullanıcıların alarm grupları çakışıyor"
    )
    checkThat(AlarmGroupingPolicy.uniqueMedicationCount(longArrayOf(1, 1, 2)) == 2, "Toplu bildirim ilaç sayısı hatalı")

    val istanbul = ZoneId.of("Europe/Istanbul")
    val after = LocalDateTime.of(2026, 7, 21, 9, 0).atZone(istanbul).toInstant().toEpochMilli()
    val next = AlarmTimePolicy.nextValidOccurrence("2026-07-01", null, true, "08:00", after, istanbul)
    val expected = LocalDateTime.of(2026, 7, 22, 8, 0).atZone(istanbul).toInstant().toEpochMilli()
    checkThat(next == expected, "Sonraki alarm zamanı hatalı")

    checkThat(!DoseActionPolicy.shouldDecreaseStock("ALINDI"), "Tekrarlı doz stok düşürüyor")
    checkThat(DoseActionPolicy.canUndo(0) && !DoseActionPolicy.canUndo(1), "Geri al idempotensi hatalı")
    checkThat(HealthDataValidator.bloodPressureError(120, 80, 70) == null, "Geçerli tansiyon reddedildi")
    checkThat(HealthDataValidator.bloodPressureError(70, 90, 70) != null, "Geçersiz tansiyon kabul edildi")
    checkThat(HealthDataValidator.glucoseError(100) == null, "Geçerli glikoz reddedildi")
    checkThat(HealthDataValidator.glucoseError(0) != null, "Geçersiz glikoz kabul edildi")

    listOf("../x", "/x", "a//b", "C:/x", "a/./b").forEach {
        checkThat(BackupArchivePolicy.isUnsafeEntryName(it), "Güvensiz yedek yolu kabul edildi: $it")
    }
    checkThat(!BackupArchivePolicy.isUnsafeEntryName("medicine_photos/a.jpg"), "Güvenli yedek yolu reddedildi")
    checkThat(BackupArchivePolicy.hasSuspiciousCompression(10_100, 100), "Zip-bomb oranı kaçırıldı")

    val original = "Şifahane şifreli yedek testi".toByteArray()
    val encrypted = ByteArrayOutputStream()
    BackupCipherCore.encrypt(ByteArrayInputStream(original), encrypted, "guvenli-parola".toCharArray())
    val opened = BackupCipherCore.open(ByteArrayInputStream(encrypted.toByteArray()), "guvenli-parola".toCharArray())
    checkThat(opened.use { it.readBytes() }.contentEquals(original), "AES-GCM round-trip başarısız")
    val tampered = encrypted.toByteArray().also { it[it.lastIndex] = (it.last().toInt() xor 1).toByte() }
    val tamperRejected = runCatching {
        BackupCipherCore.open(ByteArrayInputStream(tampered), "guvenli-parola".toCharArray()).use { it.readBytes() }
    }.isFailure
    checkThat(tamperRejected, "Değiştirilmiş AES-GCM yedeği kabul edildi")

    val xlsx = ByteArrayOutputStream()
    SimpleXlsxWriter.write(
        xlsx,
        listOf(SimpleXlsxWriter.Sheet("İlaçlar", listOf(listOf("İlaç", "Stok"), listOf("Test", 5))))
    )
    val entries = linkedMapOf<String, ByteArray>()
    ZipInputStream(ByteArrayInputStream(xlsx.toByteArray())).use { zip ->
        var entry = zip.nextEntry
        while (entry != null) {
            entries[entry.name] = zip.readBytes()
            entry = zip.nextEntry
        }
    }
    listOf("[Content_Types].xml", "xl/workbook.xml", "xl/styles.xml", "xl/worksheets/sheet1.xml").forEach {
        checkThat(it in entries, "Eksik OOXML parçası: $it")
    }
    val xmlFactory = DocumentBuilderFactory.newInstance().apply { isNamespaceAware = true }
    entries.filterKeys { it.endsWith(".xml") }.values.forEach {
        xmlFactory.newDocumentBuilder().parse(ByteArrayInputStream(it))
    }

    println("PURE_KOTLIN_QUALITY_GATE_OK")
}
