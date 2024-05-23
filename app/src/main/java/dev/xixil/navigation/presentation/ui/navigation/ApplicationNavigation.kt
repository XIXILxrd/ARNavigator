package dev.xixil.navigation.presentation.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import dev.xixil.navigation.presentation.ui.navigation.ApplicationArgument.AUDIENCE
import dev.xixil.navigation.presentation.ui.navigation.ApplicationArgument.DESTINATION
import dev.xixil.navigation.presentation.ui.navigation.ApplicationArgument.EMPTY_ARGUMENT
import dev.xixil.navigation.presentation.ui.navigation.ApplicationArgument.SOURCE
import dev.xixil.navigation.presentation.ui.screens.AdminModeScreen
import dev.xixil.navigation.presentation.ui.screens.DirectionsScreen
import dev.xixil.navigation.presentation.ui.screens.HomeScreen
import dev.xixil.navigation.presentation.ui.screens.RouteScreen
import dev.xixil.navigation.presentation.ui.screens.SearchVertexScreen
import dev.xixil.navigation.presentation.ui.screens.UserProfileScreen

@SuppressLint("RestrictedApi")
@Composable
fun ApplicationNavigation(
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        exitTransition = { ExitTransition.None },
        enterTransition = { EnterTransition.None }
    ) {
        navigation(
            startDestination = Screen.AdminMode.AdminScreen.route,
            route = Screen.AdminMode.route
        ) {
            composable(Screen.AdminMode.Home.route) {
                HomeScreen(detectedText = {
                    navController.navigate("${Screen.AdminMode.AdminScreen.route}?$AUDIENCE=$it")
                })
            }
            composable(
                "${Screen.AdminMode.AdminScreen.route}?$AUDIENCE={$AUDIENCE}",
                arguments = listOf(
                    navArgument(AUDIENCE) {
                        defaultValue = EMPTY_ARGUMENT
                        type = NavType.StringType
                    })
            ) { navBackStackEntry ->
                val audience = navBackStackEntry.arguments?.getString(AUDIENCE) ?: EMPTY_ARGUMENT

                AdminModeScreen(
                    audience = audience,
                    onAddAudience = { navController.navigate(Screen.AdminMode.Home.route) })
            }
        }

        navigation(
            startDestination = Screen.Directions.Direction.route,
            route = Screen.Directions.route
        ) {
            composable(
                route = "${Screen.Directions.Direction.route}?$SOURCE={$SOURCE}&$DESTINATION={$DESTINATION}",
                arguments = listOf(
                    navArgument(SOURCE) {
                        defaultValue = EMPTY_ARGUMENT
                        type = NavType.StringType
                    },
                    navArgument(DESTINATION) {
                        defaultValue = EMPTY_ARGUMENT
                        type = NavType.StringType
                    })
            ) { navBackStackEntry ->
                val source =
                    navBackStackEntry.arguments?.getString(SOURCE) ?: EMPTY_ARGUMENT
                val destination =
                    navBackStackEntry.arguments?.getString(DESTINATION) ?: EMPTY_ARGUMENT

                DirectionsScreen(
                    source = source,
                    destination = destination,
                    onBack = { navController.popBackStack() },
                    onChooseSource = { navController.navigate(Screen.Home.route) },
                    onChooseDestination = { navController.navigate(Screen.Directions.Search.route) },
                    onStartRoute = { navController.navigate("${Screen.Directions.Route.route}?$SOURCE=$source&$DESTINATION=$destination") })
            }
            composable(
                route = "${Screen.Directions.Direction.route}?$SOURCE={$SOURCE}&$DESTINATION={$DESTINATION}",
                arguments = listOf(
                    navArgument(SOURCE) {
                        defaultValue = EMPTY_ARGUMENT
                        type = NavType.StringType
                    },
                    navArgument(DESTINATION) {
                        defaultValue = EMPTY_ARGUMENT
                        type = NavType.StringType
                    })
            ) { navBackStackEntry ->
                val source = navBackStackEntry.arguments?.getString(SOURCE) ?: EMPTY_ARGUMENT
                val destination = navBackStackEntry.arguments?.getString(DESTINATION) ?: EMPTY_ARGUMENT

                RouteScreen(
                    source = source,
                    destination = destination,
                    onBack = { navController.popBackStack() })
            }
            composable(Screen.Directions.Search.route) {
                SearchVertexScreen(onSelectedAudience = {
                    navController.navigate("${Screen.Directions.Direction.route}?$SOURCE=$it&$DESTINATION=$EMPTY_ARGUMENT")
                })
            }
        }

        composable(Screen.Home.route) {
            HomeScreen(detectedText = {
                navController.navigate(  "${Screen.Directions.Direction.route}?$SOURCE=$it&$DESTINATION=$EMPTY_ARGUMENT")
            })
        }

        composable(Screen.Profile.route) {
            UserProfileScreen()
        }
    }
}

object ApplicationArgument {
    const val SOURCE = "source"
    const val DESTINATION = "destination"
    const val AUDIENCE = "audience"
    const val EMPTY_ARGUMENT = "-1"
}

