package com.hazerfen.sifahane.backup

import android.content.Context
import android.net.Uri
import com.hazerfen.sifahane.data.AppDatabase
import com.hazerfen.sifahane.data.DatabaseKeyManager
import java.io.File
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale
import java.util.UUID

data class RestorePoint(
    val file: File,
    val createdAt: Long,
    val automatic: Boolean
)

/**
 * Kullanıcı başına cihaz içi, parola sormayan fakat Keystore kökenli bir
 * anahtarla AES-GCM şifrelenen geri yükleme noktaları.
 */
object RestorePointManager {
    const val MAX_POINTS = 10

    suspend fun create(
        context: Context,
        db: AppDatabase,
        profileId: Long,
        automatic: Boolean = false,
        preserve: File? = null
    ): RestorePoint {
        val password = restorePassword(context)
        val temporary = SifahaneBackupManager.createBackup(context, db, profileId, password)
        val directory = directory(context, profileId).apply { mkdirs() }
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(Date())
        val prefix = if (automatic) "auto" else "manual"
        val target = File(directory, "${prefix}_${stamp}_${UUID.randomUUID()}.sifbak")
        try {
            temporary.inputStream().use { input -> target.outputStream().use { input.copyTo(it) } }
            target.setLastModified(System.currentTimeMillis())
        } finally {
            temporary.delete()
        }
        prune(context, profileId, preserve = preserve ?: target)
        return RestorePoint(target, target.lastModified(), automatic)
    }

    fun list(context: Context, profileId: Long): List<RestorePoint> =
        directory(context, profileId).listFiles()
            .orEmpty()
            .filter { it.isFile && it.extension.equals("sifbak", true) }
            .map { RestorePoint(it, it.lastModified(), it.name.startsWith("auto_")) }
            .sortedByDescending(RestorePoint::createdAt)

    fun delete(point: RestorePoint): Boolean = point.file.delete()

    fun preview(context: Context, point: RestorePoint): BackupPreview =
        SifahaneBackupManager.preview(context, Uri.fromFile(point.file), restorePassword(context))

    suspend fun restore(
        context: Context,
        db: AppDatabase,
        profileId: Long,
        point: RestorePoint
    ): BackupImportResult {
        require(point.file.isFile) { "Geri yükleme noktası bulunamadı." }
        create(context, db, profileId, automatic = true, preserve = point.file)
        val result = SifahaneBackupManager.importBackup(
            context = context,
            db = db,
            uri = Uri.fromFile(point.file),
            mergeIntoProfileId = profileId,
            password = restorePassword(context),
            replaceExistingProfile = true
        )
        prune(context, profileId)
        return result
    }

    private fun directory(context: Context, profileId: Long): File =
        File(context.filesDir, "restore_points/$profileId")

    private fun prune(context: Context, profileId: Long, preserve: File? = null) {
        val files = list(context, profileId).map(RestorePoint::file)
        var retained = files.size
        files.asReversed().forEach { file ->
            if (retained <= MAX_POINTS) return
            if (preserve != null && file.absolutePath == preserve.absolutePath) return@forEach
            if (file.delete()) retained--
        }
    }

    private fun restorePassword(context: Context): CharArray {
        val databaseKey = DatabaseKeyManager.getOrCreate(context)
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.update("SIFAHANE_RESTORE_POINT_V1".toByteArray(Charsets.UTF_8))
            digest.update(databaseKey)
            Base64.getEncoder().withoutPadding().encodeToString(digest.digest()).toCharArray()
        } finally {
            databaseKey.fill(0)
        }
    }
}
