package dev.xixil.navigation.ui.screens

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.xixil.navigation.ui.common.CameraContent
import dev.xixil.navigation.ui.common.NoPermissionContent


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {
    val cameraPermission: PermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    MainScreenContent(
        hasPermission = cameraPermission.status.isGranted,
        onRequestPermission = { cameraPermission.launchPermissionRequest() }
    )
}

@Composable
private fun MainScreenContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
) {
    if (hasPermission) {
        CameraContent()
    } else {
        NoPermissionContent {
            onRequestPermission()
        }
    }
}