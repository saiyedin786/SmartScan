package com.smartscan.app.domain.repository

import android.graphics.Bitmap
import com.smartscan.app.domain.model.CropBounds
import com.smartscan.app.domain.model.Document
import com.smartscan.app.domain.model.DocumentPage
import com.smartscan.app.domain.model.OCRResult
import com.smartscan.app.domain.model.ScanFilter
import java.io.File

interface DocumentRepository {
    suspend fun saveDocument(document: Document): String
    suspend fun getDocumentById(id: String): Document?
    suspend fun getAllDocuments(): List<Document>
    suspend fun searchDocuments(query: String): List<Document>
    suspend fun updateDocument(document: Document)
    suspend fun deleteDocument(id: String)
    suspend fun toggleFavorite(id: String)
}

interface ScannerRepository {
    suspend fun processImage(
        originalFile: File,
        filter: ScanFilter = ScanFilter.AUTO,
        cropBounds: CropBounds = CropBounds(),
        rotationDegrees: Int = 0
    ): File

    suspend fun applyFilter(bitmap: Bitmap, filter: ScanFilter): Bitmap
    suspend fun transformPerspective(bitmap: Bitmap, bounds: CropBounds): Bitmap
}

interface OcrRepository {
    suspend fun extractText(imageFile: File, languageCode: String = "en"): OCRResult
    suspend fun extractTextFromBitmap(bitmap: Bitmap, languageCode: String = "en"): OCRResult
}
