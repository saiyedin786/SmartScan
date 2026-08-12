package com.smartscan.app.data.local.file

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileManager(private val context: Context) {

    private val yearMonthFormat = SimpleDateFormat("yyyy/MM", Locale.US)
    private val timestampFormat = SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.US)

    fun getDocumentDirectory(documentId: String): File {
        val relativeFolder = yearMonthFormat.format(Date())
        val dir = File(context.getExternalFilesDir("documents"), "$relativeFolder/doc_$documentId")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    fun saveBitmap(bitmap: Bitmap, targetFile: File, quality: Int = 90): File {
        FileOutputStream(targetFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }
        return targetFile
    }

    fun loadBitmap(file: File): Bitmap? {
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else null
    }

    fun generateFileName(prefix: String, extension: String): String {
        val timestamp = timestampFormat.format(Date())
        return "${prefix}_${timestamp}.$extension"
    }

    fun getShareableCacheFile(filename: String): File {
        val cacheDir = File(context.cacheDir, "shared_documents")
        if (!cacheDir.exists()) cacheDir.mkdirs()
        return File(cacheDir, filename)
    }

    fun formatFileSize(sizeInBytes: Long): String {
        if (sizeInBytes <= 0) return "0 KB"
        val kb = sizeInBytes / 1024.0
        val mb = kb / 1024.0
        return if (mb >= 1.0) {
            String.format(Locale.US, "%.1f MB", mb)
        } else {
            String.format(Locale.US, "%.0f KB", kb)
        }
    }

    fun clearTempFiles() {
        val cacheDir = File(context.cacheDir, "shared_documents")
        if (cacheDir.exists()) {
            cacheDir.listFiles()?.forEach { it.delete() }
        }
    }
}
