package dev.xixil.navigation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.xixil.navigation.R
import dev.xixil.navigation.ui.annotations.DefaultPreview
import dev.xixil.navigation.ui.common.LargeTextField
import dev.xixil.navigation.ui.common.MediumTitleBar
import dev.xixil.navigation.ui.common.TopNavigationBar
import dev.xixil.navigation.ui.theme.ARNavigationTheme
import dev.xixil.navigation.ui.theme.Gray

@Composable
fun DirectionsScreen() {
    ARNavigationTheme {
        DirectionsContent()
    }
}

@Composable
private fun DirectionsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopNavigationBar(onBackButtonClick = {}) {
            Text(
                text = stringResource(R.string.directions_text), modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
        LargeTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 3.dp),
            placeholder = stringResource(R.string.from_here_placeholder_text)
        ) {
            ""
        }
        LargeTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            placeholder = stringResource(R.string.to_placeholder_text)
        ) {
            ""
        }
        MediumTitleBar(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            text = stringResource(R.string.history_title_text),
            textColor = MaterialTheme.colorScheme.tertiary
        )
    }
}

@DefaultPreview
@Composable
private fun DirectionsContentPreview() {
    ARNavigationTheme {
        DirectionsContent()
    }
}