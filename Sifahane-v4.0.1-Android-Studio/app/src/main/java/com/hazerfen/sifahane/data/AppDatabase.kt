package com.hazerfen.sifahane.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

@Database(
    entities = [
        UserProfile::class,
        Medication::class,
        DoseLog::class,
        BloodPressure::class,
        BloodGlucose::class,
        ReportGroup::class,
        Appointment::class
    ],
    version = 11,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun medicationDao(): MedicationDao
    abstract fun doseLogDao(): DoseLogDao
    abstract fun vitalsDao(): VitalsDao
    abstract fun reportGroupDao(): ReportGroupDao
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE profiles ADD COLUMN surname TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE profiles ADD COLUMN photoUri TEXT DEFAULT NULL")
                db.execSQL("ALTER TABLE medications ADD COLUMN archived INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE medications ADD COLUMN barcode TEXT DEFAULT NULL")
                db.execSQL("ALTER TABLE medications ADD COLUMN prospectusUrl TEXT DEFAULT NULL")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE medications ADD COLUMN doctorName TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE medications ADD COLUMN doctorBranch TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE medications ADD COLUMN doctorInstitution TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE medications ADD COLUMN doctorPhone TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE medications ADD COLUMN isReported INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE medications ADD COLUMN reportStartDate TEXT DEFAULT NULL")
                db.execSQL("ALTER TABLE medications ADD COLUMN reportEndDate TEXT DEFAULT NULL")
                db.execSQL("ALTER TABLE medications ADD COLUMN reportWarningDays INTEGER NOT NULL DEFAULT 7")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE profiles ADD COLUMN birthDate TEXT DEFAULT NULL")
                db.execSQL("ALTER TABLE profiles ADD COLUMN bloodGroup TEXT NOT NULL DEFAULT 'Bilinmiyor'")
                db.execSQL("ALTER TABLE profiles ADD COLUMN profileNote TEXT NOT NULL DEFAULT ''")
            }
        }



val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE medications ADD COLUMN reportGroupId INTEGER DEFAULT NULL")
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS report_groups (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                profileId INTEGER NOT NULL,
                name TEXT NOT NULL,
                startDate TEXT NOT NULL,
                endDate TEXT NOT NULL,
                warningDays INTEGER NOT NULL
            )
        """.trimIndent())
    }
}

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE profiles ADD COLUMN role TEXT NOT NULL DEFAULT 'STANDARD'")
                db.execSQL("ALTER TABLE profiles ADD COLUMN adminPinHash TEXT DEFAULT NULL")
                db.execSQL("ALTER TABLE profiles ADD COLUMN permissionsCsv TEXT NOT NULL DEFAULT 'OWN_DATA,MEDICATIONS,MEASUREMENTS,REPORTS,EXPORT_BACKUP'")
                db.execSQL("ALTER TABLE profiles ADD COLUMN accountEnabled INTEGER NOT NULL DEFAULT 1")
                db.execSQL("UPDATE profiles SET role='ADMIN', permissionsCsv='ALL' WHERE id=(SELECT MIN(id) FROM profiles)")
            }
        }


        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS appointments (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        profileId INTEGER NOT NULL,
                        doctorName TEXT NOT NULL,
                        branch TEXT NOT NULL,
                        institution TEXT NOT NULL,
                        clinic TEXT NOT NULL,
                        appointmentDateTime INTEGER NOT NULL,
                        phone TEXT NOT NULL,
                        address TEXT NOT NULL,
                        note TEXT NOT NULL,
                        status TEXT NOT NULL,
                        remindersCsv TEXT NOT NULL,
                        active INTEGER NOT NULL,
                        source TEXT NOT NULL,
                        createdAt INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Existing records remain NULL because older versions did not
                // record whether the stock decrement actually happened.
                db.execSQL("ALTER TABLE dose_logs ADD COLUMN stockDecreased INTEGER")
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // İlişkili tablolara açık foreign-key davranışı ve sorgu indeksleri eklenir.
                // Kopyalama yalnız mevcut bir profile bağlı satırları alır; geçmiş
                // sürümlerden kalmış yetim satırlar güvenli biçimde dışarıda bırakılır.
                db.execSQL("""
                    CREATE TABLE report_groups_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        profileId INTEGER NOT NULL,
                        name TEXT NOT NULL,
                        startDate TEXT NOT NULL,
                        endDate TEXT NOT NULL,
                        warningDays INTEGER NOT NULL,
                        FOREIGN KEY(profileId) REFERENCES profiles(id) ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("""
                    INSERT INTO report_groups_new(id, profileId, name, startDate, endDate, warningDays)
                    SELECT rg.id, rg.profileId, rg.name, rg.startDate, rg.endDate, rg.warningDays
                    FROM report_groups rg INNER JOIN profiles p ON p.id = rg.profileId
                """.trimIndent())
                db.execSQL("DROP TABLE report_groups")
                db.execSQL("ALTER TABLE report_groups_new RENAME TO report_groups")
                db.execSQL("CREATE INDEX index_report_groups_profileId ON report_groups(profileId)")
                db.execSQL("CREATE INDEX index_report_groups_profileId_name ON report_groups(profileId, name)")

                db.execSQL("""
                    CREATE TABLE medications_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        profileId INTEGER NOT NULL,
                        name TEXT NOT NULL,
                        purpose TEXT NOT NULL,
                        dose TEXT NOT NULL,
                        timesCsv TEXT NOT NULL,
                        stock INTEGER NOT NULL,
                        lowStockLimit INTEGER NOT NULL,
                        photoUri TEXT,
                        notes TEXT NOT NULL,
                        startDate TEXT NOT NULL,
                        endDate TEXT,
                        continuous INTEGER NOT NULL,
                        active INTEGER NOT NULL,
                        archived INTEGER NOT NULL,
                        barcode TEXT,
                        prospectusUrl TEXT,
                        doctorName TEXT NOT NULL,
                        doctorBranch TEXT NOT NULL,
                        doctorInstitution TEXT NOT NULL,
                        doctorPhone TEXT NOT NULL,
                        isReported INTEGER NOT NULL,
                        reportStartDate TEXT,
                        reportEndDate TEXT,
                        reportWarningDays INTEGER NOT NULL,
                        reportGroupId INTEGER,
                        FOREIGN KEY(profileId) REFERENCES profiles(id) ON DELETE CASCADE,
                        FOREIGN KEY(reportGroupId) REFERENCES report_groups(id) ON DELETE SET NULL
                    )
                """.trimIndent())
                db.execSQL("""
                    INSERT INTO medications_new(
                        id, profileId, name, purpose, dose, timesCsv, stock, lowStockLimit,
                        photoUri, notes, startDate, endDate, continuous, active, archived,
                        barcode, prospectusUrl, doctorName, doctorBranch, doctorInstitution,
                        doctorPhone, isReported, reportStartDate, reportEndDate,
                        reportWarningDays, reportGroupId
                    )
                    SELECT m.id, m.profileId, m.name, m.purpose, m.dose, m.timesCsv,
                        CASE WHEN m.stock < 0 THEN 0 ELSE m.stock END,
                        CASE WHEN m.lowStockLimit < 0 THEN 0 ELSE m.lowStockLimit END,
                        m.photoUri, m.notes, m.startDate, m.endDate, m.continuous, m.active,
                        m.archived, m.barcode, m.prospectusUrl, m.doctorName, m.doctorBranch,
                        m.doctorInstitution, m.doctorPhone, m.isReported, m.reportStartDate,
                        m.reportEndDate, m.reportWarningDays,
                        CASE WHEN rg.id IS NULL OR rg.profileId != m.profileId THEN NULL ELSE rg.id END
                    FROM medications m
                    INNER JOIN profiles p ON p.id = m.profileId
                    LEFT JOIN report_groups rg ON rg.id = m.reportGroupId
                """.trimIndent())
                db.execSQL("DROP TABLE medications")
                db.execSQL("ALTER TABLE medications_new RENAME TO medications")
                db.execSQL("CREATE INDEX index_medications_profileId ON medications(profileId)")
                db.execSQL("CREATE INDEX index_medications_reportGroupId ON medications(reportGroupId)")
                db.execSQL("CREATE INDEX index_medications_profileId_active_archived ON medications(profileId, active, archived)")
                db.execSQL("CREATE INDEX index_medications_name ON medications(name)")

                db.execSQL("""
                    CREATE TABLE dose_logs_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        profileId INTEGER NOT NULL,
                        medicationId INTEGER NOT NULL,
                        medicationName TEXT NOT NULL,
                        scheduledDateTime INTEGER NOT NULL,
                        actualDateTime INTEGER,
                        action TEXT NOT NULL,
                        stockDecreased INTEGER,
                        timestamp INTEGER NOT NULL,
                        FOREIGN KEY(profileId) REFERENCES profiles(id) ON DELETE CASCADE,
                        FOREIGN KEY(medicationId) REFERENCES medications(id) ON DELETE NO ACTION
                    )
                """.trimIndent())
                db.execSQL("""
                    INSERT INTO dose_logs_new(id, profileId, medicationId, medicationName,
                        scheduledDateTime, actualDateTime, action, stockDecreased, timestamp)
                    SELECT d.id, d.profileId, d.medicationId, d.medicationName,
                        d.scheduledDateTime, d.actualDateTime, d.action, d.stockDecreased, d.timestamp
                    FROM dose_logs d
                    INNER JOIN profiles p ON p.id = d.profileId
                    INNER JOIN medications m ON m.id = d.medicationId AND m.profileId = d.profileId
                """.trimIndent())
                db.execSQL("DROP TABLE dose_logs")
                db.execSQL("ALTER TABLE dose_logs_new RENAME TO dose_logs")
                db.execSQL("CREATE INDEX index_dose_logs_profileId ON dose_logs(profileId)")
                db.execSQL("CREATE INDEX index_dose_logs_medicationId ON dose_logs(medicationId)")
                db.execSQL("CREATE INDEX index_dose_logs_scheduledDateTime ON dose_logs(scheduledDateTime)")
                db.execSQL("CREATE INDEX index_dose_logs_profileId_timestamp ON dose_logs(profileId, timestamp)")

                recreateVitalsTable(db, "blood_pressure", "systolic INTEGER NOT NULL, diastolic INTEGER NOT NULL, pulse INTEGER", "systolic, diastolic, pulse")
                recreateVitalsTable(db, "blood_glucose", "valueMgDl INTEGER NOT NULL, measurementType TEXT NOT NULL", "valueMgDl, measurementType")

                db.execSQL("""
                    CREATE TABLE appointments_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        profileId INTEGER NOT NULL,
                        doctorName TEXT NOT NULL,
                        branch TEXT NOT NULL,
                        institution TEXT NOT NULL,
                        clinic TEXT NOT NULL,
                        appointmentDateTime INTEGER NOT NULL,
                        phone TEXT NOT NULL,
                        address TEXT NOT NULL,
                        note TEXT NOT NULL,
                        status TEXT NOT NULL,
                        remindersCsv TEXT NOT NULL,
                        active INTEGER NOT NULL,
                        source TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        FOREIGN KEY(profileId) REFERENCES profiles(id) ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("""
                    INSERT INTO appointments_new SELECT a.* FROM appointments a
                    INNER JOIN profiles p ON p.id = a.profileId
                """.trimIndent())
                db.execSQL("DROP TABLE appointments")
                db.execSQL("ALTER TABLE appointments_new RENAME TO appointments")
                db.execSQL("CREATE INDEX index_appointments_profileId ON appointments(profileId)")
                db.execSQL("CREATE INDEX index_appointments_appointmentDateTime ON appointments(appointmentDateTime)")
                db.execSQL("CREATE INDEX index_appointments_profileId_active_status_appointmentDateTime ON appointments(profileId, active, status, appointmentDateTime)")
            }

            private fun recreateVitalsTable(
                db: SupportSQLiteDatabase,
                table: String,
                valueColumns: String,
                valueNames: String
            ) {
                val newTable = "${table}_new"
                db.execSQL("""
                    CREATE TABLE $newTable (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        profileId INTEGER NOT NULL,
                        $valueColumns,
                        measuredAt INTEGER NOT NULL,
                        note TEXT NOT NULL,
                        FOREIGN KEY(profileId) REFERENCES profiles(id) ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("""
                    INSERT INTO $newTable(id, profileId, $valueNames, measuredAt, note)
                    SELECT v.id, v.profileId, $valueNames, v.measuredAt, v.note
                    FROM $table v INNER JOIN profiles p ON p.id = v.profileId
                """.trimIndent())
                db.execSQL("DROP TABLE $table")
                db.execSQL("ALTER TABLE $newTable RENAME TO $table")
                db.execSQL("CREATE INDEX index_${table}_profileId ON $table(profileId)")
                db.execSQL("CREATE INDEX index_${table}_measuredAt ON $table(measuredAt)")
                db.execSQL("CREATE INDEX index_${table}_profileId_measuredAt ON $table(profileId, measuredAt)")
            }
        }

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: run {
                    val appContext = context.applicationContext
                    System.loadLibrary("sqlcipher")
                    val passphrase = DatabaseKeyManager.getOrCreate(appContext)
                    PlaintextDatabaseMigrator.migrateIfNeeded(
                        appContext,
                        "sifahane.db",
                        passphrase
                    )
                    val factory = SupportOpenHelperFactory(passphrase.copyOf())
                    passphrase.fill(0)
                    Room.databaseBuilder(
                        appContext,
                        AppDatabase::class.java,
                        "sifahane.db"
                    )
                        .openHelperFactory(factory)
                        .addMigrations(
                            MIGRATION_3_4,
                            MIGRATION_4_5,
                            MIGRATION_5_6,
                            MIGRATION_6_7,
                            MIGRATION_7_8,
                            MIGRATION_8_9,
                            MIGRATION_9_10,
                            MIGRATION_10_11
                        )
                        .build()
                        .also { INSTANCE = it }
                }
            }
    }
}
