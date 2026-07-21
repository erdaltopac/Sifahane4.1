package com.hazerfen.sifahane.data

import android.content.Context
import android.net.Uri
import java.io.File

/**
 * Yalnız Şifahane'nin özel filesDir altındaki yönetilen fotoğraf klasörlerinde çalışır.
 * SAF belgelerine, galeri öğelerine veya başka uygulamaların dosyalarına dokunmaz.
 */
object ManagedPhotoCleaner {
    data class Result(val deleted: Int, val kept: Int)

    suspend fun cleanup(context: Context, db: AppDatabase): Result {
        val profileUris = db.profileDao().allProfiles().mapNotNull(UserProfile::photoUri)
        val medicationUris = db.medicationDao().allMedications().mapNotNull(Medication::photoUri)
        val allUris = profileUris + medicationUris

        val referencedProfileFiles = allUris.mapNotNull { managedContentFileName(it, "profile_photos") }.toSet()
        val referencedMedicationFiles = allUris.mapNotNull { managedContentFileName(it, "medicine_photos") }.toSet()
        val importedRoot = File(context.filesDir, "imported_backup_media").canonicalFile
        val referencedImportedFiles = allUris.mapNotNull { managedImportedFile(it, importedRoot) }.toSet()

        var deleted = 0
        var kept = 0
        listOf(
            "profile_photos" to referencedProfileFiles,
            "medicine_photos" to referencedMedicationFiles
        ).forEach { (folder, referenced) ->
            val root = File(context.filesDir, folder).canonicalFile
            root.listFiles()?.filter(File::isFile)?.forEach { file ->
                if (file.name in referenced) kept++
                else if (file.canonicalFile.parentFile == root && file.delete()) deleted++
            }
        }

        if (importedRoot.exists()) {
            importedRoot.walkBottomUp().forEach { file ->
                val canonical = runCatching { file.canonicalFile }.getOrNull() ?: return@forEach
                if (!canonical.toPath().startsWith(importedRoot.toPath())) return@forEach
                if (canonical.isFile) {
                    if (canonical.path in referencedImportedFiles) kept++
                    else if (canonical.delete()) deleted++
                } else if (canonical != importedRoot && canonical.listFiles().isNullOrEmpty()) {
                    canonical.delete()
                }
            }
        }
        return Result(deleted, kept)
    }

    private fun managedContentFileName(uriText: String, expectedFolder: String): String? {
        val uri = runCatching { Uri.parse(uriText) }.getOrNull() ?: return null
        if (uri.scheme != "content") return null
        val segments = uri.pathSegments
        val folderIndex = segments.indexOf(expectedFolder)
        if (folderIndex < 0 || folderIndex == segments.lastIndex) return null
        return segments.last().takeIf { it.isSafeFileName() }
    }

    private fun managedImportedFile(uriText: String, importedRoot: File): String? {
        val uri = runCatching { Uri.parse(uriText) }.getOrNull() ?: return null
        if (uri.scheme != "file") return null
        val file = runCatching { File(requireNotNull(uri.path)).canonicalFile }.getOrNull() ?: return null
        return file.path.takeIf { file.toPath().startsWith(importedRoot.toPath()) }
    }

    private fun String.isSafeFileName(): Boolean =
        isNotBlank() && '/' !in this && '\\' !in this && this != "." && this != ".."
}
