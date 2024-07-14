package dev.xixil.navigation.presentation.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme

@Composable
fun MediumTitleBar(
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    text: String,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            color = textColor
        )
    }

}

@dev.xixil.navigation.presentation.ui.annotations.DefaultPreview
@Composable
private fun TitleBar() {
    ARNavigationTheme {
        MediumTitleBar(
            text = "Hello world"
        )
    }
}
