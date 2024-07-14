package dev.xixil.navigation.presentation.ui.common

import android.util.Log
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import dev.xixil.navigation.R
import dev.xixil.navigation.presentation.ui.common.TabItem.Audiences.onItemClicked
import dev.xixil.navigation.presentation.viewmodels.SearchViewModel
import dev.xixil.navigation.presentation.viewmodels.ViewModelState

sealed class TabItem(
    val titleResId: Int,
    val screen: @Composable () -> Unit,
    var onItemClicked: (String) -> Unit,
) {
    data object Audiences :
        TabItem(R.string.audiences_text, { AudiencesList(onItemClicked = { onItemClicked(it) }) }, {})

    data object Recent :
        TabItem(R.string.recent_text, { RecentAudiences(onItemClicked = { onItemClicked(it) }) }, {})
}


@Composable
fun Tabs(
    modifier: Modifier = Modifier,
    onItemClicked: (String) -> Unit,
) {
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }

    val tabs = listOf(
        TabItem.Audiences,
        TabItem.Recent
    )

    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(35.dp),
        color = Color.Black
    ) {
        TabRow(
            modifier = Modifier
                .fillMaxWidth(),
            selectedTabIndex = tabIndex,
            containerColor = Color(0xFFEFF1F5),
            indicator = { tabPositions ->
                CustomIndicator(tabPositions, tabIndex)
            },
            divider = { Spacer(modifier = Modifier.width(6.dp)) },
            tabs = {
                tabs.forEachIndexed { index, tabItem ->
                    CustomTab(
                        selected = tabIndex == index,
                        onClick = {
                            tabIndex = index
                        },
                        text = stringResource(id = tabItem.titleResId)
                    )
                    tabItem.onItemClicked = onItemClicked
                }
            }
        )

    }

    tabs[tabIndex].screen()
}

@Composable
private fun CustomIndicator(tabPositions: List<TabPosition>, tabIndex: Int) {
    val transition = updateTransition(tabIndex, label = "custom_index")
    val indicatorStart by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 50f)
            } else {
                spring(dampingRatio = 1f, stiffness = 1000f)
            }
        }, label = ""
    ) {
        tabPositions[it].left
    }

    val indicatorEnd by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 1000f)
            } else {
                spring(dampingRatio = 1f, stiffness = 50f)
            }
        }, label = ""
    ) {
        tabPositions[it].right
    }

    Box(
        Modifier
            .offset(x = indicatorStart)
            .wrapContentSize(align = Alignment.BottomStart)
            .width(indicatorEnd - indicatorStart)
            .fillMaxSize()
            .background(color = Color(0xFF004394), RoundedCornerShape(35.dp))
            .zIndex(1f)
    )
}

@Composable
private fun CustomTab(
    modifier: Modifier = Modifier,
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
) {
    Tab(
        modifier = modifier
            .zIndex(6f)
            .wrapContentSize(),
        selected = selected,
        onClick = onClick,
        selectedContentColor = Color.White,
        unselectedContentColor = Color(0xFFA09CAB)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = text,
            maxLines = 1,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun AudiencesList(modifier: Modifier = Modifier, onItemClicked: (String) -> Unit) {
    val viewModel: SearchViewModel = hiltViewModel()

    val audiences = viewModel.audiencesState.collectAsState()

    when (val list = audiences.value) {
        is ViewModelState.Error -> EmptyListScreen()
        is ViewModelState.Loading -> LoadingListScreen()
        is ViewModelState.None -> {}
        is ViewModelState.Success -> {
            SuccessAudiencesListScreen(
                modifier = modifier,
                list = list.data.map { "${it.data}" },
            ) {
                onItemClicked(it)
            }
        }
    }
}

@Composable
private fun SuccessAudiencesListScreen(
    modifier: Modifier = Modifier,
    list: List<String>,
    onItemClicked: (String) -> Unit,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(list) { audience ->
            AudienceItem(
                audienceNumberText = audience,
                onItemClicked = {
                    onItemClicked(it)
                    Log.d("NavParamsCheck", "Item search: $it")
                }
            )
        }
    }
}

@Composable
private fun SuccessRecordsListScreen(
    modifier: Modifier = Modifier,
    list: List<String>,
    onItemClicked: (String) -> Unit,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(list) { audience ->
            AudienceHistoryItem(
                audienceNumberText = audience,
                onItemClicked = { onItemClicked(it)
                    Log.d("NavParamsCheck", "ListScreen search: $it")
                }
            )
        }
    }
}

@Composable
private fun RecentAudiences(modifier: Modifier = Modifier, onItemClicked: (String) -> Unit) {
    val viewModel: SearchViewModel = hiltViewModel()

    val records = viewModel.recordsState.collectAsState()

    when (val list = records.value) {
        is ViewModelState.Error -> EmptyListScreen()
        is ViewModelState.Loading -> LoadingListScreen()
        is ViewModelState.None -> {}
        is ViewModelState.Success -> {
            SuccessRecordsListScreen(modifier = modifier, list = list.data.map { it.end }) {
                onItemClicked(it)
            }
        }
    }
}


@Composable
fun LoadingListScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun EmptyListScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Empty",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge
        )
    }
}