package dev.xixil.navigation.presentation.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable


@ExperimentalAnimationApi
@Composable
fun PresentModal(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + fadeOut(),
        content = { content() }
    )
}

@ExperimentalAnimationApi
@Composable
fun PresentNested(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutHorizontally() + fadeOut(),
        content = { content() }
    )

}