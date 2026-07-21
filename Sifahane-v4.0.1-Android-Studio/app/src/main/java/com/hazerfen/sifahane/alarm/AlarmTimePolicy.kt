package com.hazerfen.sifahane.alarm

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/** Saat dilimi ve yaz/kış saati geçişlerini java.time ile tek noktada yönetir. */
object AlarmTimePolicy {
    fun plannedForDate(date: LocalDate, timeText: String, zoneId: ZoneId): Long? {
        val time = parseTime(timeText) ?: return null
        return date.atTime(time).atZone(zoneId).toInstant().toEpochMilli()
    }

    fun nextValidOccurrence(
        startDate: String,
        endDate: String?,
        continuous: Boolean,
        timeText: String,
        afterMillis: Long,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): Long? {
        val time = parseTime(timeText) ?: return null
        val start = runCatching { LocalDate.parse(startDate) }.getOrNull() ?: return null
        val end = endDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        var date = Instant.ofEpochMilli(afterMillis).atZone(zoneId).toLocalDate()
        if (date.isBefore(start)) date = start

        repeat(3700) {
            if (!continuous && end != null && date.isAfter(end)) return null
            if (!date.isBefore(start)) {
                val candidate = date.atTime(time).atZone(zoneId).toInstant().toEpochMilli()
                if (candidate > afterMillis) return candidate
            }
            date = date.plusDays(1)
        }
        return null
    }

    fun isDateInRange(
        startDate: String,
        endDate: String?,
        continuous: Boolean,
        dateMillis: Long,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): Boolean {
        val date = Instant.ofEpochMilli(dateMillis).atZone(zoneId).toLocalDate()
        val start = runCatching { LocalDate.parse(startDate) }.getOrNull() ?: return false
        val end = endDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        return !date.isBefore(start) && (continuous || end == null || !date.isAfter(end))
    }

    private fun parseTime(value: String): LocalTime? =
        runCatching { LocalTime.parse(value) }.getOrNull()
}
