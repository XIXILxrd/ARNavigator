package dev.xixil.navigation.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.ar.core.ArCoreApk
import com.google.ar.core.exceptions.UnavailableException
import dagger.hilt.android.AndroidEntryPoint
import dev.xixil.navigation.presentation.ui.navigation.Screen
import dev.xixil.navigation.presentation.ui.navigation.createExternalRouter
import dev.xixil.navigation.presentation.ui.navigation.navigate
import dev.xixil.navigation.presentation.ui.screens.MainScreen
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ARNavigationTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    if (isARCoreSupportedAndUpToDate()) {

                        NavHost(navController = navController, startDestination = Screen.Main.route) {
                            composable(Screen.Main.route) {
                                MainScreen(router = createExternalRouter { screen, params ->
                                    navController.navigate(screen, params)
                                })
                            }
                        }

                    } else {
                        Toast.makeText(this, "AR not installed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun isARCoreSupportedAndUpToDate(): Boolean {
        return when (ArCoreApk.getInstance().checkAvailability(this)) {
            ArCoreApk.Availability.SUPPORTED_INSTALLED -> true
            ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD, ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED -> {
                try {
                    when (ArCoreApk.getInstance().requestInstall(this, true)) {
                        ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                            false
                        }

                        ArCoreApk.InstallStatus.INSTALLED -> true
                    }
                } catch (e: UnavailableException) {
                    false
                }
            }

            ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE ->
                false

            ArCoreApk.Availability.UNKNOWN_CHECKING -> {
                false
            }

            ArCoreApk.Availability.UNKNOWN_ERROR, ArCoreApk.Availability.UNKNOWN_TIMED_OUT -> {
                false
            }
        }
    }
}


