package dev.xixil.navigation.ui.screens

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import dev.xixil.navigation.R
import dev.xixil.navigation.ui.annotations.DefaultPreview
import dev.xixil.navigation.ui.common.SmallPrimitiveButton
import dev.xixil.navigation.ui.common.SmallTextField
import dev.xixil.navigation.ui.common.TravelTimeBar
import dev.xixil.navigation.ui.theme.ARNavigationTheme

@Composable
fun RouteScreen() {
    ARNavigationTheme {
        RouteScreenContent()
    }
}

@DefaultPreview
@Composable
private fun RouteScreenPreview() {
    ARNavigationTheme {
        RouteScreenContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RouteScreenContent() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }
    var hours by remember { mutableIntStateOf(0)}
    var minutes by remember { mutableIntStateOf(0)}


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SmallTextField(
                    modifier = Modifier.weight(0.5f),
                    placeholder = stringResource(id = R.string.to_text)
                ) {
                    ""
                }
                SmallTextField(
                    modifier = Modifier.weight(0.5f),
                    placeholder = stringResource(id = R.string.from_text)
                ) {
                    ""
                }
            }

            TravelTimeBar(hours = hours, minutes = minutes)
        }) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Cyan)
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
                    }.also { previewView ->
                        cameraController.imageAnalysisTargetSize = CameraController.OutputSize(
                            AspectRatio.RATIO_16_9
                        )
                        cameraController.bindToLifecycle(lifecycleOwner)
                        previewView.controller = cameraController
                    }
                }
            )
        }
    }
}