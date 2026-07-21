package com.hazerfen.sifahane.backup

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.room.withTransaction
import com.hazerfen.sifahane.BuildConfig
import com.hazerfen.sifahane.alarm.AlarmRescheduler
import com.hazerfen.sifahane.data.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

private const val FORMAT = "SifahaneBackup"
private const val FORMAT_VERSION = 4
private const val BACKUP_JSON = "backup.json"
private const val PREFS = "sifahane_backup_preferences"
private const val PREF_TREE_URI = "backup_tree_uri"

data class BackupDocument(val uri: Uri, val name: String, val size: Long, val modifiedAt: Long)
data class BackupPreview(
    val uri: Uri,
    val profileName: String,
    val birthDate: String?,
    val bloodGroup: String,
    val createdAt: String,
    val appVersion: String,
    val medicationCount: Int,
    val doseLogCount: Int,
    val bloodPressureCount: Int,
    val glucoseCount: Int,
    val reportGroupCount: Int,
    val appointmentCount: Int,
    val photoCount: Int
)

data class BackupImportResult(
    val profileId: Long,
    val insertedCount: Int,
    val skippedDuplicateCount: Int
) {
    fun summary(): String = when {
        skippedDuplicateCount > 0 -> "$insertedCount kayıt eklendi, $skippedDuplicateCount özdeş kayıt atlandı."
        else -> "$insertedCount kayıt eklendi."
    }
}

private data class StagedMedia(val entryName: String, val file: File, val sha256: String)
private data class ExtractedBackup(
    val root: JSONObject,
    val media: Map<String, File>,
    val temporaryDirectory: File?
) : Closeable {
    override fun close() {
        temporaryDirectory?.deleteRecursively()
    }
}

object SifahaneBackupManager {
    fun savedTreeUri(context: Context): Uri? = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        .getString(PREF_TREE_URI, null)?.let(Uri::parse)

    fun saveTreeUri(context: Context, uri: Uri) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(PREF_TREE_URI, uri.toString()).apply()
    }

    suspend fun createBackup(
        context: Context,
        db: AppDatabase,
        profileId: Long,
        password: CharArray
    ): File {
        val profile = db.profileDao().byId(profileId) ?: error("Kişi bulunamadı.")
        val medications = db.medicationDao().allForProfile(profileId)
        val logs = db.doseLogDao().allForProfile(profileId)
        val bloodPressure = db.vitalsDao().allBpForExport(profileId)
        val glucose = db.vitalsDao().allGlucoseForExport(profileId)
        val groups = db.reportGroupDao().allForProfile(profileId)
        val appointments = db.appointmentDao().allForProfile(profileId)

        val cache = File(context.cacheDir, "sifahane_backups").apply { mkdirs() }
        val stageDir = File(cache, ".media_${UUID.randomUUID()}").apply { mkdirs() }
        val stagedMedia = linkedMapOf<String, StagedMedia>()

        fun stageMedia(uriText: String?, entryName: String): String? {
            if (uriText.isNullOrBlank()) return null
            return runCatching {
                val target = File(stageDir, entryName.substringAfterLast('/'))
                val digest = MessageDigest.getInstance("SHA-256")
                context.contentResolver.openInputStream(Uri.parse(uriText))?.use { input ->
                    FileOutputStream(target).use { output ->
                        input.copyLimited(output, BackupArchivePolicy.MAX_MEDIA_BYTES, digest)
                    }
                } ?: return@runCatching null
                stagedMedia[entryName] = StagedMedia(entryName, target, digest.hexDigest())
                entryName
            }.getOrNull()
        }

        val profilePhoto = stageMedia(profile.photoUri, "profile_photos/profile_${profile.id}.jpg")
        val medicationPhotos = mutableMapOf<Long, String>()
        medications.forEach { medication ->
            stageMedia(
                medication.photoUri,
                "medicine_photos/medicine_${medication.id}.jpg"
            )?.let { medicationPhotos[medication.id] = it }
        }

        val payload = JSONObject().apply {
            put("profile", JSONObject().apply {
                put("originalProfileId", profile.id)
                put("name", profile.name)
                put("surname", profile.surname)
                put("relation", profile.relation)
                putN("birthDate", profile.birthDate)
                put("bloodGroup", profile.bloodGroup)
                put("profileNote", profile.profileNote)
                putN("photoEntry", profilePhoto)
            })
            put("medications", JSONArray().apply {
                medications.forEach { medication ->
                    put(JSONObject().apply {
                        put("oldId", medication.id)
                        put("name", medication.name)
                        put("purpose", medication.purpose)
                        put("dose", medication.dose)
                        put("timesCsv", medication.timesCsv)
                        put("stock", medication.stock)
                        put("lowStockLimit", medication.lowStockLimit)
                        putN("photoEntry", medicationPhotos[medication.id])
                        put("notes", medication.notes)
                        put("startDate", medication.startDate)
                        putN("endDate", medication.endDate)
                        put("continuous", medication.continuous)
                        put("active", medication.active)
                        put("archived", medication.archived)
                        putN("barcode", medication.barcode)
                        putN("prospectusUrl", medication.prospectusUrl)
                        put("doctorName", medication.doctorName)
                        put("doctorBranch", medication.doctorBranch)
                        put("doctorInstitution", medication.doctorInstitution)
                        put("doctorPhone", medication.doctorPhone)
                        put("isReported", medication.isReported)
                        putN("reportStartDate", medication.reportStartDate)
                        putN("reportEndDate", medication.reportEndDate)
                        put("reportWarningDays", medication.reportWarningDays)
                        putN("reportGroupId", medication.reportGroupId)
                    })
                }
            })
            put("doseLogs", JSONArray().apply {
                logs.forEach { log ->
                    put(JSONObject().apply {
                        put("oldMedicationId", log.medicationId)
                        put("medicationName", log.medicationName)
                        put("scheduledDateTime", log.scheduledDateTime)
                        putN("actualDateTime", log.actualDateTime)
                        put("action", log.action)
                        putN("stockDecreased", log.stockDecreased)
                        put("timestamp", log.timestamp)
                    })
                }
            })
            put("bloodPressure", JSONArray().apply {
                bloodPressure.forEach { value ->
                    put(JSONObject().apply {
                        put("systolic", value.systolic)
                        put("diastolic", value.diastolic)
                        putN("pulse", value.pulse)
                        put("measuredAt", value.measuredAt)
                        put("note", value.note)
                    })
                }
            })
            put("bloodGlucose", JSONArray().apply {
                glucose.forEach { value ->
                    put(JSONObject().apply {
                        put("valueMgDl", value.valueMgDl)
                        put("measurementType", value.measurementType)
                        put("measuredAt", value.measuredAt)
                        put("note", value.note)
                    })
                }
            })
            put("reportGroups", JSONArray().apply {
                groups.forEach { group ->
                    put(JSONObject().apply {
                        put("oldId", group.id)
                        put("name", group.name)
                        put("startDate", group.startDate)
                        put("endDate", group.endDate)
                        put("warningDays", group.warningDays)
                    })
                }
            })
            put("appointments", JSONArray().apply {
                appointments.forEach { appointment ->
                    put(JSONObject().apply {
                        put("doctorName", appointment.doctorName)
                        put("branch", appointment.branch)
                        put("institution", appointment.institution)
                        put("clinic", appointment.clinic)
                        put("appointmentDateTime", appointment.appointmentDateTime)
                        put("phone", appointment.phone)
                        put("address", appointment.address)
                        put("note", appointment.note)
                        put("status", appointment.status)
                        put("remindersCsv", appointment.remindersCsv)
                        put("active", appointment.active)
                        put("source", appointment.source)
                        put("createdAt", appointment.createdAt)
                    })
                }
            })
        }

        val identity = listOf(
            profile.name.trim().lowercase(),
            profile.surname.trim().lowercase(),
            profile.birthDate.orEmpty()
        ).joinToString("|")
        val root = JSONObject().apply {
            put("format", FORMAT)
            put("formatVersion", FORMAT_VERSION)
            put("appVersion", BuildConfig.VERSION_NAME)
            put("createdAt", isoNow())
            put("backupId", UUID.randomUUID().toString())
            put("profileIdentity", JSONObject().apply {
                put("originalProfileId", profile.id)
                put("name", profile.name)
                put("surname", profile.surname)
                putN("birthDate", profile.birthDate)
                put("bloodGroup", profile.bloodGroup)
                put("relation", profile.relation)
                put("profileFingerprint", sha256(identity.toByteArray()))
            })
            put("recordSummary", JSONObject().apply {
                put("medicationCount", medications.size)
                put("doseLogCount", logs.size)
                put("bloodPressureCount", bloodPressure.size)
                put("glucoseCount", glucose.size)
                put("reportGroupCount", groups.size)
                put("appointmentCount", appointments.size)
                put("photoCount", stagedMedia.size)
            })
            put("payloadSha256", sha256(payload.toString().toByteArray(Charsets.UTF_8)))
            put("mediaChecksums", JSONObject().apply {
                stagedMedia.values.forEach { put(it.entryName, it.sha256) }
            })
            put("payload", payload)
        }

        val safe = (profile.name + "_" + profile.surname).trim('_')
            .replace(Regex("[^A-Za-z0-9ÇĞİÖŞÜçğıöşü_-]"), "_")
        val stamp = SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US).format(Date())
        val plain = File(cache, ".Sifahane_${safe}_${stamp}_${UUID.randomUUID()}.zip.tmp")
        val output = File(cache, "Sifahane_${safe}_${stamp}.sifbak")
        try {
            ZipOutputStream(BufferedOutputStream(plain.outputStream())).use { zip ->
                zip.putNextEntry(ZipEntry(BACKUP_JSON))
                zip.write(root.toString(2).toByteArray(Charsets.UTF_8))
                zip.closeEntry()
                stagedMedia.values.forEach { media ->
                    zip.putNextEntry(ZipEntry(media.entryName))
                    media.file.inputStream().buffered().use { it.copyTo(zip) }
                    zip.closeEntry()
                }
            }
            BackupCrypto.encryptFile(plain, output, password)
        } finally {
            plain.delete()
            stageDir.deleteRecursively()
            password.fill('\u0000')
        }
        return output
    }

    fun copyBackupToTree(context: Context, treeUri: Uri, source: File): Uri {
        val selected = DocumentFile.fromTreeUri(context, treeUri)
            ?: error("Yedek klasörüne erişilemedi.")
        val folder = if (selected.name == "Şifahane Yedek") selected
        else selected.findFile("Şifahane Yedek") ?: selected.createDirectory("Şifahane Yedek")
        ?: error("Şifahane Yedek klasörü oluşturulamadı.")
        folder.findFile(source.name)?.delete()
        val target = folder.createFile("application/octet-stream", source.name)
            ?: error("Yedek oluşturulamadı.")
        context.contentResolver.openOutputStream(target.uri, "w")!!.use { output ->
            source.inputStream().use { it.copyTo(output) }
        }
        return target.uri
    }

    fun copyBackupToUri(context: Context, source: File, target: Uri) {
        context.contentResolver.openOutputStream(target, "w")?.use { output ->
            source.inputStream().use { it.copyTo(output) }
        } ?: error("Seçilen hedefe yazılamadı.")
    }

    fun listBackups(context: Context, treeUri: Uri): List<BackupDocument> {
        val selected = DocumentFile.fromTreeUri(context, treeUri) ?: return emptyList()
        val folder = if (selected.name == "Şifahane Yedek") selected
        else selected.findFile("Şifahane Yedek") ?: selected
        return folder.listFiles()
            .filter {
                it.isFile && (
                    it.name?.endsWith(".sifbak", true) == true ||
                    it.name?.endsWith(".sifbackup", true) == true ||
                    it.name?.endsWith(".zip", true) == true
                )
            }
            .map {
                BackupDocument(
                    it.uri,
                    it.name ?: "Şifahane yedeği.zip",
                    it.length(),
                    it.lastModified()
                )
            }
            .sortedByDescending { it.modifiedAt }
    }

    fun isEncrypted(context: Context, uri: Uri): Boolean = BackupCrypto.isEncrypted(context, uri)

    fun preview(context: Context, uri: Uri, password: CharArray? = null): BackupPreview {
        extract(context, uri, mediaToo = false, password = password).use { extracted ->
            val identity = extracted.root.getJSONObject("profileIdentity")
            val summary = extracted.root.getJSONObject("recordSummary")
            return BackupPreview(
                uri = uri,
                profileName = listOf(identity.optString("name"), identity.optString("surname"))
                    .filter(String::isNotBlank).joinToString(" "),
                birthDate = identity.optNS("birthDate"),
                bloodGroup = identity.optString("bloodGroup", "Bilinmiyor"),
                createdAt = extracted.root.optString("createdAt"),
                appVersion = extracted.root.optString("appVersion"),
                medicationCount = summary.optInt("medicationCount"),
                doseLogCount = summary.optInt("doseLogCount"),
                bloodPressureCount = summary.optInt("bloodPressureCount"),
                glucoseCount = summary.optInt("glucoseCount"),
                reportGroupCount = summary.optInt("reportGroupCount"),
                appointmentCount = summary.optInt("appointmentCount"),
                photoCount = summary.optInt("photoCount")
            )
        }
    }

    suspend fun importBackup(
        context: Context,
        db: AppDatabase,
        uri: Uri,
        mergeIntoProfileId: Long?,
        password: CharArray? = null,
        replaceExistingProfile: Boolean = false
    ): BackupImportResult {
        extract(context, uri, mediaToo = true, password = password).use { extracted ->
            val payload = extracted.root.getJSONObject("payload")
            val sourceProfile = payload.getJSONObject("profile")
            val finalMediaDirectory = File(
                context.filesDir,
                "imported_backup_media/${UUID.randomUUID()}"
            ).apply { mkdirs() }
            var importSucceeded = false
            var inserted = 0
            var skipped = 0

            fun restore(entryName: String?): String? {
                val source = entryName?.let(extracted.media::get) ?: return null
                val safeName = entryName.substringAfterLast('/').replace(Regex("[^A-Za-z0-9._-]"), "_")
                val target = File(finalMediaDirectory, safeName)
                source.inputStream().use { input -> target.outputStream().use { input.copyTo(it) } }
                return Uri.fromFile(target).toString()
            }

            try {
                var profileId = 0L
                db.withTransaction {
                    profileId = mergeIntoProfileId ?: db.profileDao().insert(
                        UserProfile(
                            name = sourceProfile.optString("name"),
                            surname = sourceProfile.optString("surname"),
                            relation = sourceProfile.optString("relation"),
                            birthDate = sourceProfile.optNS("birthDate"),
                            bloodGroup = sourceProfile.optString("bloodGroup", "Bilinmiyor"),
                            profileNote = sourceProfile.optString("profileNote"),
                            photoUri = restore(sourceProfile.optNS("photoEntry"))
                        )
                    ).also { inserted++ }

                    var targetProfile = db.profileDao().byId(profileId)
                        ?: throw BackupValidationException.CorruptBackup()
                    if (mergeIntoProfileId != null && replaceExistingProfile) {
                        db.profileDao().clearProfileData(profileId)
                        targetProfile = targetProfile.copy(
                            name = sourceProfile.optString("name"),
                            surname = sourceProfile.optString("surname"),
                            relation = sourceProfile.optString("relation"),
                            birthDate = sourceProfile.optNS("birthDate"),
                            bloodGroup = sourceProfile.optString("bloodGroup", "Bilinmiyor"),
                            profileNote = sourceProfile.optString("profileNote"),
                            photoUri = restore(sourceProfile.optNS("photoEntry"))
                        )
                        db.profileDao().update(targetProfile)
                    } else if (mergeIntoProfileId != null && targetProfile.photoUri.isNullOrBlank()) {
                        restore(sourceProfile.optNS("photoEntry"))?.let { restored ->
                            targetProfile = targetProfile.copy(photoUri = restored)
                            db.profileDao().update(targetProfile)
                        }
                    }

                    val existingGroups = db.reportGroupDao().allForProfile(profileId)
                        .associateBy(::reportGroupKey).toMutableMap()
                    val groupMap = mutableMapOf<Long, Long>()
                    val groups = payload.optJSONArray("reportGroups") ?: JSONArray()
                    for (index in 0 until groups.length()) {
                        val value = groups.getJSONObject(index)
                        val candidate = ReportGroup(
                            profileId = profileId,
                            name = value.optString("name"),
                            startDate = value.optString("startDate"),
                            endDate = value.optString("endDate"),
                            warningDays = value.optInt("warningDays", 7)
                        )
                        val key = reportGroupKey(candidate)
                        val existing = existingGroups[key]
                        val mappedId = if (existing != null) {
                            skipped++
                            existing.id
                        } else {
                            db.reportGroupDao().insert(candidate).also { id ->
                                inserted++
                                existingGroups[key] = candidate.copy(id = id)
                            }
                        }
                        groupMap[value.optLong("oldId")] = mappedId
                    }

                    val existingMedications = db.medicationDao().allForProfile(profileId)
                        .associateBy(::medicationKey).toMutableMap()
                    val medicationMap = mutableMapOf<Long, Long>()
                    val medications = payload.optJSONArray("medications") ?: JSONArray()
                    for (index in 0 until medications.length()) {
                        val value = medications.getJSONObject(index)
                        val oldGroup = value.optNL("reportGroupId")
                        val candidate = Medication(
                            profileId = profileId,
                            name = value.optString("name"),
                            purpose = value.optString("purpose"),
                            dose = value.optString("dose"),
                            timesCsv = value.optString("timesCsv"),
                            stock = value.optInt("stock").coerceAtLeast(0),
                            lowStockLimit = value.optInt("lowStockLimit", 5).coerceAtLeast(0),
                            photoUri = null,
                            notes = value.optString("notes"),
                            startDate = value.optString("startDate"),
                            endDate = value.optNS("endDate"),
                            continuous = value.optBoolean("continuous"),
                            active = value.optBoolean("active", true),
                            archived = value.optBoolean("archived"),
                            barcode = value.optNS("barcode"),
                            prospectusUrl = value.optNS("prospectusUrl"),
                            doctorName = value.optString("doctorName"),
                            doctorBranch = value.optString("doctorBranch"),
                            doctorInstitution = value.optString("doctorInstitution"),
                            doctorPhone = value.optString("doctorPhone"),
                            isReported = value.optBoolean("isReported"),
                            reportStartDate = value.optNS("reportStartDate"),
                            reportEndDate = value.optNS("reportEndDate"),
                            reportWarningDays = value.optInt("reportWarningDays", 7),
                            reportGroupId = oldGroup?.let(groupMap::get)
                        )
                        val key = medicationKey(candidate)
                        val existing = existingMedications[key]
                        val mappedId = if (existing != null) {
                            skipped++
                            existing.id
                        } else {
                            val withPhoto = candidate.copy(photoUri = restore(value.optNS("photoEntry")))
                            db.medicationDao().insert(withPhoto).also { id ->
                                inserted++
                                existingMedications[key] = withPhoto.copy(id = id)
                            }
                        }
                        medicationMap[value.optLong("oldId")] = mappedId
                    }

                    val existingDoseKeys = db.doseLogDao().allForProfile(profileId)
                        .mapTo(hashSetOf(), ::doseLogKey)
                    val logs = payload.optJSONArray("doseLogs") ?: JSONArray()
                    for (index in 0 until logs.length()) {
                        val value = logs.getJSONObject(index)
                        val oldMedicationId = value.optLong("oldMedicationId")
                        val medicationId = medicationMap[oldMedicationId] ?: run {
                            val originalName = value.optString("medicationName").ifBlank { "Bilinmeyen ilaç" }
                            val marker = "YEDEK_GEÇMİŞ_KAYDI:$oldMedicationId"
                            val existingPlaceholder = existingMedications.values.firstOrNull {
                                it.archived && it.notes == marker
                            }
                            val placeholderId = existingPlaceholder?.id ?: db.medicationDao().insert(
                                Medication(
                                    profileId = profileId,
                                    name = "[Geçmiş kayıt] $originalName",
                                    purpose = "Yedekten korunan geçmiş doz kaydı",
                                    dose = "-",
                                    timesCsv = "",
                                    stock = 0,
                                    lowStockLimit = 0,
                                    notes = marker,
                                    startDate = "1970-01-01",
                                    continuous = true,
                                    active = false,
                                    archived = true
                                )
                            ).also { id ->
                                inserted++
                                val placeholder = Medication(
                                    id = id,
                                    profileId = profileId,
                                    name = "[Geçmiş kayıt] $originalName",
                                    purpose = "Yedekten korunan geçmiş doz kaydı",
                                    dose = "-",
                                    timesCsv = "",
                                    stock = 0,
                                    lowStockLimit = 0,
                                    notes = marker,
                                    startDate = "1970-01-01",
                                    continuous = true,
                                    active = false,
                                    archived = true
                                )
                                existingMedications[medicationKey(placeholder)] = placeholder
                            }
                            medicationMap[oldMedicationId] = placeholderId
                            placeholderId
                        }
                        val candidate = DoseLog(
                            profileId = profileId,
                            medicationId = medicationId,
                            medicationName = value.optString("medicationName"),
                            scheduledDateTime = value.optLong("scheduledDateTime"),
                            actualDateTime = value.optNL("actualDateTime"),
                            action = value.optString("action"),
                            stockDecreased = value.optNB("stockDecreased"),
                            timestamp = value.optLong("timestamp")
                        )
                        if (!existingDoseKeys.add(doseLogKey(candidate))) skipped++
                        else {
                            db.doseLogDao().insert(candidate)
                            inserted++
                        }
                    }

                    val existingBpKeys = db.vitalsDao().allBp(profileId).mapTo(hashSetOf(), ::bpKey)
                    val bp = payload.optJSONArray("bloodPressure") ?: JSONArray()
                    for (index in 0 until bp.length()) {
                        val value = bp.getJSONObject(index)
                        val candidate = BloodPressure(
                            profileId = profileId,
                            systolic = value.optInt("systolic"),
                            diastolic = value.optInt("diastolic"),
                            pulse = value.optNI("pulse"),
                            measuredAt = value.optLong("measuredAt"),
                            note = value.optString("note")
                        )
                        if (!existingBpKeys.add(bpKey(candidate))) skipped++
                        else {
                            db.vitalsDao().insertBp(candidate)
                            inserted++
                        }
                    }

                    val existingGlucoseKeys = db.vitalsDao().allGlucose(profileId)
                        .mapTo(hashSetOf(), ::glucoseKey)
                    val glucose = payload.optJSONArray("bloodGlucose") ?: JSONArray()
                    for (index in 0 until glucose.length()) {
                        val value = glucose.getJSONObject(index)
                        val candidate = BloodGlucose(
                            profileId = profileId,
                            valueMgDl = value.optInt("valueMgDl"),
                            measurementType = value.optString("measurementType"),
                            measuredAt = value.optLong("measuredAt"),
                            note = value.optString("note")
                        )
                        if (!existingGlucoseKeys.add(glucoseKey(candidate))) skipped++
                        else {
                            db.vitalsDao().insertGlucose(candidate)
                            inserted++
                        }
                    }

                    val existingAppointmentKeys = db.appointmentDao().allForProfile(profileId)
                        .mapTo(hashSetOf(), ::appointmentKey)
                    val appointments = payload.optJSONArray("appointments") ?: JSONArray()
                    for (index in 0 until appointments.length()) {
                        val value = appointments.getJSONObject(index)
                        val candidate = Appointment(
                            profileId = profileId,
                            doctorName = value.optString("doctorName"),
                            branch = value.optString("branch"),
                            institution = value.optString("institution"),
                            clinic = value.optString("clinic"),
                            appointmentDateTime = value.optLong("appointmentDateTime"),
                            phone = value.optString("phone"),
                            address = value.optString("address"),
                            note = value.optString("note"),
                            status = AppointmentStatus.fromStorage(value.optString("status", AppointmentStatus.PLANNED.name)).name,
                            remindersCsv = value.optString("remindersCsv", "10080,1440,180"),
                            active = value.optBoolean("active", true),
                            source = value.optString("source", "BACKUP"),
                            createdAt = value.optLong("createdAt", System.currentTimeMillis())
                        )
                        if (!existingAppointmentKeys.add(appointmentKey(candidate))) skipped++
                        else {
                            db.appointmentDao().insert(candidate)
                            inserted++
                        }
                    }
                }
                importSucceeded = true
                runCatching { ManagedPhotoCleaner.cleanup(context, db) }
                runCatching { AlarmRescheduler.refreshAll(context) }
                return BackupImportResult(profileId, inserted, skipped)
            } finally {
                password?.fill('\u0000')
                if (!importSucceeded) finalMediaDirectory.deleteRecursively()
                if (finalMediaDirectory.listFiles().isNullOrEmpty()) finalMediaDirectory.delete()
            }
        }
    }

    private fun extract(
        context: Context,
        uri: Uri,
        mediaToo: Boolean,
        password: CharArray?
    ): ExtractedBackup {
        val temporaryDirectory = if (mediaToo) {
            File(context.cacheDir, "sifahane_extract/${UUID.randomUUID()}").apply { mkdirs() }
        } else null
        val mediaFiles = linkedMapOf<String, File>()
        val seenNames = hashSetOf<String>()
        var backupJson: ByteArray? = null
        var entryCount = 0
        var totalUncompressed = 0L

        try {
            BackupCrypto.openInput(context, uri, password).use { input ->
                ZipInputStream(BufferedInputStream(input)).use { zip ->
                    var entry = zip.nextEntry
                    while (entry != null) {
                        val name = BackupArchivePolicy.normalizeEntryName(entry.name)
                        if (BackupArchivePolicy.isUnsafeEntryName(name)) {
                            throw BackupValidationException.CorruptBackup()
                        }
                        if (!entry.isDirectory) {
                            entryCount++
                            if (entryCount > BackupArchivePolicy.MAX_ENTRIES || !seenNames.add(name)) {
                                throw BackupValidationException.ResourceLimit()
                            }
                            val limit = BackupArchivePolicy.entryLimit(name, BACKUP_JSON)
                            if (entry.size > limit) throw BackupValidationException.ResourceLimit()
                            val openedSize = if (name == BACKUP_JSON) {
                                val bytes = zip.readLimited(limit)
                                backupJson = bytes
                                bytes.size.toLong()
                            } else if (mediaToo) {
                                val target = File(requireNotNull(temporaryDirectory), "entry_${entryCount}")
                                val count = target.outputStream().use { zip.copyLimited(it, limit, null) }
                                mediaFiles[name] = target
                                count
                            } else {
                                zip.copyLimited(NullOutputStream, limit, null)
                            }
                            totalUncompressed += openedSize
                            if (totalUncompressed > BackupArchivePolicy.MAX_TOTAL_UNCOMPRESSED_BYTES) {
                                throw BackupValidationException.ResourceLimit()
                            }
                            if (BackupArchivePolicy.hasSuspiciousCompression(openedSize, entry.compressedSize)) {
                                throw BackupValidationException.ResourceLimit()
                            }
                        }
                        zip.closeEntry()
                        entry = zip.nextEntry
                    }
                }
            }
        } catch (failure: BackupValidationException) {
            temporaryDirectory?.deleteRecursively()
            throw failure
        } catch (failure: Exception) {
            temporaryDirectory?.deleteRecursively()
            val names = generateSequence<Throwable>(failure) { it.cause }
                .map { it::class.java.name }.toList()
            if (names.any { it.contains("AEADBadTag") || it.contains("BadPadding") }) {
                throw BackupValidationException.WrongPassword()
            }
            throw BackupValidationException.NotSifahaneBackup()
        }

        val root = try {
            JSONObject(String(backupJson ?: throw BackupValidationException.NotSifahaneBackup(), Charsets.UTF_8))
        } catch (failure: BackupValidationException) {
            temporaryDirectory?.deleteRecursively()
            throw failure
        } catch (_: Exception) {
            temporaryDirectory?.deleteRecursively()
            throw BackupValidationException.NotSifahaneBackup()
        }
        try {
            if (root.optString("format") != FORMAT) {
                throw BackupValidationException.NotSifahaneBackup()
            }
            val version = root.optInt("formatVersion", -1)
            if (version > FORMAT_VERSION) throw BackupValidationException.UnsupportedVersion()
            if (version <= 0) throw BackupValidationException.CorruptBackup()
            val payload = root.optJSONObject("payload")
                ?: throw BackupValidationException.CorruptBackup()
            if (sha256(payload.toString().toByteArray()) != root.optString("payloadSha256")) {
                throw BackupValidationException.CorruptBackup()
            }
            if (mediaToo) {
                val checksums = root.optJSONObject("mediaChecksums") ?: JSONObject()
                val keys = checksums.keys()
                while (keys.hasNext()) {
                    val name = keys.next()
                    val file = mediaFiles[name] ?: throw BackupValidationException.CorruptBackup()
                    if (sha256(file) != checksums.optString(name)) {
                        throw BackupValidationException.CorruptBackup()
                    }
                }
            }
            return ExtractedBackup(root, mediaFiles, temporaryDirectory)
        } catch (failure: Exception) {
            temporaryDirectory?.deleteRecursively()
            throw failure
        }
    }
}

private object NullOutputStream : OutputStream() {
    override fun write(b: Int) = Unit
    override fun write(b: ByteArray, off: Int, len: Int) = Unit
}

private fun reportGroupKey(value: ReportGroup) = listOf(
    value.name.trim().lowercase(), value.startDate, value.endDate
).joinToString("|")
private fun medicationKey(value: Medication) = listOf(
    value.name.trim().lowercase(), value.dose.trim().lowercase(), value.timesCsv, value.startDate
).joinToString("|")
private fun doseLogKey(value: DoseLog) = listOf(
    value.medicationId, value.scheduledDateTime, value.actualDateTime, value.action, value.timestamp
).joinToString("|")
private fun bpKey(value: BloodPressure) = listOf(
    value.systolic, value.diastolic, value.pulse, value.measuredAt, value.note.trim()
).joinToString("|")
private fun glucoseKey(value: BloodGlucose) = listOf(
    value.valueMgDl, value.measurementType, value.measuredAt, value.note.trim()
).joinToString("|")
private fun appointmentKey(value: Appointment) = listOf(
    value.doctorName.trim().lowercase(), value.institution.trim().lowercase(),
    value.appointmentDateTime, value.branch.trim().lowercase()
).joinToString("|")

private fun JSONObject.putN(key: String, value: Any?) {
    if (value == null) put(key, JSONObject.NULL) else put(key, value)
}
private fun JSONObject.optNS(key: String): String? =
    if (!has(key) || isNull(key)) null else optString(key).takeIf { it.isNotBlank() }
private fun JSONObject.optNB(key: String): Boolean? =
    if (!has(key) || isNull(key)) null else optBoolean(key)
private fun JSONObject.optNL(key: String): Long? =
    if (!has(key) || isNull(key)) null else optLong(key)
private fun JSONObject.optNI(key: String): Int? =
    if (!has(key) || isNull(key)) null else optInt(key)
private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
    .digest(bytes).joinToString("") { "%02x".format(it) }
private fun sha256(file: File): String {
    val digest = MessageDigest.getInstance("SHA-256")
    file.inputStream().buffered().use { input ->
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (true) {
            val read = input.read(buffer)
            if (read < 0) break
            digest.update(buffer, 0, read)
        }
    }
    return digest.hexDigest()
}
private fun MessageDigest.hexDigest(): String = digest().joinToString("") { "%02x".format(it) }
private fun isoNow() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US).format(Date())
private fun InputStream.readLimited(limit: Long): ByteArray {
    val output = ByteArrayOutputStream()
    copyLimited(output, limit, null)
    return output.toByteArray()
}
private fun InputStream.copyLimited(
    output: OutputStream,
    limit: Long,
    digest: MessageDigest?
): Long {
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var total = 0L
    while (true) {
        val read = read(buffer)
        if (read < 0) break
        total += read
        if (total > limit) throw IOException("Dosya çok büyük")
        output.write(buffer, 0, read)
        digest?.update(buffer, 0, read)
    }
    return total
}
