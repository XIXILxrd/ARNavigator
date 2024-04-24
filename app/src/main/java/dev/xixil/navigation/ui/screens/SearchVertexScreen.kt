package dev.xixil.navigation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.xixil.navigation.ui.annotations.DefaultPreview
import dev.xixil.navigation.ui.common.Tabs
import dev.xixil.navigation.ui.common.TopSearchBar
import dev.xixil.navigation.ui.theme.ARNavigationTheme

@Composable
fun SearchVertexScreen() {
    ARNavigationTheme {
        SearchVertexContent()
    }
}

@DefaultPreview
@Composable
private fun SearchVertexContentPreview() {
    SearchVertexContent()
}

@Composable
private fun SearchVertexContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        TopSearchBar(query = "")
        Tabs(modifier = Modifier.padding(horizontal = 16.dp))
        /*TODO(add list of items)*/
    }
}