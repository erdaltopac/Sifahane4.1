package com.hazerfen.sifahane.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.hazerfen.sifahane.SifahaneApp
import com.hazerfen.sifahane.data.*
import kotlinx.coroutines.flow.*

/** Ana sağlık ekranlarının tek yönlü, profil bazlı okunur UI veri kaynağı. */
class SifahaneViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val repositories = (application as SifahaneApp).repositories
    private val activeProfileId = savedStateHandle.getStateFlow("active_profile_id", 0L)

    val profiles: Flow<List<UserProfile>> = repositories.profiles.profiles
    val allMedications: Flow<List<Medication>> = repositories.medications.all

    val medications: Flow<List<Medication>> = activeProfileId.flatMapLatest { id ->
        if (id > 0) repositories.medications.active(id) else flowOf(emptyList())
    }
    val archivedMedications: Flow<List<Medication>> = activeProfileId.flatMapLatest { id ->
        if (id > 0) repositories.medications.archive(id) else flowOf(emptyList())
    }
    val doseLogs: Flow<List<DoseLog>> = activeProfileId.flatMapLatest { id ->
        if (id > 0) repositories.health.doseLogs(id) else flowOf(emptyList())
    }
    val bloodPressure: Flow<List<BloodPressure>> = activeProfileId.flatMapLatest { id ->
        if (id > 0) repositories.health.bloodPressure(id) else flowOf(emptyList())
    }
    val glucose: Flow<List<BloodGlucose>> = activeProfileId.flatMapLatest { id ->
        if (id > 0) repositories.health.glucose(id) else flowOf(emptyList())
    }
    val appointments: Flow<List<Appointment>> = activeProfileId.flatMapLatest { id ->
        if (id > 0) repositories.appointments.appointments(id) else flowOf(emptyList())
    }
    val reportGroups: Flow<List<ReportGroup>> = activeProfileId.flatMapLatest { id ->
        if (id > 0) repositories.reports.groups(id) else flowOf(emptyList())
    }

    fun selectProfile(profileId: Long) {
        savedStateHandle["active_profile_id"] = profileId.coerceAtLeast(0L)
    }
}
