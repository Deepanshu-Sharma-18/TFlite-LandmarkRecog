package com.example.landmarkrecognition.presentation

import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.landmarkrecognition.domain.Classification
import com.example.landmarkrecognition.domain.LandmarkClassifier
import java.lang.IllegalArgumentException

class LandmarkImageAnalyzer(
    private val classifier : LandmarkClassifier,
    private val onResults : (List<Classification>)->Unit
) : ImageAnalysis.Analyzer {

    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {
        if (frameSkipCounter % 60 == 0){
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image.toBitmap().centerCrop(321, 321)

            val results = classifier.classify(bitmap, rotationDegrees)
            onResults(results)
        }
        frameSkipCounter++
        image.close()
    }

    private fun Bitmap.centerCrop(desiredWidth : Int, desiredHeight : Int) : Bitmap{
        val xStart = (width - desiredWidth) / 2
        val yStart = (height - desiredHeight) / 2

        if(xStart < 0 || yStart < 0 || desiredWidth > width || desiredHeight > height){
            throw IllegalArgumentException("invalid arguments")
        }

        return Bitmap.createBitmap(this,xStart,yStart,desiredWidth,desiredHeight)
    }

}