package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.xixil.navigation.R
import dev.xixil.navigation.presentation.ui.common.LargeTextField
import dev.xixil.navigation.presentation.ui.common.MediumTitleBar
import dev.xixil.navigation.presentation.ui.common.PrimaryButton
import dev.xixil.navigation.presentation.ui.common.TopNavigationBar
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme

@Composable
fun DirectionsScreen(
    source: String,
    destination: String,
    onBack: () -> Unit,
    onChooseSource: () -> Unit,
    onChooseDestination: () -> Unit,
    onStartRoute: () -> Unit,
) {
    ARNavigationTheme {
        DirectionsContent(
            onBack = onBack,
            onChooseSource = onChooseSource,
            onChooseDestination = onChooseDestination,
            onStartRoute = onStartRoute,
            source = source,
            destination = destination
        )
    }
}

@Composable
private fun DirectionsContent(
    onBack: () -> Unit,
    onChooseSource: () -> Unit,
    onChooseDestination: () -> Unit,
    onStartRoute: () -> Unit,
    source: String,
    destination: String,
) {
    val sourceFieldText by remember {
        mutableStateOf(source)
    }

    val destinationFieldText by remember {
        mutableStateOf(destination)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopNavigationBar(onBackButtonClick = onBack) {
            Text(
                text = stringResource(R.string.directions_text), modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
        LargeTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 3.dp)
                .clickable { onChooseSource() },
            placeholder = stringResource(R.string.from_here_placeholder_text)
        ) {
            sourceFieldText
        }
        LargeTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clickable { onChooseDestination() },
            placeholder = stringResource(R.string.to_placeholder_text)
        ) {
            destinationFieldText
        }
        MediumTitleBar(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            text = stringResource(R.string.history_title_text),
            textColor = MaterialTheme.colorScheme.tertiary
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(text = R.string.next_text_button) {
            onStartRoute()
        }
    }
}