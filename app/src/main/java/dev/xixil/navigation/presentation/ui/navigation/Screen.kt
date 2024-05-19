package dev.xixil.navigation.presentation.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data class AdminModeScreen(val source: String = "", val destination: String = "") : Screen() {
        @Serializable
        object HomeScreen: Screen()
    }

    @Serializable
    object ChooseRoute : Screen() {
        @Serializable
        data class DirectionsScreen(val source: String = "", val destination: String = ""): Screen()

        @Serializable
        object HomeScreen : Screen()

        @Serializable
        object SearchVertexScreen : Screen()

        @Serializable
        data class RouteScreen(val source: String = "", val destination: String = "") : Screen()

    }

    @Serializable
    object HomeScreen : Screen()

    @Serializable
    object UserProfileScreen : Screen()
}



