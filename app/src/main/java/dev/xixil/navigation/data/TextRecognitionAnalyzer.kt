package dev.xixil.navigation.data

import android.graphics.Rect
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dev.xixil.navigation.data.utils.ImageUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class TextRecognitionAnalyzer(
    private val onDetectedTextUpdated: (String) -> Unit,
) : ImageAnalysis.Analyzer {
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val textRecognizer: TextRecognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var imageCropPercentages: Pair<Int, Int> =
        Pair(DESIRED_HEIGHT_CROP_PERCENT, DESIRED_WIDTH_CROP_PERCENT)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        coroutineScope.launch {
            val mediaImage = image.image ?: return@launch

            val rotationDegrees = image.imageInfo.rotationDegrees

            val imageHeight = mediaImage.height
            val imageWidth = mediaImage.width

            val actualAspectRatio = imageWidth / imageHeight

            val convertImageToBitmap = ImageUtils.convertYuv420888ImageToBitmap(mediaImage)
            val cropRect = Rect(0, 0, imageWidth, imageHeight)

            val currentCropPercentages = imageCropPercentages
            if (actualAspectRatio > 3) {
                val originalHeightCropPercentage = currentCropPercentages.first
                val originalWidthCropPercentage = currentCropPercentages.second
                imageCropPercentages =
                    Pair(originalHeightCropPercentage / 2, originalWidthCropPercentage)
            }

            val cropPercentages = imageCropPercentages
            val heightCropPercent = cropPercentages.first
            val widthCropPercent = cropPercentages.second
            val (widthCrop, heightCrop) = when (rotationDegrees) {
                90, 270 -> Pair(heightCropPercent / 100f, widthCropPercent / 100f)
                else -> Pair(widthCropPercent / 100f, heightCropPercent / 100f)
            }

            cropRect.inset(
                (imageWidth * widthCrop / 2).toInt(),
                (imageHeight * heightCrop / 2).toInt()
            )
            val croppedBitmap =
                ImageUtils.rotateAndCrop(convertImageToBitmap, rotationDegrees, cropRect)
            recognizeTextOnDevice(InputImage.fromBitmap(croppedBitmap, 0)).addOnCompleteListener {
                image.close()
            }
        }
    }

    private fun recognizeTextOnDevice(
        image: InputImage,
    ): Task<Text> {
        return textRecognizer.process(image)
            .addOnSuccessListener { visionText ->
                onDetectedTextUpdated(visionText.text)
            }
            .addOnFailureListener { exception ->
                error(exception)
            }
    }

    companion object {
        private const val DESIRED_WIDTH_CROP_PERCENT = 8
        private const val DESIRED_HEIGHT_CROP_PERCENT = 74
    }
}

