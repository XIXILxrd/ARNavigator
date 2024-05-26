package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.xixil.navigation.presentation.ui.common.CameraContent
import dev.xixil.navigation.presentation.ui.common.NoPermissionContent
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    modifier: Modifier = Modifier,
    detectedText: (String) -> Unit,
) {
    val cameraPermission: PermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    ARNavigationTheme {
        ScannerScreenContent(
            modifier = modifier,
            hasPermission = cameraPermission.status.isGranted,
            onRequestPermission = { cameraPermission.launchPermissionRequest() },
            onTextDetected = detectedText
        )
    }
}

@Composable
private fun ScannerScreenContent(
    modifier: Modifier = Modifier,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    onTextDetected: (String) -> Unit,
) {
    if (hasPermission) {
        CameraContent(
            modifier = modifier,
            onTextDetected = onTextDetected
        )
    } else {
        NoPermissionContent {
            onRequestPermission()
        }
    }
}