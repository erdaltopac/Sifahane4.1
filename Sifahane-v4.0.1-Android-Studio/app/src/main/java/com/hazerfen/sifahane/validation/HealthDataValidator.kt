package com.hazerfen.sifahane.validation

import java.time.LocalDate

/**
 * Sağlık verisi için yalnız veri bütünlüğü sınırları uygular; klinik yorum,
 * teşhis veya tedavi önerisi üretmez.
 */
object HealthDataValidator {
    fun bloodPressureError(systolic: Int?, diastolic: Int?, pulse: Int?): String? = when {
        systolic == null || diastolic == null -> "Büyük ve küçük tansiyon değerlerini girin."
        systolic !in 40..300 -> "Büyük tansiyon 40–300 mmHg aralığında olmalıdır."
        diastolic !in 20..200 -> "Küçük tansiyon 20–200 mmHg aralığında olmalıdır."
        diastolic >= systolic -> "Büyük tansiyon küçük tansiyondan yüksek olmalıdır."
        pulse != null && pulse !in 20..300 -> "Nabız 20–300 atım/dk aralığında olmalıdır."
        else -> null
    }

    fun glucoseError(valueMgDl: Int?): String? = when {
        valueMgDl == null -> "Kan şekeri değerini girin."
        valueMgDl !in 20..1000 -> "Kan şekeri 20–1000 mg/dL aralığında olmalıdır."
        else -> null
    }

    fun medicationError(
        name: String,
        dose: String,
        times: List<String>,
        stock: Int?,
        lowStockLimit: Int?,
        startDate: String,
        endDate: String?,
        continuous: Boolean,
        reportWarningDays: Int? = null
    ): String? {
        if (name.isBlank()) return "İlaç adı zorunludur."
        if (dose.isBlank()) return "Doz bilgisi zorunludur."
        if (times.isEmpty() || times.any { !TIME_REGEX.matches(it) }) {
            return "En az bir geçerli ilaç saati girin."
        }
        if (stock == null || stock < 0) return "Stok sıfırdan küçük olamaz."
        if (lowStockLimit == null || lowStockLimit < 0) return "Düşük stok sınırı sıfırdan küçük olamaz."
        val start = parseDate(startDate) ?: return "Başlangıç tarihi geçersiz."
        if (!continuous) {
            val end = parseDate(endDate) ?: return "Bitiş tarihi geçersiz."
            if (end.isBefore(start)) return "Bitiş tarihi başlangıç tarihinden önce olamaz."
        }
        if (reportWarningDays != null && reportWarningDays !in 0..3650) {
            return "Rapor uyarı süresi 0–3650 gün aralığında olmalıdır."
        }
        return null
    }

    fun reportGroupError(name: String, startDate: String, endDate: String, warningDays: Int?): String? {
        if (name.isBlank()) return "Rapor grubu adı zorunludur."
        val start = parseDate(startDate) ?: return "Rapor başlangıç tarihi geçersiz."
        val end = parseDate(endDate) ?: return "Rapor bitiş tarihi geçersiz."
        if (end.isBefore(start)) return "Rapor bitiş tarihi başlangıç tarihinden önce olamaz."
        if (warningDays == null || warningDays !in 0..3650) return "Uyarı süresi 0–3650 gün aralığında olmalıdır."
        return null
    }

    private fun parseDate(value: String?): LocalDate? =
        value?.takeIf(String::isNotBlank)?.let { runCatching { LocalDate.parse(it) }.getOrNull() }

    private val TIME_REGEX = Regex("^(?:[01]\\d|2[0-3]):[0-5]\\d$")
}
