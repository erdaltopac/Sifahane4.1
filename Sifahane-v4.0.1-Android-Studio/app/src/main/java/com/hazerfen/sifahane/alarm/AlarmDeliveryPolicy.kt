package com.hazerfen.sifahane.alarm

/** Android bağımlılığı olmayan, yinelenen alarm teslimi karar tablosu. */
internal object AlarmDeliveryPolicy {
    const val DUPLICATE_WINDOW_MILLIS = 30_000L
    fun isDuplicate(handled: Boolean, lastDeliveredAt: Long, now: Long): Boolean =
        handled || (lastDeliveredAt > 0L && now - lastDeliveredAt in 0 until DUPLICATE_WINDOW_MILLIS)
}
