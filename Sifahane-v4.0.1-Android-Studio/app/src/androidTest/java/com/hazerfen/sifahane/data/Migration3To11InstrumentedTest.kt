package com.hazerfen.sifahane.data

import android.content.Context
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Migration3To11InstrumentedTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java
    )
    private val dbName = "migration-3-11-test"

    @After
    fun cleanUp() {
        context.deleteDatabase(dbName)
    }

    @Test
    fun version3DataMigratesTo11WithoutOrphans() {
        helper.createDatabase(dbName, 3).apply {
            execSQL("INSERT INTO profiles(id,name,relation) VALUES(1,'Test','Kendim')")
            execSQL(
                """
                INSERT INTO medications(
                    id,profileId,name,purpose,dose,timesCsv,stock,lowStockLimit,photoUri,
                    notes,startDate,endDate,continuous,active
                ) VALUES(1,1,'İlaç','','1 tablet','08:00',5,2,NULL,'','2026-01-01',NULL,1,1)
                """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO dose_logs(
                    id,profileId,medicationId,medicationName,scheduledDateTime,
                    actualDateTime,action,timestamp
                ) VALUES(1,1,1,'İlaç',1000,1000,'ALINDI',1000)
                """.trimIndent()
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate(
            dbName,
            11,
            true,
            AppDatabase.MIGRATION_3_4,
            AppDatabase.MIGRATION_4_5,
            AppDatabase.MIGRATION_5_6,
            AppDatabase.MIGRATION_6_7,
            AppDatabase.MIGRATION_7_8,
            AppDatabase.MIGRATION_8_9,
            AppDatabase.MIGRATION_9_10,
            AppDatabase.MIGRATION_10_11
        )
        migrated.use {
            assertEquals(1, count(it, "profiles"))
            assertEquals(1, count(it, "medications"))
            assertEquals(1, count(it, "dose_logs"))
            assertTrue(foreignKeyTargets(it, "dose_logs").containsAll(setOf("profiles", "medications")))
            assertTrue(indexNames(it).contains("index_medications_profileId_active_archived"))
        }
    }

    @Test
    fun version10InvalidRowsAreSafelyExcluded() {
        helper.createDatabase(dbName, 10).apply {
            execSQL(
                """
                INSERT INTO profiles(
                    id,name,relation,surname,photoUri,birthDate,bloodGroup,profileNote,
                    role,adminPinHash,permissionsCsv,accountEnabled
                ) VALUES(1,'Test','Kendim','',NULL,NULL,'Bilinmiyor','','ADMIN',NULL,'ALL',1)
                """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO medications(
                    id,profileId,name,purpose,dose,timesCsv,stock,lowStockLimit,photoUri,
                    notes,startDate,endDate,continuous,active,archived,barcode,prospectusUrl,
                    doctorName,doctorBranch,doctorInstitution,doctorPhone,isReported,
                    reportStartDate,reportEndDate,reportWarningDays,reportGroupId
                ) VALUES(1,1,'İlaç','','1','08:00',-4,-2,NULL,'','2026-01-01',NULL,1,1,0,
                    NULL,NULL,'','','','',0,NULL,NULL,7,NULL)
                """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO dose_logs(
                    id,profileId,medicationId,medicationName,scheduledDateTime,
                    actualDateTime,action,stockDecreased,timestamp
                ) VALUES(1,1,999,'Yetim ilaç',1000,NULL,'ALINDI',1,1000)
                """.trimIndent()
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate(
            dbName,
            11,
            true,
            AppDatabase.MIGRATION_10_11
        )
        migrated.use {
            assertEquals(0, count(it, "dose_logs"))
            it.query("SELECT stock,lowStockLimit FROM medications WHERE id=1").use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(0, cursor.getInt(0))
                assertEquals(0, cursor.getInt(1))
            }
        }
    }

    private fun count(db: SupportSQLiteDatabase, table: String): Int =
        db.query("SELECT COUNT(*) FROM $table").use { cursor ->
            cursor.moveToFirst()
            cursor.getInt(0)
        }

    private fun foreignKeyTargets(db: SupportSQLiteDatabase, table: String): Set<String> =
        db.query("PRAGMA foreign_key_list(`$table`)").use { cursor ->
            buildSet {
                val index = cursor.getColumnIndexOrThrow("table")
                while (cursor.moveToNext()) add(cursor.getString(index))
            }
        }

    private fun indexNames(db: SupportSQLiteDatabase): Set<String> =
        db.query("SELECT name FROM sqlite_master WHERE type='index'").use { cursor ->
            buildSet { while (cursor.moveToNext()) add(cursor.getString(0)) }
        }
}
