package com.hazerfen.sifahane.alarm

import android.content.Context

/** Aynı alarm kimliğinin paralel ekran/bildirim oluşturmasını engelleyen tek kapı. */
object AlarmDeliveryGate {
    private const val PREFS = "alarm_delivery_v4"
    private val lock = Any()

    fun claim(context: Context, key: String, now: Long = System.currentTimeMillis()): Boolean =
        synchronized(lock) {
            val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            val handled = prefs.getBoolean("handled:$key", false)
            val lastDelivered = prefs.getLong("delivered:$key", 0L)
            if (AlarmDeliveryPolicy.isDuplicate(handled, lastDelivered, now)) return@synchronized false
            prefs.edit().putLong("delivered:$key", now).commit()
        }

    fun markHandled(context: Context, key: String) {
        synchronized(lock) {
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putBoolean("handled:$key", true)
                .remove("delivered:$key")
                .commit()
        }
    }

    fun isHandled(context: Context, key: String): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean("handled:$key", false)

    fun clear(context: Context, key: String) {
        synchronized(lock) {
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .remove("handled:$key")
                .remove("delivered:$key")
                .commit()
        }
    }

    fun clearAll(context: Context) {
        synchronized(lock) {
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit()
        }
    }
}
