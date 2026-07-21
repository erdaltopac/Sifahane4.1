package com.hazerfen.sifahane.repository

import com.hazerfen.sifahane.data.*
import kotlinx.coroutines.flow.Flow

/** UI ile Room ayrıntıları arasında özellik bazlı sınırlar. */
class ProfileRepository(private val db: AppDatabase) {
    val profiles: Flow<List<UserProfile>> = db.profileDao().observeAll()
    suspend fun byId(id: Long): UserProfile? = db.profileDao().byId(id)
    suspend fun insert(profile: UserProfile): Long = db.profileDao().insert(profile)
    suspend fun update(profile: UserProfile) = db.profileDao().update(profile)
}

class MedicationRepository(private val db: AppDatabase) {
    val all: Flow<List<Medication>> = db.medicationDao().observeAllMedications()
    fun active(profileId: Long): Flow<List<Medication>> = db.medicationDao().observeForProfile(profileId)
    fun archive(profileId: Long): Flow<List<Medication>> = db.medicationDao().observeArchive(profileId)
    suspend fun insert(medication: Medication): Long = db.medicationDao().insert(medication)
    suspend fun update(medication: Medication) = db.medicationDao().update(medication)
}

class HealthRepository(private val db: AppDatabase) {
    fun doseLogs(profileId: Long): Flow<List<DoseLog>> = db.doseLogDao().observeForProfile(profileId)
    fun bloodPressure(profileId: Long): Flow<List<BloodPressure>> = db.vitalsDao().observeBp(profileId)
    fun glucose(profileId: Long): Flow<List<BloodGlucose>> = db.vitalsDao().observeGlucose(profileId)
}

class AppointmentRepository(private val db: AppDatabase) {
    fun appointments(profileId: Long): Flow<List<Appointment>> = db.appointmentDao().observeForProfile(profileId)
    suspend fun insert(appointment: Appointment): Long = db.appointmentDao().insert(appointment)
    suspend fun update(appointment: Appointment) = db.appointmentDao().update(appointment)
}

class ReportRepository(private val db: AppDatabase) {
    fun groups(profileId: Long): Flow<List<ReportGroup>> = db.reportGroupDao().observeForProfile(profileId)
}

class SifahaneRepositoryContainer(db: AppDatabase) {
    val profiles = ProfileRepository(db)
    val medications = MedicationRepository(db)
    val health = HealthRepository(db)
    val appointments = AppointmentRepository(db)
    val reports = ReportRepository(db)
}
