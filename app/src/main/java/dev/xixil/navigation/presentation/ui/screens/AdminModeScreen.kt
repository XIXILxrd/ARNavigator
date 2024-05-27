package dev.xixil.navigation.presentation.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import com.google.ar.core.TrackingState
import dev.xixil.navigation.R
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.presentation.MainActivity
import dev.xixil.navigation.presentation.ui.common.SmallTextField
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme
import dev.xixil.navigation.presentation.utils.DisplayRotationHelper
import dev.xixil.navigation.presentation.utils.DrawerHelper
import dev.xixil.navigation.presentation.utils.rememberCameraNode2
import dev.xixil.navigation.presentation.viewmodels.AdminModeViewModel
import dev.xixil.navigation.presentation.viewmodels.ViewModelState
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.canHostCloudAnchor
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.ARCameraNode
import io.github.sceneview.ar.node.CloudAnchorNode
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

@Composable
fun AdminModeScreen(
    audience: String,
    onAddAudience: () -> Unit,
) {
    val viewModel: AdminModeViewModel = hiltViewModel()
    ARNavigationTheme {
        AdminModeContent(
            audience = audience,
            onAddAudience = onAddAudience,
            onCreateVertex = { viewModel.createVertex(it) },
            onRemoveVertex = { viewModel.removeVertex(it) },
            onCreateEdge = { viewModel.createEdge(it) },
            onRemoveEdge = { viewModel.removeEdge(it) },
            onObserveGraph = viewModel.graphState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminModeContent(
    audience: String,
    modifier: Modifier = Modifier, drawerHelper: DrawerHelper = DrawerHelper(),
    onObserveGraph: StateFlow<ViewModelState<Map<Vertex, List<Edge>>>>,
    onAddAudience: () -> Unit,
    onCreateVertex: (Vertex) -> Unit,
    onCreateEdge: (Edge) -> Unit,
    onRemoveVertex: (Vertex) -> Unit,
    onRemoveEdge: (Edge) -> Unit,
) {
    var audienceNumber by remember { mutableStateOf(audience) }

    val lifecycleOwner: LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

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
    var clickedNode by remember { mutableStateOf<Node?>(null) }
    val context = LocalContext.current

    val graphState = onObserveGraph.collectAsState()

    val scaffoldState = rememberBottomSheetScaffoldState()

    val displayRotationHelper = DisplayRotationHelper(context)

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetPeekHeight = 48.dp,
        scaffoldState = scaffoldState,
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            BottomSheetContent(
                audienceNumber,
                modifier,
                cameraNode,
                drawerHelper,
                graphState,
                context,
                engine,
                modelInstances,
                modelLoader,
                childNodes,
                onAddAudience
            )
        }) { innerPadding ->

        ARScene(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            childNodes = childNodes.keys.toList(),
            engine = engine,
            view = view,
            lifecycle = lifecycleOwner.lifecycle,
            activity = LocalContext.current as MainActivity,
            modelLoader = modelLoader,
            collisionSystem = collisionSystem,
            sessionConfiguration = { session, config ->
                config.focusMode = Config.FocusMode.FIXED
                config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    true -> Config.DepthMode.AUTOMATIC
                    else -> Config.DepthMode.DISABLED
                }
                config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
                config.lightEstimationMode = Config.LightEstimationMode.DISABLED
                config.cloudAnchorMode = Config.CloudAnchorMode.ENABLED
            },
            cameraNode = cameraNode,
            planeRenderer = true,
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
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { motionEvent, node ->
                    if (node == null) {
                        val hitResults = frame?.hitTest(motionEvent.x, motionEvent.y)
                        hitResults?.firstOrNull {
                            it.isValid(
                                depthPoint = true,
                                point = false,
                            )
                        }?.createAnchorOrNull()?.let { anchor ->
                            val vertexNode = ModelNode(
                                modelInstance = modelInstances.apply {
                                    if (isEmpty()) {
                                        this += modelLoader.createInstancedModel(
                                            "models/cylinder.glb",
                                            1
                                        )
                                    }
                                }.removeLast(), scaleToUnits = 0.1f
                            )

                            if (cameraNode.trackingState == TrackingState.TRACKING) {
                                cameraNode.session?.let { currentSession ->
                                    if (!currentSession.canHostCloudAnchor(cameraNode)) {
                                        Toast.makeText(
                                            context,
                                            "Cant host cloud node",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()

                                        vertexNode.destroy()
                                        return@rememberOnGestureListener
                                    }

                                    CloudAnchorNode(engine = engine, anchor = anchor).apply {
                                        host(
                                            session = currentSession,
                                            ttlDays = 1
                                        ) { cloudAnchorId, state ->
                                            if (state == Anchor.CloudAnchorState.SUCCESS && cloudAnchorId != null) {
                                                vertexNode.parent = this

                                                childNodes[vertexNode] = Vertex(
                                                    id = Random.nextLong(),
                                                    data = if (audienceNumber.isNotBlank()) audience else "",
                                                    cloudAnchorId = cloudAnchorId,
                                                    coordinates = vertexNode.worldPosition
                                                ).also(onCreateVertex)

                                                audienceNumber = ""

                                                Toast.makeText(
                                                    context,
                                                    "Vertex has been added",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                childNodes.remove(vertexNode)
                                                vertexNode.destroy()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                onLongPress = { _, node ->
                    if (node == null) {
                        return@rememberOnGestureListener
                    }

                    childNodes[node]?.let { element ->
                        when (element) {
                            is Edge -> {
                                onRemoveEdge(element)
                                childNodes.remove(node)
                                Toast.makeText(context, "Edge has been removed", Toast.LENGTH_SHORT)
                                    .show()
                                node.destroy()
                                node.parent?.destroy()
                            }

                            is Vertex -> {
                                onRemoveVertex(element)
                                childNodes.remove(node)
                                Toast.makeText(
                                    context,
                                    "Vertex has been removed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                node.destroy()
                                node.parent?.destroy()
                            }
                        }
                    }
                },
                onDoubleTap = { _, node ->
                    if (node == null) {
                        return@rememberOnGestureListener
                    }

                    if (clickedNode == null) {
                        clickedNode = node
                        return@rememberOnGestureListener
                    }

                    if (clickedNode != node) {
                        val sourceVertex =
                            (childNodes[clickedNode] ?: childNodes[clickedNode?.parent]) as Vertex
                        val destinationVertex =
                            (childNodes[node] ?: childNodes[node.parent]) as Vertex

                        val edgeNode = drawerHelper.drawEdge(
                            destinationVertex = node,
                            sourceVertex = clickedNode!!,
                            engine = engine,
                        )

                        if (cameraNode.trackingState == TrackingState.TRACKING) {
                            cameraNode.session?.let { currentSession ->
                                frame?.getUpdatedPlanes()
                                    ?.firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                                    ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
                                        CloudAnchorNode(
                                            engine = engine, anchor = anchor
                                        ).apply {
                                            host(
                                                session = currentSession,
                                                ttlDays = 1
                                            ) { cloudAnchorId, state ->
                                                if (state == Anchor.CloudAnchorState.SUCCESS && cloudAnchorId != null) {
                                                    edgeNode.addChildNode(this)

                                                    childNodes[edgeNode] = Edge(
                                                        id = Random.nextLong(),
                                                        source = sourceVertex,
                                                        destination = destinationVertex,
                                                        cloudAnchorId = cloudAnchorId,
                                                        weight = drawerHelper.calculateWeight(
                                                            sourceVertex,
                                                            destinationVertex
                                                        )
                                                    ).also(onCreateEdge)

                                                    Toast.makeText(
                                                        context,
                                                        "Edge has been added",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    childNodes.remove(edgeNode)
                                                    edgeNode.destroy()
                                                }
                                            }
                                        }
                                    }
                            }
                        }

                        clickedNode = null
                    }
                })
        )
    }
}

@Composable
private fun BottomSheetContent(
    audience: String,
    modifier: Modifier,
    cameraNode: ARCameraNode,
    drawerHelper: DrawerHelper,
    graphState: State<ViewModelState<Map<Vertex, List<Edge>>>>,
    context: Context,
    engine: Engine,
    modelInstances: MutableList<ModelInstance>,
    modelLoader: ModelLoader,
    childNodes: SnapshotStateMap<Node, Any>,
    onAddAudience: () -> Unit,
) {
    val audienceField by remember {
        mutableStateOf(audience)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SmallTextField(
                modifier = Modifier
                    .clickable {
                        onAddAudience()
                    },
                placeholder = audienceField.ifBlank { stringResource(id = R.string.click_to_add_an_audience_text_placeholder) },
                textAlign = TextAlign.Center
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                if (cameraNode.trackingState == TrackingState.TRACKING) {
                    childNodes.clear()

                    when (val state = graphState.value) {
                        is ViewModelState.Error -> Toast.makeText(
                            context,
                            "An error occurred during graph initialization",
                            Toast.LENGTH_SHORT
                        ).show()

                        is ViewModelState.Loading -> Toast.makeText(
                            context,
                            "Loading the graph...",
                            Toast.LENGTH_SHORT
                        ).show()

                        is ViewModelState.None -> Toast.makeText(
                            context,
                            "None. ?:_)",
                            Toast.LENGTH_SHORT
                        ).show()

                        is ViewModelState.Success -> {
                            cameraNode.session?.let { currentSession ->
                                drawerHelper.loadGraph(
                                    state.data,
                                    context,
                                    engine,
                                    currentSession,
                                    modelInstances,
                                    modelLoader,
                                    childNodes
                                )
                            }
                        }
                    }
                }
            }) {
            Image(
                imageVector = Icons.Outlined.CloudDownload,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
fun rememberNodesMap(creator: MutableMap<Node, Any>.() -> Unit = {}) = remember {
    buildMap(creator).toList().toMutableStateMap()
}.also { nodes ->
    DisposableEffect(nodes) {
        onDispose {
            nodes.forEach { it.key.destroy() }
            nodes.clear()
        }
    }
}
