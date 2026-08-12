package com.smartscan.app.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.smartscan.app.domain.model.OCRResult
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume

class OcrEngine(private val context: Context) {

    private val latinRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val devanagariRecognizer = TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build())

    suspend fun recognizeText(imageFile: File, languageCode: String = "en"): OCRResult {
        if (!imageFile.exists()) return OCRResult("File not found")
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath) ?: return OCRResult("Failed to decode image")
        return recognizeText(bitmap, languageCode)
    }

    suspend fun recognizeText(bitmap: Bitmap, languageCode: String = "en"): OCRResult =
        suspendCancellableCoroutine { continuation ->
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            val recognizer = if (languageCode == "hi" || languageCode == "gu") {
                devanagariRecognizer
            } else {
                latinRecognizer
            }

            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    val fullText = visionText.text
                    val paragraphs = visionText.textBlocks.map { it.text }
                    continuation.resume(
                        OCRResult(
                            fullText = fullText.ifEmpty { "No text recognized in document." },
                            paragraphs = paragraphs,
                            languageCode = languageCode,
                            confidence = 0.95f
                        )
                    )
                }
                .addOnFailureListener { exception ->
                    continuation.resume(
                        OCRResult(
                            fullText = "OCR Recognition Error: ${exception.localizedMessage ?: "Unknown error"}",
                            paragraphs = emptyList(),
                            languageCode = languageCode,
                            confidence = 0.0f
                        )
                    )
                }
        }
}
