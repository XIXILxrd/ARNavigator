package dev.xixil.navigation.presentation.ui.navigation

import dev.xixil.navigation.R

sealed class Screen(val route: String, val titleResourceId: Int) {
    object AdminModeScreen: Screen("adminModeScreen", R.string.admin_mode_screen_title)
    object DirectionsScreen: Screen("directionsScreen", R.string.directions_screen_title)
    object HomeScreen: Screen("homeScreen", R.string.home_screen_title)
    object RouteScreen: Screen("routeScreen", R.string.route_screen_title)
    object SearchVertexScreen: Screen("searchVertexScreen", R.string.search_screen_title)
    object UserProfileScreen: Screen("userProfileScreen", R.string.user_profile_screen_title)
}