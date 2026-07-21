package com.hazerfen.sifahane.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val relation: String = "",
    val surname: String = "",
    val photoUri: String? = null,
    val birthDate: String? = null,
    val bloodGroup: String = "Bilinmiyor",
    val profileNote: String = "",
    val role: String = "STANDARD",
    val adminPinHash: String? = null,
    val permissionsCsv: String = "OWN_DATA,MEDICATIONS,MEASUREMENTS,REPORTS,EXPORT_BACKUP",
    val accountEnabled: Boolean = true
)

@Entity(
    tableName = "medications",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ReportGroup::class,
            parentColumns = ["id"],
            childColumns = ["reportGroupId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("profileId"),
        Index("reportGroupId"),
        Index(value = ["profileId", "active", "archived"]),
        Index("name")
    ]
)
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long = 1,
    val name: String,
    val purpose: String = "",
    val dose: String,
    val timesCsv: String,
    val stock: Int,
    val lowStockLimit: Int = 5,
    val photoUri: String? = null,
    val notes: String = "",
    val startDate: String,
    val endDate: String? = null,
    val continuous: Boolean = false,
    val active: Boolean = true,
    val archived: Boolean = false,
    val barcode: String? = null,
    val prospectusUrl: String? = null,
    val doctorName: String = "",
    val doctorBranch: String = "",
    val doctorInstitution: String = "",
    val doctorPhone: String = "",
    val isReported: Boolean = false,
    val reportStartDate: String? = null,
    val reportEndDate: String? = null,
    val reportWarningDays: Int = 7,
    val reportGroupId: Long? = null
)

@Entity(
    tableName = "dose_logs",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Medication::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("profileId"),
        Index("medicationId"),
        Index("scheduledDateTime"),
        Index(value = ["profileId", "timestamp"])
    ]
)
data class DoseLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long,
    val medicationId: Long,
    val medicationName: String,
    val scheduledDateTime: Long,
    val actualDateTime: Long? = null,
    val action: String,
    /**
     * true  = this ALINDI log decreased medication stock by one
     * false = stock was not decreased (for example stock was already zero)
     * null  = legacy log created before v3.3.50; stock effect is unknown
     */
    val stockDecreased: Boolean? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "blood_pressure",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId"), Index("measuredAt"), Index(value = ["profileId", "measuredAt"])]
)
data class BloodPressure(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long,
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int?,
    val measuredAt: Long,
    val note: String = ""
)

@Entity(
    tableName = "blood_glucose",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId"), Index("measuredAt"), Index(value = ["profileId", "measuredAt"])]
)
data class BloodGlucose(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long,
    val valueMgDl: Int,
    val measurementType: String,
    val measuredAt: Long,
    val note: String = ""
)


@Entity(
    tableName = "report_groups",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId"), Index(value = ["profileId", "name"])]
)
data class ReportGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long,
    val name: String,
    val startDate: String,
    val endDate: String,
    val warningDays: Int = 7
)

@Entity(
    tableName = "appointments",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("profileId"),
        Index("appointmentDateTime"),
        Index(value = ["profileId", "active", "status", "appointmentDateTime"])
    ]
)
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long,
    val doctorName: String = "",
    val branch: String = "",
    val institution: String = "",
    val clinic: String = "",
    val appointmentDateTime: Long,
    val phone: String = "",
    val address: String = "",
    val note: String = "",
    val status: String = AppointmentStatus.PLANNED.name,
    val remindersCsv: String = "10080,1440,180",
    val active: Boolean = true,
    val source: String = "MANUAL",
    val createdAt: Long = System.currentTimeMillis()
)

