package com.hazerfen.sifahane.security

/**
 * Android bağımlılığı içermeyen ilk kurulum doğrulama kuralları.
 * Aynı kurallar Compose formunda ve yerel/JVM testlerinde kullanılır.
 */
object FirstRunSecurityPolicy {
    const val MIN_PIN_LENGTH = 4
    const val MAX_PIN_LENGTH = 12
    const val MIN_PATTERN_POINTS = 4

    fun validationError(
        name: String,
        pin: String,
        confirmation: String,
        pattern: List<Int>?
    ): String? = when {
        name.isBlank() -> "Ad alanı zorunludur."
        pin.length !in MIN_PIN_LENGTH..MAX_PIN_LENGTH || !pin.all(Char::isDigit) ->
            "Yönetici şifresi 4–12 haneli olmalıdır."
        pin != confirmation -> "Yönetici şifreleri eşleşmiyor."
        pattern == null || pattern.size < MIN_PATTERN_POINTS ->
            "Kilit deseni belirlemek zorunludur."
        pattern.distinct().size != pattern.size ->
            "Kilit deseni aynı noktayı birden fazla kullanamaz."
        pattern.any { it !in 1..9 } -> "Kilit deseni geçersiz noktalar içeriyor."
        else -> null
    }
}
