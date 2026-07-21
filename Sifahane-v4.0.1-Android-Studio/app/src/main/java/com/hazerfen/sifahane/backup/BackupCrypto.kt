package com.hazerfen.sifahane.backup

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

internal object BackupCrypto {
    fun isEncrypted(context: Context, uri: Uri): Boolean =
        openUriInput(context, uri).use(BackupCipherCore::isEncrypted)

    fun encryptFile(plainZip: File, encryptedTarget: File, password: CharArray) {
        plainZip.inputStream().use { input ->
            encryptedTarget.outputStream().use { output ->
                BackupCipherCore.encrypt(input, output, password)
            }
        }
    }

    fun openInput(context: Context, uri: Uri, password: CharArray?): InputStream {
        return BackupCipherCore.open(openUriInput(context, uri), password)
    }

    private fun openUriInput(context: Context, uri: Uri): InputStream =
        if (uri.scheme == "file") {
            val path = uri.path ?: throw BackupValidationException.NotSifahaneBackup()
            FileInputStream(path)
        } else {
            context.contentResolver.openInputStream(uri)
                ?: throw BackupValidationException.NotSifahaneBackup()
        }
}
