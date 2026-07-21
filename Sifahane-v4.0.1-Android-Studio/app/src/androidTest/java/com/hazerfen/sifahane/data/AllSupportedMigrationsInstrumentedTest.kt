package com.hazerfen.sifahane.data

import android.content.Context
import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AllSupportedMigrationsInstrumentedTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java
    )
    private val createdDatabases = mutableListOf<String>()

    @After
    fun cleanUp() {
        createdDatabases.forEach(context::deleteDatabase)
    }

    @Test
    fun everySupportedSchemaReachesVersion11() {
        val migrations = listOf(
            AppDatabase.MIGRATION_3_4,
            AppDatabase.MIGRATION_4_5,
            AppDatabase.MIGRATION_5_6,
            AppDatabase.MIGRATION_6_7,
            AppDatabase.MIGRATION_7_8,
            AppDatabase.MIGRATION_8_9,
            AppDatabase.MIGRATION_9_10,
            AppDatabase.MIGRATION_10_11
        )
        for (startVersion in 3..10) {
            val name = "migration-$startVersion-to-11"
            createdDatabases += name
            helper.createDatabase(name, startVersion).close()
            val applicable = migrationChain(startVersion, migrations)
            helper.runMigrationsAndValidate(name, 11, true, *applicable.toTypedArray()).close()
        }
    }

    private fun migrationChain(startVersion: Int, all: List<Migration>): List<Migration> {
        var current = startVersion
        val result = mutableListOf<Migration>()
        while (current < 11) {
            val next = all.single { it.startVersion == current }
            result += next
            current = next.endVersion
        }
        return result
    }
}
