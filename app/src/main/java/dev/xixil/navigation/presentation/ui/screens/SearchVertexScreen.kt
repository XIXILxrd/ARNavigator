package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.xixil.navigation.presentation.ui.common.Tabs
import dev.xixil.navigation.presentation.ui.common.TopSearchBar
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme

@Composable
fun SearchVertexScreen(
    onSelectedAudience: (String) -> Unit,
) {
    ARNavigationTheme {
        SearchVertexContent(onSelectedAudience = {
            onSelectedAudience(it)
        })
    }
}

@Composable
private fun SearchVertexContent(onSelectedAudience: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopSearchBar(query = "")
        Tabs(modifier = Modifier.padding(horizontal = 16.dp)) {
            onSelectedAudience(it)
        }
    }
}