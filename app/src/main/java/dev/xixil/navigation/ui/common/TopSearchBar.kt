package dev.xixil.navigation.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.xixil.navigation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    @StringRes placeholderText: Int = R.string.search_placeholder_text,
) {
    var isActive by rememberSaveable { mutableStateOf(false) }
    var queryText by rememberSaveable { mutableStateOf(query) }

    SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        query = queryText,
        onQueryChange = { queryText = it },
        onSearch = { isActive = false },
        active = isActive,
        onActiveChange = { isActive = it },
        placeholder = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 48.dp),
                text = stringResource(id = placeholderText),
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.search_content_description_text)
            )
        }
    ) {
        /*TODO
        *  implement suggest list*/
    }
}

@Composable
fun AudienceItem(
    modifier: Modifier = Modifier,
    audienceNumberText: String,
    subtitleText: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(vertical = 16.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = audienceNumberText,
            maxLines = 1,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
        Text(
            text = subtitleText,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFA09CAB)
        )
    }
}

@Composable
fun AudienceHistoryItem(
    modifier: Modifier = Modifier,
    audienceNumberText: String,
    subtitleText: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.History,
            tint = Color(0xFFA09CAB),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = audienceNumberText,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Text(
                text = subtitleText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFA09CAB) //TODO Implement in materialtheme color
            )
        }
    }
}

@Composable
fun AudienceList(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    list: List<String>,
) {
    LazyColumn {
        items(list) {audience ->
            /*TODO*/
        }
    }
}


@Preview
@Composable
private fun ItemHistoryPreview() {
    AudienceHistoryItem(audienceNumberText = "First List Title", subtitleText = "1st subtitle")
}

@Preview
@Composable
private fun ItemPreview() {
    AudienceItem(audienceNumberText = "First List Title", subtitleText = "1st subtitle")
}

@Preview(showSystemUi = true)
@Composable
private fun SearchbarPreview() {
    TopSearchBar(query = "")
}

