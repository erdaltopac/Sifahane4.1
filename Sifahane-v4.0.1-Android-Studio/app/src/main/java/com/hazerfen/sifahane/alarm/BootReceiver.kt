package com.hazerfen.sifahane.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reason = when (intent.action) {
                    Intent.ACTION_BOOT_COMPLETED -> "Telefon yeniden başlatıldı"
                    Intent.ACTION_MY_PACKAGE_REPLACED -> "Şifahane güncellendi"
                    Intent.ACTION_TIME_CHANGED -> "Sistem saati değişti"
                    Intent.ACTION_TIMEZONE_CHANGED -> "Saat dilimi değişti"
                    Intent.ACTION_DATE_CHANGED -> "Sistem tarihi değişti"
                    else -> intent.action ?: "Sistem olayı"
                }
                val succeeded = runCatching {
                    AlarmRescheduler.refreshAll(context.applicationContext)
                }.isSuccess
                AlarmDiagnostics.recordAutomaticReschedule(context.applicationContext, reason, succeeded)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
