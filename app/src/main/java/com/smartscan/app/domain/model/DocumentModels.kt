package com.smartscan.app.domain.model

import java.io.File

enum class ScanFilter(val label: String) {
    ORIGINAL("Original"),
    AUTO("Auto Enhance"),
    DOCUMENT("Document Clean"),
    BLACK_WHITE("Black & White"),
    GRAYSCALE("Grayscale"),
    COLOR("Vivid Color"),
    HIGH_CONTRAST("High Contrast"),
    LOW_LIGHT("Low Light")
}

enum class ExportFormat(val extension: String, val mimeType: String, val displayName: String) {
    PDF("pdf", "application/pdf", "PDF Document"),
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "Microsoft Word"),
    JPG("jpg", "image/jpeg", "JPEG Image"),
    PNG("png", "image/png", "PNG Image"),
    TXT("txt", "text/plain", "Plain Text")
}

enum class CompressionLevel(val quality: Int, val label: String) {
    HIGH(95, "High Quality (Largest size)"),
    MEDIUM(75, "Balanced (Recommended)"),
    SMALL(50, "Small Size (Compressed)")
}

data class PointF(val x: Float, val y: Float)

data class CropBounds(
    val topLeft: PointF = PointF(0.05f, 0.05f),
    val topRight: PointF = PointF(0.95f, 0.05f),
    val bottomRight: PointF = PointF(0.95f, 0.95f),
    val bottomLeft: PointF = PointF(0.05f, 0.95f)
)

data class DocumentPage(
    val id: String,
    val documentId: String,
    val pageNumber: Int,
    val imagePath: String,
    val rotationDegrees: Int = 0,
    val filter: ScanFilter = ScanFilter.AUTO,
    val cropBounds: CropBounds = CropBounds(),
    val ocrText: String = ""
)

data class Document(
    val id: String,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val pageCount: Int = 1,
    val defaultFormat: ExportFormat = ExportFormat.PDF,
    val mainFilePath: String = "",
    val folderId: String? = null,
    val ocrText: String = "",
    val isFavorite: Boolean = false,
    val fileSizeFormatted: String = "0 KB",
    val pages: List<DocumentPage> = emptyList()
)

data class OCRResult(
    val fullText: String,
    val paragraphs: List<String> = emptyList(),
    val languageCode: String = "en",
    val confidence: Float = 0.95f
)
