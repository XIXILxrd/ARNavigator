package dev.xixil.navigation.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import dev.xixil.navigation.R
import dev.xixil.navigation.data.database.graph.GraphDatabase
import dev.xixil.navigation.data.repository.GraphRepositoryImplementation
import dev.xixil.navigation.presentation.ui.annotations.DefaultPreview
import dev.xixil.navigation.presentation.ui.common.SmallTextField
import dev.xixil.navigation.presentation.ui.common.TravelTimeBar
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberView

@Composable
fun RouteScreen() {
    ARNavigationTheme {
        RouteScreenContent()
    }
}

@DefaultPreview
@Composable
private fun RouteScreenPreview() {
    ARNavigationTheme {
        RouteScreenContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RouteScreenContent(
    from: String = "1",
    to: String = "2",
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val hours by remember { mutableIntStateOf(0) }
    val minutes by remember { mutableIntStateOf(0) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SmallTextField(
                    modifier = Modifier.weight(0.5f),
                    placeholder = stringResource(id = R.string.to_text)
                ) {
                    from
                }
                SmallTextField(
                    modifier = Modifier.weight(0.5f),
                    placeholder = stringResource(id = R.string.from_text)
                ) {
                    to
                }
            }

            TravelTimeBar(hours = hours, minutes = minutes)
        }) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Cyan)
        ) {
            ARRouteContent()
        }
    }
}

@Composable
private fun ARRouteContent(modifier: Modifier = Modifier) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val cameraNode = rememberARCameraNode(engine)
    val childNodes = rememberNodesMap()
    val view = rememberView(engine)
    val collisionSystem = rememberCollisionSystem(view)
    var frame by remember { mutableStateOf<Frame?>(null) }
    var planeRenderer by remember { mutableStateOf(true) }
    val modelInstances = remember { mutableListOf<ModelInstance>() }
    var trackingFailureReason by remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }

    val graphRepository =
        GraphRepositoryImplementation(graphDatabase = GraphDatabase(LocalContext.current))

    ARScene(
        modifier = Modifier.fillMaxSize(),
        childNodes = childNodes.keys.toList(),
        engine = engine,
        view = view,
        modelLoader = modelLoader,
        collisionSystem = collisionSystem,
        sessionConfiguration = { session, config ->
            config.depthMode =
                when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    true -> Config.DepthMode.AUTOMATIC
                    else -> Config.DepthMode.DISABLED
                }
            config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
            config.lightEstimationMode = Config.LightEstimationMode.DISABLED
        },
        cameraNode = cameraNode,
        planeRenderer = planeRenderer,
        onTrackingFailureChanged = {
            trackingFailureReason = it
        },
        onSessionCreated = { _ ->

        },
        onSessionUpdated = { _, updatedFrame ->
            frame = updatedFrame
        }
    )
}