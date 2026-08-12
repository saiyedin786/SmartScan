package com.smartscan.app.document

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.smartscan.app.domain.model.DocumentPage
import java.io.File
import java.io.FileOutputStream

object ImageAndTextExporter {

    fun exportToJpg(page: DocumentPage, outputFile: File, quality: Int = 90): File {
        val bitmap = BitmapFactory.decodeFile(page.imagePath) ?: return outputFile
        FileOutputStream(outputFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }
        return outputFile
    }

    fun exportToPng(page: DocumentPage, outputFile: File): File {
        val bitmap = BitmapFactory.decodeFile(page.imagePath) ?: return outputFile
        FileOutputStream(outputFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return outputFile
    }

    fun exportToTxt(title: String, pages: List<DocumentPage>, outputFile: File): File {
        val sb = StringBuilder()
        sb.append("=========================================\n")
        sb.append("Document: ").append(title).append("\n")
        sb.append("=========================================\n\n")

        pages.forEachIndexed { index, page ->
            sb.append("--- PAGE ").append(index + 1).append(" ---\n")
            if (page.ocrText.isNotBlank()) {
                sb.append(page.ocrText)
            } else {
                sb.append("[No text recognized for page ").append(index + 1).append("]")
            }
            sb.append("\n\n")
        }

        outputFile.writeText(sb.toString())
        return outputFile
    }
}
