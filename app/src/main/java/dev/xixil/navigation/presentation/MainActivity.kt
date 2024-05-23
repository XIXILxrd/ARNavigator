package dev.xixil.navigation.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.ar.core.ArCoreApk
import com.google.ar.core.exceptions.UnavailableException
import dagger.hilt.android.AndroidEntryPoint
import dev.xixil.navigation.presentation.ui.common.BottomNavigationBar
import dev.xixil.navigation.presentation.ui.navigation.ApplicationNavigation
import dev.xixil.navigation.presentation.ui.navigation.Screen
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ARNavigationTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomNavigationBar(navController = navController) }
                ) {
                    if (isARCoreSupportedAndUpToDate()) {
                        ApplicationNavigation(
                            modifier = Modifier.padding(it),
                            navController = navController,
                            startDestination = Screen.Home.route
                        )
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


