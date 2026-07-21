package com.hazerfen.sifahane.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DoseActionRepositoryInstrumentedTest {
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun repeatedTakeAndUndoAreIdempotent() = runBlocking {
        val profileId = db.profileDao().insert(UserProfile(name = "Test", role = "ADMIN"))
        val medicationId = db.medicationDao().insert(
            Medication(
                profileId = profileId,
                name = "Test ilacı",
                dose = "1 tablet",
                timesCsv = "08:00",
                stock = 5,
                startDate = "2026-01-01",
                continuous = true
            )
        )
        val planned = 1_800_000_000_000L

        val first = DoseActionRepository.recordTaken(
            db, profileId, medicationId, "Test ilacı", planned, planned
        )
        val second = DoseActionRepository.recordTaken(
            db, profileId, medicationId, "Test ilacı", planned, planned + 1_000L
        )

        assertTrue(first.stockChanged)
        assertFalse(first.duplicateSuppressed)
        assertFalse(second.stockChanged)
        assertTrue(second.duplicateSuppressed)
        assertEquals(4, db.medicationDao().byId(medicationId)?.stock)

        val undoFirst = DoseActionRepository.undoTaken(db, first.log, restoreLegacyStock = false)
        val undoSecond = DoseActionRepository.undoTaken(db, first.log, restoreLegacyStock = false)
        assertTrue(undoFirst.stockChanged)
        assertTrue(undoSecond.duplicateSuppressed)
        assertEquals(5, db.medicationDao().byId(medicationId)?.stock)
    }
}
