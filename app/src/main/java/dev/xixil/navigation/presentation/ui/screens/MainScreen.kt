package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.xixil.navigation.presentation.ui.navigation.Router
import dev.xixil.navigation.presentation.ui.navigation.Screen
import dev.xixil.navigation.presentation.ui.navigation.navigate

@Composable
fun MainScreen(
    router: Router,
) {
    val navController = rememberNavController()
    val screens = listOf(
        Screen.Scanner,
        Screen.Route,
        Screen.AdminScreen,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(imageVector = screen.icon, contentDescription = null) }
                    )
                }
            }
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = Screen.Scanner.route,
        ) {
            composable(Screen.Scanner.route) {
                ScannerScreen(detectedText = { text ->
                    navController.navigate(Screen.Route.route, bundleOf("source" to text))
                })
            }
            composable(Screen.Route.route) { RouteContainer(externalRouter = router) }
            composable(Screen.AdminScreen.route) { AdminModeContainer(externalRouter = router) }
            composable(Screen.Profile.route) { UserProfileScreen() }
        }
    }
}