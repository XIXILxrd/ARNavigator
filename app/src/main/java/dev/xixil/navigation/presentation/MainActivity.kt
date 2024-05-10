package dev.xixil.navigation.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.ar.core.ArCoreApk
import com.google.ar.core.exceptions.UnavailableException
import dagger.hilt.android.AndroidEntryPoint
import dev.xixil.navigation.presentation.ui.common.BottomNavigationBar
import dev.xixil.navigation.presentation.ui.navigation.PresentModal
import dev.xixil.navigation.presentation.ui.navigation.Screen
import dev.xixil.navigation.presentation.ui.screens.AdminModeScreen
import dev.xixil.navigation.presentation.ui.screens.DirectionsScreen
import dev.xixil.navigation.presentation.ui.screens.HomeScreen
import dev.xixil.navigation.presentation.ui.screens.RouteScreen
import dev.xixil.navigation.presentation.ui.screens.SearchVertexScreen
import dev.xixil.navigation.presentation.ui.screens.UserProfileScreen
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ARNavigationTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surface),
                    bottomBar = { BottomNavigationBar(navController = navController) }

                ) {
                    NavigationHost(navController = navController, modifier = Modifier.padding(it))

                    if (isARCoreSupportedAndUpToDate()) {
                        navController.navigate(Screen.HomeScreen.route)
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationHost(navController: NavController, modifier: Modifier) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.HomeScreen.route,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        navigation(
            startDestination = Screen.DirectionsScreen.route,
            route = "BuildPath"
        ) {
            composable(Screen.DirectionsScreen.route) {
                DirectionsScreen()
            }
            composable(Screen.SearchVertexScreen.route) {
                SearchVertexScreen()
            }
            composable(Screen.RouteScreen.route) {
                RouteScreen()
            }
        }
        composable(Screen.HomeScreen.route) {
            PresentModal { HomeScreen() }
//            HomeScreen()
        }
        composable(Screen.SearchVertexScreen.route) {
            PresentModal { SearchVertexScreen() }
//            SearchVertexScreen()
        }
        composable(Screen.UserProfileScreen.route) {
            PresentModal { UserProfileScreen() }
//            UserProfileScreen()
        }
        composable(Screen.AdminModeScreen.route) {
            PresentModal { AdminModeScreen() }
//            AdminModeScreen()
        }
    }
}

