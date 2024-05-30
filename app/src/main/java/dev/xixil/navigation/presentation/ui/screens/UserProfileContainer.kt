package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.runtime.Composable
import dev.xixil.navigation.presentation.ui.navigation.NavigationController
import dev.xixil.navigation.presentation.ui.navigation.Router
import dev.xixil.navigation.presentation.ui.navigation.Screen

@Composable
fun UserProfileContainer(
    externalRouter: Router,
) {
    NavigationController(
        startDestination = Screen.Profile.route,
        router = externalRouter,
        screens = listOf(
            Pair(Screen.SignIn.route) { nav, _, _ ->
                SignInScreen(onSignInSuccess = { nav.navigate(Screen.Profile.route) })
            },
            Pair(Screen.Profile.route) { nav, _, _ ->
                UserProfileScreen(onSingOutPressed = {nav.navigate(Screen.SignIn.route)})
            }
        ))
}