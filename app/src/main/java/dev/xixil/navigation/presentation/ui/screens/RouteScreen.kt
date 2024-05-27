package dev.xixil.navigation.presentation.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import com.google.ar.core.TrackingState
import dev.xixil.navigation.R
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.presentation.ui.common.SmallTextField
import dev.xixil.navigation.presentation.ui.common.TravelTimeBar
import dev.xixil.navigation.presentation.ui.navigation.Screen
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme
import dev.xixil.navigation.presentation.utils.DisplayRotationHelper
import dev.xixil.navigation.presentation.utils.DrawerHelper
import dev.xixil.navigation.presentation.utils.rememberCameraNode2
import dev.xixil.navigation.presentation.viewmodels.RouteViewModel
import dev.xixil.navigation.presentation.viewmodels.ViewModelState
import io.github.sceneview.ar.ARScene
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberView

@Composable
fun RouteScreen(
    source: String = "5",
    destination: String = "2",
    navController: NavController,
) {
    Log.d("RouteScreenParams", "$source $destination")
    val viewModel: RouteViewModel = hiltViewModel()

    ARNavigationTheme {
        RouteScreenContent(
            source = source,
            destination = destination,
            viewModel = viewModel,
            onChooseSource = { navController.navigate(Screen.Scanner.route) },
            onChooseDestination = { navController.navigate("${Screen.Search.route}?$SOURCE_PARAM_KEY=$source") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RouteScreenContent(
    source: String,
    destination: String,
    viewModel: RouteViewModel,
    onChooseSource: () -> Unit,
    onChooseDestination: () -> Unit,
) {
    val context = LocalContext.current

    val sourceTextField by remember { mutableStateOf(source) }
    val destinationTextField by remember { mutableStateOf(destination) }

    val sourceVertex by produceState<ViewModelState<Vertex>>(
        initialValue = ViewModelState.None(),
        source
    ) {
        value = viewModel
            .getVertex(source)
            .await()
    }

    val destinationVertex by produceState<ViewModelState<Vertex>>(
        initialValue = ViewModelState.None(),
        destination
    ) {
        value = viewModel
            .getVertex(destination)
            .await()
    }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val cameraNode = rememberCameraNode2(engine)
    val childNodes = rememberNodesMap()
    val view = rememberView(engine)
    val collisionSystem = rememberCollisionSystem(view)
    var frame by remember { mutableStateOf<Frame?>(null) }
    val modelInstances = remember { mutableListOf<ModelInstance>() }
    var trackingFailureReason by remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }

    val scaffoldState = rememberBottomSheetScaffoldState()
    val hours by remember { mutableIntStateOf(0) }
    val minutes by remember { mutableIntStateOf(0) }

    val graphState = viewModel.graph.collectAsState().value

    val drawerHelper = DrawerHelper()
    val displayRotationHelper = DisplayRotationHelper(context)

    BottomSheetScaffold(
        sheetPeekHeight = 48.dp,
        scaffoldState = scaffoldState,
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            BottomSheetTextFields(
                onChooseSource,
                sourceTextField,
                onChooseDestination,
                destinationTextField,
                hours,
                minutes
            )

            Button(
                enabled = sourceTextField.isNotBlank() && destinationTextField.isNotBlank(),
                onClick = {
                    if (childNodes.isEmpty() && cameraNode.trackingState == TrackingState.TRACKING) {
                        when (graphState) {
                            is ViewModelState.Error -> Toast.makeText(
                                context,
                                "Error",
                                Toast.LENGTH_SHORT
                            ).show()

                            is ViewModelState.Loading -> Toast.makeText(
                                context,
                                "Loading",
                                Toast.LENGTH_SHORT
                            ).show()

                            is ViewModelState.None -> {}
                            is ViewModelState.Success -> {
                                if (sourceVertex is ViewModelState.Success && destinationVertex is ViewModelState.Success) {
                                    val path = viewModel.findPath(
                                        start = (sourceVertex as ViewModelState.Success<Vertex>).data,
                                        destination = (destinationVertex as ViewModelState.Success<Vertex>).data,
                                        graph = graphState.data
                                    )

                                    path.forEach { vertex ->
                                        if (childNodes.isEmpty() && cameraNode.trackingState == TrackingState.TRACKING) {
                                            cameraNode.session?.let { currentSession ->
                                                drawerHelper.createAndResolveNode(
                                                    modelInstances = modelInstances,
                                                    modelLoader = modelLoader,
                                                    cloudAnchorId = vertex.cloudAnchorId,
                                                    engine = engine,
                                                    session = currentSession,
                                                    onSuccess = { node ->
                                                        childNodes[node] = vertex
                                                    },
                                                    onError = {
                                                        Toast.makeText(
                                                            context,
                                                            "Can`t resolve node",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            ) {
                Text("Go!")
            }
        }) { innerPadding ->
        ARScene(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            childNodes = childNodes.keys.toList(),
            lifecycle = lifecycleOwner.lifecycle,
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
            planeRenderer = false,
            onTrackingFailureChanged = {
                trackingFailureReason = it
            },
            onSessionResumed = {
                displayRotationHelper.onResume()
                cameraNode.onTrackingStateChanged(TrackingState.TRACKING)
            },
            onSessionPaused = {
                displayRotationHelper.onPause()
                cameraNode.onTrackingStateChanged(TrackingState.PAUSED)
                it.close()
            },
            onSessionUpdated = { _, updatedFrame ->
                frame = updatedFrame
            },
        )
    }
}

@Composable
private fun BottomSheetTextFields(
    onChooseSource: () -> Unit,
    sourceTextField: String,
    onChooseDestination: () -> Unit,
    destinationTextField: String,
    hours: Int,
    minutes: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SmallTextField(
            modifier = Modifier
                .weight(0.5f)
                .clickable {
                    onChooseSource()
                },
            placeholder = sourceTextField.ifBlank {
                stringResource(
                    id = R.string.from_here_placeholder_text
                )
            },
        )
        SmallTextField(
            modifier = Modifier
                .weight(0.5f)
                .clickable {
                    onChooseDestination()
                },
            placeholder = destinationTextField.ifBlank {
                stringResource(
                    id = R.string.to_placeholder_text
                )
            }
        )
    }
    if (sourceTextField.isNotBlank() && destinationTextField.isNotBlank()) {
        TravelTimeBar(hours = hours, minutes = minutes)
    }
}