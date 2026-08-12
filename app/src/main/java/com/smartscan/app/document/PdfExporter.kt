package com.smartscan.app.document

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import com.smartscan.app.domain.model.CompressionLevel
import com.smartscan.app.domain.model.DocumentPage
import java.io.File
import java.io.FileOutputStream

object PdfExporter {

    fun generatePdf(
        pages: List<DocumentPage>,
        outputFile: File,
        compressionLevel: CompressionLevel = CompressionLevel.MEDIUM,
        includeOcrTextOverlay: Boolean = true
    ): File {
        val pdfDocument = PdfDocument()

        pages.forEachIndexed { index, page ->
            val bitmap = BitmapFactory.decodeFile(page.imagePath) ?: return@forEachIndexed

            val pageWidth = 595 // Standard A4 width in points
            val pageHeight = 842 // Standard A4 height in points

            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, index + 1).create()
            val pdfPage = pdfDocument.startPage(pageInfo)
            val canvas = pdfPage.canvas

            // Fill white background
            canvas.drawColor(Color.WHITE)

            // Calculate scaled bitmap dimensions preserving aspect ratio
            val srcWidth = bitmap.width
            val srcHeight = bitmap.height
            val scale = (pageWidth.toFloat() / srcWidth).coerceAtMost(pageHeight.toFloat() / srcHeight)

            val destWidth = (srcWidth * scale).toInt()
            val destHeight = (srcHeight * scale).toInt()
            val left = (pageWidth - destWidth) / 2
            val top = (pageHeight - destHeight) / 2

            val srcRect = Rect(0, 0, srcWidth, srcHeight)
            val destRect = Rect(left, top, left + destWidth, top + destHeight)
            val paint = Paint().apply { isFilterBitmap = true }

            canvas.drawBitmap(bitmap, srcRect, destRect, paint)

            // Optional searchable OCR text overlay
            if (includeOcrTextOverlay && page.ocrText.isNotBlank()) {
                val textPaint = Paint().apply {
                    color = Color.TRANSPARENT // Invisible searchable text layer
                    textSize = 10f
                }
                canvas.drawText(page.ocrText.take(100), 10f, (pageHeight - 20).toFloat(), textPaint)
            }

            pdfDocument.finishPage(pdfPage)
        }

        FileOutputStream(outputFile).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        return outputFile
    }
}
