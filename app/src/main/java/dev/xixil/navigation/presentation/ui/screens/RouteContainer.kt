package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import dev.xixil.navigation.presentation.ui.navigation.NavigationController
import dev.xixil.navigation.presentation.ui.navigation.Router
import dev.xixil.navigation.presentation.ui.navigation.Screen
import dev.xixil.navigation.presentation.ui.navigation.navigate

@Composable
fun RouteContainer(
    externalRouter: Router,
) {
    NavigationController(
        startDestination = Screen.Route.route,
        router = externalRouter,
        screens = listOf(
            Pair(Screen.Route.route) { nav, _, params ->
                RouteScreen(
                    source = params?.getBundle(SOURCE_PARAM_KEY)?.getString(SOURCE_PARAM_KEY)
                        ?: DEFAULT_VALUE,
                    destination = params?.getBundle(DESTINATION_PARAM_KEY)?.getString(
                        DESTINATION_PARAM_KEY
                    ) ?: DEFAULT_VALUE,
                    navController = nav
                )
            },

            Pair(Screen.Scanner.route) { nav, _, _ ->
                ScannerScreen {
                    nav.navigate(Screen.Route.route, bundleOf(SOURCE_PARAM_KEY to it))
                }
            },

            Pair(Screen.Search.route) { nav, _, _ ->
                SearchVertexScreen {
                    nav.navigate(Screen.Route.route, bundleOf(DESTINATION_PARAM_KEY to it))
                }
            }
        )
    )
}

private const val SOURCE_PARAM_KEY = "source_route"
private const val DESTINATION_PARAM_KEY = "destination_route"
private const val DEFAULT_VALUE = ""