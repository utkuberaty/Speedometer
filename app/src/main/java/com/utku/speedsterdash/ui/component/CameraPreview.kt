package com.utku.speedsterdash.ui.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Typeface
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.utku.speedsterdash.ui.spToPx
import com.utku.speedsterdash.ui.theme.asset
import kotlinx.coroutines.launch

@Composable
fun CameraMaskedWithText(
    maskedText: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Show the Camera Preview
        CameraPreviewView(modifier = Modifier.fillMaxSize())
        DrawingCanvas(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            text = maskedText,
            backgroundColor = backgroundColor
        )
    }
}

@Composable
fun CameraPreviewView(
    modifier: Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    context: Context = LocalContext.current
) {
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Camera preview composable
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        ) { view ->
            coroutineScope.launch {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()

                preview.setSurfaceProvider(view.surfaceProvider)

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                    )
                } catch (exc: Exception) {
                    // Handle exceptions appropriately
                }
            }
        }
    }
}

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    text: String = "5",
    backgroundColor: Color = Color.White,
    resolver: FontFamily.Resolver = LocalFontFamilyResolver.current
) {
    val textSize = 300f.sp.spToPx()
    Canvas(modifier = modifier) {
        val size = RectF(0f, 0f, size.width, size.height)
        drawIntoCanvas {
            drawShapes(
                text = text,
                canvas = it.nativeCanvas,
                size = size,
                selectedMode = PorterDuff.Mode.SRC_OUT,
                textSize = textSize,
                resolver = resolver,
                backgroundColor = backgroundColor
            )
        }
    }
}

fun drawShapes(
    text: String,
    canvas: Canvas,
    size: RectF,
    selectedMode: PorterDuff.Mode,
    textSize: Float,
    resolver: FontFamily.Resolver,
    backgroundColor: Color = Color.White,
) {
    val typeface: Typeface = resolver.resolve(fontFamily = asset).value as Typeface

    val paintRect = Paint().apply {
        color = backgroundColor.toArgb()
        style = android.graphics.Paint.Style.FILL

    }

    val paintText = Paint().apply {
        this.textSize = textSize
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(selectedMode)
        textAlign = Paint.Align.CENTER
        this.typeface = typeface
    }

    val textBounds = android.graphics.Rect()
    paintText.getTextBounds(text, 0, text.length, textBounds)

    val textHeight = textBounds.height()
    val x = size.width() / 2
    val y = size.height() / 2 + textHeight / 2

    with(size) {
        canvas.saveLayer(this, null)
        canvas.drawRect(0f, 0f, width(), height(), paintRect)
        canvas.drawText(
            text,
            x,
            y,
            paintText
        )
        canvas.restore()
    }
}