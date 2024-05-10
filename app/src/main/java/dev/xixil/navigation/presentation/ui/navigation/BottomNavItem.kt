package dev.xixil.navigation.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.ui.graphics.vector.ImageVector
import dev.xixil.navigation.R

sealed class BottomNavItem(val route: String, val titleResourceId: Int, val icon: ImageVector) {
    object AdminModeScreen: BottomNavItem("adminModeScreen", R.string.admin_mode_screen_title, Icons.Outlined.AccountTree)
    object DirectionsScreen: BottomNavItem("directionsScreen", R.string.directions_screen_title, Icons.Outlined.Route)
    object HomeScreen: BottomNavItem("homeScreen", R.string.home_screen_title, Icons.Filled.Home)
    object SearchVertexScreen: BottomNavItem("searchVertexScreen", R.string.search_screen_title, Icons.Filled.Search)
    object UserProfileScreen: BottomNavItem("userProfileScreen", R.string.user_profile_screen_title, Icons.Outlined.Person)
}