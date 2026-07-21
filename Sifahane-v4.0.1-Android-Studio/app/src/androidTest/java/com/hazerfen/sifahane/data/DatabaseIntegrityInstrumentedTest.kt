package com.hazerfen.sifahane.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseIntegrityInstrumentedTest {
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun profileDeleteCascadesAndIndicesExist() = runBlocking {
        val profileId = db.profileDao().insert(UserProfile(name = "Test"))
        val medicationId = db.medicationDao().insert(
            Medication(
                profileId = profileId,
                name = "İlaç",
                dose = "1",
                timesCsv = "09:00",
                stock = 1,
                startDate = "2026-01-01"
            )
        )
        db.doseLogDao().insert(
            DoseLog(
                profileId = profileId,
                medicationId = medicationId,
                medicationName = "İlaç",
                scheduledDateTime = 1L,
                action = "ALINDI"
            )
        )
        assertEquals(2, db.profileDao().relatedRecordCount(profileId))
        db.profileDao().delete(requireNotNull(db.profileDao().byId(profileId)))
        assertEquals(0, db.profileDao().count())

        val indexNames = mutableSetOf<String>()
        db.openHelper.writableDatabase.query("SELECT name FROM sqlite_master WHERE type='index'").use { cursor ->
            while (cursor.moveToNext()) indexNames += cursor.getString(0)
        }
        assertTrue("index_medications_profileId_active_archived" in indexNames)
        assertTrue("index_appointments_profileId_active_status_appointmentDateTime" in indexNames)
        assertTrue("index_dose_logs_profileId_timestamp" in indexNames)
    }
}
