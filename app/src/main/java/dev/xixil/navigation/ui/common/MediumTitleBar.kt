package dev.xixil.navigation.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.xixil.navigation.R
import dev.xixil.navigation.ui.annotations.DefaultPreview

@Composable
fun MediumTitleBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    @StringRes text: Int,
) {
    Surface(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
    ) {
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@DefaultPreview
@Composable
private fun TitleBar() {
    MediumTitleBar(
        text = R.string.app_name
    )
}
