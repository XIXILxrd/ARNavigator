package dev.xixil.navigation.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddLocationAlt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
    object Main : Screen("main", Icons.Outlined.Home)
    object Scanner : Screen("scanner", Icons.Outlined.Home)
    object AdminScreen : Screen("admin", Icons.Outlined.AddLocationAlt)
    object Route : Screen("route", Icons.Outlined.Route)
    object Search : Screen("search", Icons.Outlined.Search)
    object Profile : Screen("profile", Icons.Outlined.Person)
    object SignIn: Screen("signIn",Icons.Outlined.Person)
}



