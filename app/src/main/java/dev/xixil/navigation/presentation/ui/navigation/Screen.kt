package dev.xixil.navigation.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
    object Home : Screen("home", Icons.Outlined.Home)
    object AdminMode : Screen("adminMode", Icons.Default.Route) {
        object Home : Screen("home", Icons.Outlined.Home)
        object AdminScreen : Screen("adminScreen", Icons.Default.Route)
    }

    object Directions : Screen("directions", Icons.Outlined.Route) {
        object Direction : Screen("directionScreen", Icons.Outlined.Route)
        object Route : Screen("routeScreen", Icons.Outlined.Person)
        object Search : Screen("search", Icons.Outlined.Search)
    }

    object Profile: Screen("profile", Icons.Outlined.Person)
}



