package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import dev.xixil.navigation.R
import dev.xixil.navigation.presentation.ui.annotations.DefaultPreview
import dev.xixil.navigation.presentation.ui.common.SwitcherWithText
import dev.xixil.navigation.presentation.ui.common.TopNavigationBar
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme

@Composable
fun UserProfileScreen() {
    ARNavigationTheme {
        UserProfileContent()
    }
}

@DefaultPreview
@Composable
private fun UserProfileScreenPreview() {
    UserProfileContent()
}

@Composable
private fun UserProfileContent() {
    Column {
        TopNavigationBar(onBackButtonClick = {}) {
            Text(
                text = stringResource(R.string.profile_text), modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
        SwitcherWithText(text = stringResource(id = R.string.admin_mode_text))
    }
}