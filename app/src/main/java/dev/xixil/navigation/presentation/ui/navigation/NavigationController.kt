package dev.xixil.navigation.presentation.ui.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationController(
    router: Router? = null,
    startDestination: String,
    screens: List<Pair<String, @Composable (NavController, Router?, Bundle?) -> Unit>> = emptyList()
) {
    val navigation = rememberNavController()

    NavHost(navController = navigation, startDestination = startDestination) {
        screens.forEach { screen ->
            composable(screen.first) {
                screen.second.invoke(
                    navigation,
                    router,
                    it.arguments
                )
            }
        }
    }
}