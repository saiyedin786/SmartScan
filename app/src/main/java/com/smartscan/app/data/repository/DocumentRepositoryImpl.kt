package com.smartscan.app.data.repository

import com.smartscan.app.data.local.db.DocumentDao
import com.smartscan.app.data.local.db.DocumentEntity
import com.smartscan.app.data.local.db.DocumentPageEntity
import com.smartscan.app.data.local.file.FileManager
import com.smartscan.app.domain.model.CropBounds
import com.smartscan.app.domain.model.Document
import com.smartscan.app.domain.model.DocumentPage
import com.smartscan.app.domain.model.ExportFormat
import com.smartscan.app.domain.model.ScanFilter
import com.smartscan.app.domain.repository.DocumentRepository
import java.io.File
import java.util.UUID

class DocumentRepositoryImpl(
    private val documentDao: DocumentDao,
    private val fileManager: FileManager
) : DocumentRepository {

    override suspend fun saveDocument(document: Document): String {
        val id = if (document.id.isBlank()) UUID.randomUUID().toString() else document.id
        val updatedPages = document.pages.mapIndexed { index, page ->
            page.copy(
                id = if (page.id.isBlank()) UUID.randomUUID().toString() else page.id,
                documentId = id,
                pageNumber = index + 1
            )
        }

        val entity = DocumentEntity(
            id = id,
            name = document.name,
            createdAt = document.createdAt,
            updatedAt = System.currentTimeMillis(),
            pageCount = updatedPages.size,
            defaultFormat = document.defaultFormat.name,
            mainFilePath = document.mainFilePath,
            folderId = document.folderId,
            ocrText = document.ocrText,
            isFavorite = document.isFavorite,
            fileSizeFormatted = document.fileSizeFormatted
        )

        val pageEntities = updatedPages.map { page ->
            DocumentPageEntity(
                id = page.id,
                documentId = id,
                pageNumber = page.pageNumber,
                imagePath = page.imagePath,
                rotationDegrees = page.rotationDegrees,
                filterName = page.filter.name,
                ocrText = page.ocrText
            )
        }

        documentDao.insertDocument(entity)
        documentDao.deletePagesForDocument(id)
        documentDao.insertPages(pageEntities)

        return id
    }

    override suspend fun getDocumentById(id: String): Document? {
        val entity = documentDao.getDocumentById(id) ?: return null
        val pageEntities = documentDao.getPagesForDocument(id)
        return mapToDomain(entity, pageEntities)
    }

    override suspend fun getAllDocuments(): List<Document> {
        val entities = documentDao.getAllDocuments()
        return entities.map { entity ->
            val pageEntities = documentDao.getPagesForDocument(entity.id)
            mapToDomain(entity, pageEntities)
        }
    }

    override suspend fun searchDocuments(query: String): List<Document> {
        val entities = documentDao.searchDocuments(query)
        return entities.map { entity ->
            val pageEntities = documentDao.getPagesForDocument(entity.id)
            mapToDomain(entity, pageEntities)
        }
    }

    override suspend fun updateDocument(document: Document) {
        saveDocument(document)
    }

    override suspend fun deleteDocument(id: String) {
        val doc = getDocumentById(id)
        if (doc != null) {
            val dir = fileManager.getDocumentDirectory(id)
            dir.deleteRecursively()
        }
        documentDao.deleteDocument(id)
    }

    override suspend fun toggleFavorite(id: String) {
        documentDao.toggleFavorite(id)
    }

    private fun mapToDomain(entity: DocumentEntity, pageEntities: List<DocumentPageEntity>): Document {
        val pages = pageEntities.map { pageEntity ->
            DocumentPage(
                id = pageEntity.id,
                documentId = pageEntity.documentId,
                pageNumber = pageEntity.pageNumber,
                imagePath = pageEntity.imagePath,
                rotationDegrees = pageEntity.rotationDegrees,
                filter = runCatching { ScanFilter.valueOf(pageEntity.filterName) }.getOrDefault(ScanFilter.AUTO),
                ocrText = pageEntity.ocrText
            )
        }

        return Document(
            id = entity.id,
            name = entity.name,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            pageCount = entity.pageCount,
            defaultFormat = runCatching { ExportFormat.valueOf(entity.defaultFormat) }.getOrDefault(ExportFormat.PDF),
            mainFilePath = entity.mainFilePath,
            folderId = entity.folderId,
            ocrText = entity.ocrText,
            isFavorite = entity.isFavorite,
            fileSizeFormatted = entity.fileSizeFormatted,
            pages = pages
        )
    }
}
