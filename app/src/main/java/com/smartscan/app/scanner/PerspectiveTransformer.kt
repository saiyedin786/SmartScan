package com.smartscan.app.scanner

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import com.smartscan.app.domain.model.CropBounds

object PerspectiveTransformer {

    fun cropAndTransform(src: Bitmap, bounds: CropBounds, rotationDegrees: Int): Bitmap {
        var rotated = src
        if (rotationDegrees != 0) {
            val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
            rotated = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
        }

        val w = rotated.width.toFloat()
        val h = rotated.height.toFloat()

        val left = (bounds.topLeft.x * w).coerceAtLeast(0f)
        val top = (bounds.topLeft.y * h).coerceAtLeast(0f)
        val right = (bounds.bottomRight.x * w).coerceAtMost(w)
        val bottom = (bounds.bottomRight.y * h).coerceAtMost(h)

        val cropW = (right - left).toInt().coerceAtLeast(10)
        val cropH = (bottom - top).toInt().coerceAtLeast(10)

        val cropped = Bitmap.createBitmap(
            rotated,
            left.toInt().coerceIn(0, (w - cropW).toInt().coerceAtLeast(0)),
            top.toInt().coerceIn(0, (h - cropH).toInt().coerceAtLeast(0)),
            cropW.coerceAtMost(rotated.width),
            cropH.coerceAtMost(rotated.height)
        )

        return cropped
    }

    fun rotateBitmap(src: Bitmap, degrees: Float): Bitmap {
        if (degrees == 0f) return src
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }
}
