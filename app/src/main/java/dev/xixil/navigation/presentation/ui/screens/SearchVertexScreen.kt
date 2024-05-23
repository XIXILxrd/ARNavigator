package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.xixil.navigation.domain.models.Record
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.presentation.ui.common.Tabs
import dev.xixil.navigation.presentation.ui.common.TopSearchBar
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme
import dev.xixil.navigation.presentation.viewmodels.SearchViewModel
import dev.xixil.navigation.presentation.viewmodels.ViewModelState

@Composable
fun SearchVertexScreen(
    onSelectedAudience: (String) -> Unit,
) {
    val viewModel: SearchViewModel = hiltViewModel()

    val audiences = viewModel.audiencesState.collectAsState()
    val records = viewModel.recordsState.collectAsState()

    ARNavigationTheme {
        when(val audiencesList = audiences.value) {
            is ViewModelState.Error -> {
                when(val recordsList = records.value) {
                    is ViewModelState.Error -> SearchVertexContent(audiences = emptyList(), recent = emptyList())
                    is ViewModelState.Loading -> SearchVertexLoading()
                    is ViewModelState.None -> {}
                    is ViewModelState.Success -> SearchVertexContent(audiences = emptyList(), recent = recordsList.data)
                }
            }
            is ViewModelState.Loading -> SearchVertexLoading()
            is ViewModelState.None -> {}
            is ViewModelState.Success -> {
                when(val recordsList = records.value) {
                    is ViewModelState.Error -> SearchVertexContent(audiences = emptyList(), recent = emptyList())
                    is ViewModelState.Loading -> SearchVertexLoading()
                    is ViewModelState.None -> {}
                    is ViewModelState.Success -> SearchVertexContent(audiences = audiencesList.data, recent = recordsList.data)
                }
            }
        }

    }
}

@Composable
private fun SearchVertexContent(audiences: List<Vertex>, recent: List<Record>) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopSearchBar(query = "")
        Tabs(modifier = Modifier.padding(horizontal = 16.dp), audiencesTabContent = audiences, recentContent = recent)
    }
}

@Composable
private fun SearchVertexLoading() {
    Column(modifier = Modifier.fillMaxSize()) {
        TopSearchBar(query = "")
        Tabs(modifier = Modifier.padding(horizontal = 16.dp), audiencesTabContent = emptyList(), recentContent = emptyList())
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }
}