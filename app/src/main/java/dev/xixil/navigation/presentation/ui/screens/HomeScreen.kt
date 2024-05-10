package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.xixil.navigation.presentation.ui.common.BottomNavigationBar
import dev.xixil.navigation.presentation.ui.common.CameraContent
import dev.xixil.navigation.presentation.ui.common.NavigateFloatingActionButton
import dev.xixil.navigation.presentation.ui.common.NoPermissionContent
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
) {
    val cameraPermission: PermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)

   ARNavigationTheme {
       HomeScreenContent(
           hasPermission = cameraPermission.status.isGranted,
           onRequestPermission = { cameraPermission.launchPermissionRequest() },
           onTextDetected = {

           }
       )
   }
}

@Composable
private fun HomeScreenContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    onTextDetected: (String) -> Unit
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.weight(1f)) {
            if (hasPermission) {
                CameraContent(onTextDetected)
            } else {
                NoPermissionContent {
                    onRequestPermission()
                }
            }
            NavigateFloatingActionButton(
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 12.dp)
                    .align(Alignment.BottomEnd)
            ) {}
        }
    }
}