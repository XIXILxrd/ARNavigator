package dev.xixil.navigation.presentation.ui.common

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.AspectRatio
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import dev.xixil.navigation.R
import dev.xixil.navigation.data.TextRecognitionAnalyzer


@Composable
fun CameraContent(
    onTextDetected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }
    var detectedText by remember { mutableStateOf("") }

    var dialogState by remember {
        mutableStateOf(false)
    }

    val onTextUpdated: (String) -> Unit = {
        detectedText = it
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AndroidView(
            modifier = Modifier
                .wrapContentSize()
                .padding(),
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(Color.Black.toArgb())
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }.also { preview ->
                    startTextRecognition(
                        context = context,
                        cameraController = cameraController,
                        lifecycleOwner = lifecycleOwner,
                        previewView = preview,
                        onDetectedTextUpdated = onTextUpdated
                    )
                }
            }
        )

        AnalysisArea()

        Button(onClick = {
            dialogState = true
        }) {
            Text(text = "Scan")
        }

        if (dialogState) {
            val text by remember {
                mutableStateOf(detectedText)
            }
            ConfirmDetectedText(
                detectedText = text,
                onTextDetected = {
                    onTextDetected(it)
                    dialogState = false
                },
                onDismissRequest = {
                    dialogState = false
                }
            )
        }
    }
}

@Composable
private fun ConfirmDetectedText(
    modifier: Modifier = Modifier,
    detectedText: String,
    onTextDetected: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var selectedItem by remember {
        mutableStateOf("")
    }

    AlertDialog(
        modifier = modifier,
        title = {
            Text(
                "Text is recognized correctly?",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.9f),
                    text = "$detectedText/$selectedItem",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
                Menu(modifier = Modifier.weight(0.1f)) {
                    selectedItem = it
                }
            }
        },
        onDismissRequest = { onDismissRequest() },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(id = R.string.dismiss_text_button))
            }
        },
        confirmButton = {
            TextButton(enabled = selectedItem.isNotBlank(),
                onClick = {
                    onTextDetected("$detectedText/$selectedItem")
                }) {
                Text(text = stringResource(id = R.string.correct_text_button))
            }
        })
}

private fun startTextRecognition(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onDetectedTextUpdated: (String) -> Unit,
) {
    cameraController.imageAnalysisTargetSize = CameraController.OutputSize(AspectRatio.RATIO_16_9)
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        TextRecognitionAnalyzer(onDetectedTextUpdated)
    )

    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}

@Composable
private fun Menu(modifier: Modifier = Modifier, selectedItem: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val buildings = listOf(
        "1", "1а", "1б", "2", "2б", "2в", "3а", "3б", "3бв", "3г", "3д", "4", "5", "6", "6а"
    )

    Box(
        modifier = modifier
            .wrapContentSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
        }
        DropdownMenu(
            scrollState = rememberScrollState(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            buildings.forEach {
                DropdownMenuItem(text = { Text(it) }, onClick = {
                    selectedItem(it)
                })
            }
        }
    }
}

@Composable
fun NoPermissionContent(
    onRequestPermission: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Please grant the permission to use the camera to use the core functionality of this app."
            )
            Button(onClick = onRequestPermission) {
                Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera")
                Text(text = "Grant permission")
            }
        }
    }
}

@Composable
fun AnalysisArea(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .size(
                    height = 190.dp,
                    width = 400.dp
                )
                .background(color = Color.White.copy(alpha = 0.2f), RoundedCornerShape(25.dp))
                .zIndex(1f)
        )
    }
}

