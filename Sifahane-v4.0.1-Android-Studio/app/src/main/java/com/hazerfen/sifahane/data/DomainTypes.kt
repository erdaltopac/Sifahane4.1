package com.hazerfen.sifahane.data

/** Veritabanında saklanan doz eylemlerinin tek kaynağı. */
enum class DoseAction(val storageValue: String) {
    TAKEN("ALINDI"),
    SKIPPED("BUGÜN ALINMADI");

    companion object {
        fun fromStorage(value: String?): DoseAction? = entries.firstOrNull { it.storageValue == value }
    }
}

/** Randevu durumlarının serbest metin olarak dağılmasını engeller. */
enum class AppointmentStatus {
    PLANNED,
    ATTENDED,
    CANCELLED,
    POSTPONED;

    companion object {
        fun fromStorage(value: String?): AppointmentStatus =
            entries.firstOrNull { it.name == value } ?: PLANNED
    }
}

/** Kan şekeri ölçüm bağlamı; kullanıcıya gösterilen Türkçe ad ile saklama değeri aynıdır. */
enum class GlucoseMeasurementType(val displayName: String) {
    FASTING("Açlık"),
    POSTPRANDIAL("Tokluk"),
    RANDOM("Rastgele"),
    NIGHT("Gece");

    companion object {
        fun fromStorage(value: String?): GlucoseMeasurementType =
            entries.firstOrNull { it.displayName == value } ?: FASTING
    }
}
