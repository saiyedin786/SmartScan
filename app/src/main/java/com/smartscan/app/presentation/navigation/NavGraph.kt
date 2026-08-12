package com.smartscan.app.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.smartscan.app.data.local.file.FileManager
import com.smartscan.app.document.DocxExporter
import com.smartscan.app.document.ImageAndTextExporter
import com.smartscan.app.document.PdfExporter
import com.smartscan.app.domain.model.Document
import com.smartscan.app.domain.model.DocumentPage
import com.smartscan.app.domain.model.ExportFormat
import com.smartscan.app.domain.model.ScanFilter
import com.smartscan.app.presentation.home.HomeScreen
import com.smartscan.app.presentation.library.DocumentLibraryScreen
import com.smartscan.app.presentation.ocr.OcrEditorScreen
import com.smartscan.app.presentation.onboarding.OnboardingScreen
import com.smartscan.app.presentation.preview.PreviewScreen
import com.smartscan.app.presentation.scanner.ScannerScreen
import com.smartscan.app.presentation.settings.SettingsScreen
import com.smartscan.app.presentation.splash.SplashScreen
import com.smartscan.app.presentation.tools.ToolsScreen
import com.smartscan.app.sharing.AndroidShareHelper
import java.io.File
import java.util.UUID

@Composable
fun SmartScanNavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val fileManager = remember { FileManager(context) }

    // In-memory document state holding scanned items
    var currentDocuments by remember {
        mutableStateOf(
            listOf(
                Document(
                    id = "doc_sample_001",
                    name = "Invoice_August_2026",
                    pageCount = 2,
                    defaultFormat = ExportFormat.PDF,
                    fileSizeFormatted = "420 KB",
                    ocrText = "INVOICE #2026-0813\nTotal Amount: $450.00\nPayment Status: Paid",
                    pages = listOf(
                        DocumentPage(
                            id = "p1",
                            documentId = "doc_sample_001",
                            pageNumber = 1,
                            imagePath = "",
                            ocrText = "INVOICE #2026-0813\nTotal Amount: $450.00"
                        ),
                        DocumentPage(
                            id = "p2",
                            documentId = "doc_sample_001",
                            pageNumber = 2,
                            imagePath = "",
                            ocrText = "Thank you for your business!"
                        )
                    )
                )
            )
        )
    }

    var activeScanPages by remember { mutableStateOf(mutableListOf<DocumentPage>()) }
    var activeDocument by remember { mutableStateOf<Document?>(currentDocuments.firstOrNull()) }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onOnboardingFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                documents = currentDocuments,
                onScanClick = {
                    activeScanPages = mutableListOf()
                    navController.navigate(Screen.Scanner.route)
                },
                onDocumentClick = { doc ->
                    activeDocument = doc
                    navController.navigate(Screen.Preview.createRoute(doc.id))
                },
                onShareDocument = { doc ->
                    val shareFile = fileManager.getShareableCacheFile("${doc.name}.${doc.defaultFormat.extension}")
                    if (doc.defaultFormat == ExportFormat.PDF) {
                        PdfExporter.generatePdf(doc.pages, shareFile)
                    } else if (doc.defaultFormat == ExportFormat.DOCX) {
                        DocxExporter.generateDocx(doc.name, doc.pages, shareFile)
                    } else {
                        ImageAndTextExporter.exportToTxt(doc.name, doc.pages, shareFile)
                    }
                    AndroidShareHelper.shareFile(context, shareFile, doc.defaultFormat)
                },
                onNavigateToLibrary = { navController.navigate(Screen.Library.route) },
                onNavigateToTools = { navController.navigate(Screen.Tools.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Scanner.route) {
            ScannerScreen(
                scannedCount = activeScanPages.size,
                onCapture = {
                    val pageNum = activeScanPages.size + 1
                    val newPage = DocumentPage(
                        id = UUID.randomUUID().toString(),
                        documentId = "",
                        pageNumber = pageNum,
                        imagePath = "",
                        ocrText = "Scanned Page $pageNum Content sample text extracted."
                    )
                    activeScanPages.add(newPage)
                },
                onGalleryImport = {
                    val pageNum = activeScanPages.size + 1
                    val importedPage = DocumentPage(
                        id = UUID.randomUUID().toString(),
                        documentId = "",
                        pageNumber = pageNum,
                        imagePath = "",
                        ocrText = "Gallery Imported Image Page $pageNum text."
                    )
                    activeScanPages.add(importedPage)
                },
                onDoneScanning = {
                    val docId = UUID.randomUUID().toString()
                    val newDoc = Document(
                        id = docId,
                        name = "Scan_${System.currentTimeMillis() / 1000}",
                        pageCount = activeScanPages.size,
                        defaultFormat = ExportFormat.PDF,
                        fileSizeFormatted = "${(activeScanPages.size * 250)} KB",
                        ocrText = activeScanPages.joinToString("\n") { it.ocrText },
                        pages = activeScanPages.mapIndexed { i, p -> p.copy(documentId = docId, pageNumber = i + 1) }
                    )
                    currentDocuments = listOf(newDoc) + currentDocuments
                    activeDocument = newDoc
                    navController.navigate(Screen.Preview.createRoute(docId)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Preview.route) { backStackEntry ->
            val docId = backStackEntry.arguments?.getString("documentId") ?: ""
            val doc = activeDocument ?: currentDocuments.find { it.id == docId } ?: currentDocuments.first()

            PreviewScreen(
                document = doc,
                onAddPage = {
                    navController.navigate(Screen.Scanner.route)
                },
                onRotatePage = { pageIndex ->
                    // Rotate page action
                },
                onDeletePage = { pageIndex ->
                    if (doc.pages.size > 1) {
                        val updatedPages = doc.pages.filterIndexed { index, _ -> index != pageIndex }
                        val updatedDoc = doc.copy(pages = updatedPages, pageCount = updatedPages.size)
                        activeDocument = updatedDoc
                        currentDocuments = currentDocuments.map { if (it.id == doc.id) updatedDoc else it }
                    }
                },
                onApplyFilter = { pageIndex, filter ->
                    val updatedPages = doc.pages.mapIndexed { i, page ->
                        if (i == pageIndex) page.copy(filter = filter) else page
                    }
                    val updatedDoc = doc.copy(pages = updatedPages)
                    activeDocument = updatedDoc
                },
                onPerformOcr = {
                    navController.navigate(Screen.OcrEditor.createRoute(doc.id))
                },
                onExportAndShare = { format ->
                    val exportFile = fileManager.getShareableCacheFile("${doc.name}.${format.extension}")
                    when (format) {
                        ExportFormat.PDF -> PdfExporter.generatePdf(doc.pages, exportFile)
                        ExportFormat.DOCX -> DocxExporter.generateDocx(doc.name, doc.pages, exportFile)
                        ExportFormat.TXT -> ImageAndTextExporter.exportToTxt(doc.name, doc.pages, exportFile)
                        ExportFormat.JPG -> doc.pages.firstOrNull()?.let { ImageAndTextExporter.exportToJpg(it, exportFile) }
                        ExportFormat.PNG -> doc.pages.firstOrNull()?.let { ImageAndTextExporter.exportToPng(it, exportFile) }
                    }
                    AndroidShareHelper.shareFile(context, exportFile, format)
                },
                onBack = { navController.navigate(Screen.Home.route) }
            )
        }

        composable(Screen.OcrEditor.route) { backStackEntry ->
            val docId = backStackEntry.arguments?.getString("documentId") ?: ""
            val doc = activeDocument ?: currentDocuments.find { it.id == docId } ?: currentDocuments.first()

            OcrEditorScreen(
                initialText = doc.ocrText,
                onShareText = { text ->
                    AndroidShareHelper.shareText(context, text)
                },
                onExportToDocx = { text ->
                    val docxFile = fileManager.getShareableCacheFile("${doc.name}.docx")
                    DocxExporter.generateDocx(doc.name, doc.pages, docxFile)
                    AndroidShareHelper.shareFile(context, docxFile, ExportFormat.DOCX)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Library.route) {
            DocumentLibraryScreen(
                documents = currentDocuments,
                onDocumentClick = { doc ->
                    activeDocument = doc
                    navController.navigate(Screen.Preview.createRoute(doc.id))
                },
                onShareDocument = { doc ->
                    val shareFile = fileManager.getShareableCacheFile("${doc.name}.${doc.defaultFormat.extension}")
                    PdfExporter.generatePdf(doc.pages, shareFile)
                    AndroidShareHelper.shareFile(context, shareFile, doc.defaultFormat)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Tools.route) {
            ToolsScreen(
                onToolSelected = { toolName ->
                    if (toolName.contains("Scan")) {
                        navController.navigate(Screen.Scanner.route)
                    } else {
                        navController.navigate(Screen.Library.route)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
