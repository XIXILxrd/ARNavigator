package dev.xixil.navigation.presentation.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import dev.xixil.navigation.presentation.ui.navigation.NavigationController
import dev.xixil.navigation.presentation.ui.navigation.Router
import dev.xixil.navigation.presentation.ui.navigation.Screen

@Composable
fun RouteContainer(
    externalRouter: Router,
) {
    NavigationController(
        startDestination = Screen.Route.route,
        router = externalRouter,
        screens = listOf(
            Pair("${Screen.Route.route}?$SOURCE_PARAM_KEY={$SOURCE_PARAM_KEY}&$DESTINATION_PARAM_KEY={$DESTINATION_PARAM_KEY}") { nav, _, params ->
                val source = params?.getString(SOURCE_PARAM_KEY) ?: DEFAULT_VALUE
                val destination = params?.getString(DESTINATION_PARAM_KEY)  ?: DEFAULT_VALUE
                Log.d("RouteScreenParams", "2 param: source $source, destination $destination")

                RouteScreen(
                    source = source,
                    destination = destination,
                    navController = nav
                )
            },

            Pair(Screen.Scanner.route) { nav, _, _ ->
                ScannerScreen {
                    nav.navigate("${Screen.Route.route}?$SOURCE_PARAM_KEY=$it")
                }
            },

            Pair("${Screen.Search.route}?$SOURCE_PARAM_KEY={$SOURCE_PARAM_KEY}") { nav, _, params ->
                val source = params?.getString(SOURCE_PARAM_KEY) ?: DEFAULT_VALUE
                SearchVertexScreen { destination ->
                    nav.navigate("${Screen.Route.route}?$SOURCE_PARAM_KEY=$source&$DESTINATION_PARAM_KEY=$destination")
                }
            }
        )
    )
}

const val SOURCE_PARAM_KEY = "source_route"
const val DESTINATION_PARAM_KEY = "destination_route"
private const val DEFAULT_VALUE = ""