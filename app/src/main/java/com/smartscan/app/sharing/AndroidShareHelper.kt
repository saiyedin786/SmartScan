package com.smartscan.app.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.smartscan.app.domain.model.ExportFormat
import java.io.File

object AndroidShareHelper {

    fun shareFile(
        context: Context,
        file: File,
        exportFormat: ExportFormat,
        title: String = "Share Document via SmartScan"
    ) {
        if (!file.exists()) return

        val uri: Uri = FileProvider.getUriForFile(
            context,
            "com.smartscan.app.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = exportFormat.mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, file.nameWithoutExtension)
            putExtra(Intent.EXTRA_TEXT, "Sharing document scanned with SmartScan app.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(intent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }

    fun shareText(
        context: Context,
        text: String,
        subject: String = "Scanned Text"
    ) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        val chooser = Intent.createChooser(intent, "Share Extracted Text").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }

    fun shareMultipleFiles(
        context: Context,
        files: List<File>,
        mimeType: String = "*/*",
        title: String = "Share Documents"
    ) {
        val uris = ArrayList<Uri>()
        files.forEach { file ->
            if (file.exists()) {
                uris.add(
                    FileProvider.getUriForFile(
                        context,
                        "com.smartscan.app.fileprovider",
                        file
                    )
                )
            }
        }
        if (uris.isEmpty()) return

        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = mimeType
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(intent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }
}
