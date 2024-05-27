package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.runtime.Composable
import dev.xixil.navigation.presentation.ui.navigation.NavigationController
import dev.xixil.navigation.presentation.ui.navigation.Router
import dev.xixil.navigation.presentation.ui.navigation.Screen

@Composable
fun AdminModeContainer(externalRouter: Router) {
    NavigationController(
        startDestination = Screen.AdminScreen.route,
        router = externalRouter,
        screens = listOf(
            Pair("${Screen.AdminScreen.route}?$ADMIN_SCREEN_PARAM_KEY={$ADMIN_SCREEN_PARAM_KEY}") { navController, _, params ->
                val audience  = params?.getString(ADMIN_SCREEN_PARAM_KEY) ?: DEFAULT_VALUE

                AdminModeScreen(
                    audience = audience,
                    onAddAudience = {
                        navController.navigate(
                            Screen.Scanner.route
                        )
                    })
            },

            Pair(Screen.Scanner.route) { nav, _, _ ->
                ScannerScreen {
                    nav.navigate("${Screen.AdminScreen.route}?$ADMIN_SCREEN_PARAM_KEY=$it")
                }
            }
        )
    )
}

const val ADMIN_SCREEN_PARAM_KEY = "audience_admin"
private const val DEFAULT_VALUE = ""