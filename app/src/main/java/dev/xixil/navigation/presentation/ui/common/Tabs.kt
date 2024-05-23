package dev.xixil.navigation.presentation.ui.common

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import dev.xixil.navigation.R
import dev.xixil.navigation.domain.models.Vertex

sealed class TabItem(
    open val titleResId: Int,
    open val list: List<Any>,
    open val screen: @Composable () -> Unit,
) {
    class Audiences(
        override val titleResId: Int = R.string.audiences_text,
        override val list: List<Vertex>,
        override val screen: @Composable () -> Unit,
    ) : TabItem(titleResId, list, screen)

    class Recent(
        override val titleResId: Int = R.string.recent_text,
        override val list: List<dev.xixil.navigation.domain.models.Record>,
        override val screen: @Composable () -> Unit,
    ) : TabItem(titleResId, list, screen)
}

@Composable
fun Tabs(
    modifier: Modifier = Modifier,
    audiencesTabContent: List<Vertex>,
    recentContent: List<dev.xixil.navigation.domain.models.Record>,
) {
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }

    val tabs = listOf(
        TabItem.Audiences(
            list = audiencesTabContent,
            screen = {
                LazyColumn {
                    items(audiencesTabContent) { vertex ->
                        AudienceItem(audienceNumberText = "${vertex.data}")
                    }
                }
            }
        ),
        TabItem.Recent(
            list = recentContent,
            screen = {
                LazyColumn {
                    items(recentContent) { record ->
                        AudienceHistoryItem(audienceNumberText = record.end)
                    }
                }
            }
        )
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
                            tabItem.screen
                        },
                        text = stringResource(id = tabItem.titleResId)
                    )
                }
            }
        )
    }

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
    onClick: () -> Unit,
    text: String,
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