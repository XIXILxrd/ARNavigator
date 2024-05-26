package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import dev.xixil.navigation.presentation.ui.navigation.NavigationController
import dev.xixil.navigation.presentation.ui.navigation.Router
import dev.xixil.navigation.presentation.ui.navigation.Screen
import dev.xixil.navigation.presentation.ui.navigation.navigate

@Composable
fun AdminModeContainer(externalRouter: Router) {
    NavigationController(
        startDestination = Screen.AdminScreen.route,
        router = externalRouter,
        screens = listOf(
            Pair(Screen.AdminScreen.route) { navController, _, params ->
                AdminModeScreen(
                    audience = params?.getBundle(ADMIN_SCREEN_PARAM_KEY)
                        ?.getString(ADMIN_SCREEN_PARAM_KEY, DEFAULT_VALUE)
                        ?: DEFAULT_VALUE,
                    onAddAudience = {
                        navController.navigate(
                            Screen.Scanner.route
                        )
                    })
            },

            Pair(Screen.Scanner.route) { nav, _, _ ->
                ScannerScreen {
                    nav.navigate(Screen.AdminScreen.route, bundleOf(ADMIN_SCREEN_PARAM_KEY to it))
                }
            }
        )
    )
}

private const val ADMIN_SCREEN_PARAM_KEY = "audience_admin"
private const val DEFAULT_VALUE = ""