package com.hazerfen.sifahane

import android.app.Application
import android.os.StrictMode
import android.os.SystemClock
import com.hazerfen.sifahane.alarm.AlarmRescheduler
import com.hazerfen.sifahane.data.AppDatabase
import com.hazerfen.sifahane.repository.SifahaneRepositoryContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SifahaneApp : Application() {
    val database by lazy { AppDatabase.get(this) }
    val repositories by lazy { SifahaneRepositoryContainer(database) }

    private val applicationScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        val startedAt = SystemClock.elapsedRealtime()
        super.onCreate()
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedClosableObjects()
                    .detectLeakedRegistrationObjects()
                    .penaltyLog()
                    .build()
            )
        }
        getSharedPreferences("sifahane_performance", MODE_PRIVATE)
            .edit()
            .putLong("last_application_on_create_ms", SystemClock.elapsedRealtime() - startedAt)
            .apply()
        applicationScope.launch {
            val succeeded = runCatching {
                AlarmRescheduler.refreshAll(applicationContext)
            }.isSuccess
            com.hazerfen.sifahane.alarm.AlarmDiagnostics.recordAutomaticReschedule(
                applicationContext,
                "Uygulama başlatıldı",
                succeeded
            )
        }
    }
}
