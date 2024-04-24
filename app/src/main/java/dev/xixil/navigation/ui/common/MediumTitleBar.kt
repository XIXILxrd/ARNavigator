package dev.xixil.navigation.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.xixil.navigation.ui.annotations.DefaultPreview
import dev.xixil.navigation.ui.theme.ARNavigationTheme

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

@DefaultPreview
@Composable
private fun TitleBar() {
    ARNavigationTheme {
        MediumTitleBar(
            text = "Hello world"
        )
    }
}
