package com.dev.james.booktracker.compose_ui.ui.components

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.dev.james.booktracker.compose_ui.R
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun CameraView(
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onCloseAction : () -> Unit ,
    onError: (ImageCaptureException) -> Unit
) {
    // setup
    val coroutineScope = rememberCoroutineScope()

    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isFlashTurnedOn by remember { mutableStateOf(false) }

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context).apply { PreviewView.ImplementationMode.PERFORMANCE } }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()


    // using the camera provider method
    // used within disposable effect for handling the camera flash
    // once the CameraComponent leaves composition.
    DisposableEffect(key1 = lensFacing , key2 = isFlashTurnedOn) {

        lateinit var camera: ProcessCameraProvider

        coroutineScope.launch {
            camera = context.getCameraProvider()
            camera.unbindAll()
            camera.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            ).apply {

                val hasFlashLight = this.cameraInfo.hasFlashUnit()

                if (isFlashTurnedOn){
                    if(hasFlashLight){
                        this.cameraControl.enableTorch(false)
                    }
                } else{
                    if(hasFlashLight){
                        this.cameraControl.enableTorch(true)
                    }
                }

            }

        }

        preview.setSurfaceProvider(previewView.surfaceProvider)

        onDispose {
            camera.unbindAll()
            camera.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            ).apply {
                this.cameraControl.enableTorch(false)
            }
        }
    }

    Box(
        modifier = Modifier.background(Color.Transparent).fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {



        //the camera screen itself
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()
        ) {

            AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

            Row(
                modifier = Modifier.background(color = Color.Transparent) ,
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                val flashIcon = if (isFlashTurnedOn) R.drawable.baseline_flash_on_24 else R.drawable.baseline_flash_off_24

                IconButton(
                    modifier =
                    Modifier.padding(bottom = 20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                    ,
                    onClick = {
                        isFlashTurnedOn = !isFlashTurnedOn
                    },
                    content = {
                        Icon(
                            painter = painterResource(id = flashIcon),
                            contentDescription = "Take picture",
                            tint = Color.White,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(1.dp)
                        )
                    }
                )

                IconButton(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    onClick = {
                        Timber.i("camera view", "ON CLICK")
                        takePhoto(
                            filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                            imageCapture = imageCapture,
                            outputDirectory = outputDirectory,
                            executor = executor,
                            onImageCaptured = onImageCaptured,
                            onError = onError
                        )
                    },
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_camera_24),
                            contentDescription = "Take picture",
                            tint = Color.White,
                            modifier = Modifier
                                .size(120.dp)
                                .padding(1.dp)
                        )
                    }
                )

            }

            //Box(modifier = Modifier.fillMaxSize().padding(50.dp).border(width = 5.dp , color = Color.Green).background(color = Color.Transparent)
        }

        Row(
            horizontalArrangement = Arrangement.End ,
            verticalAlignment = Alignment.CenterVertically ,
            modifier = Modifier.background(Color.Transparent)
                .padding(8.dp).fillMaxWidth()

        ) {
            IconButton(
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                onClick = {
                    onCloseAction()
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close photo view",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(1.dp)
                    )
                }
            )
        }

    }


}

private fun takePhoto(
    filenameFormat: String,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {

    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions, executor, object: ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            Timber.e("camera view", "Take photo error:", exception)
            onError(exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(photoFile)
            onImageCaptured(savedUri)
        }
    })
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}
