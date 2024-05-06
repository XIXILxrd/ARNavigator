package dev.xixil.navigation.ui.screens

import android.widget.Toast
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import dev.xixil.navigation.R
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.ui.AdminModeViewModel
import dev.xixil.navigation.ui.DrawerHelper
import dev.xixil.navigation.ui.annotations.DefaultPreview
import dev.xixil.navigation.ui.common.SmallTextField
import dev.xixil.navigation.ui.theme.ARNavigationTheme
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.collision.Vector3
import io.github.sceneview.math.toVector3
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun AdminModeScreen(viewModel: AdminModeViewModel = hiltViewModel()) {
    ARNavigationTheme {
        AdminModeContent(
            onCreateVertex = { viewModel.createVertex(it) },
            onRemoveVertex = { viewModel.removeVertex(it) },
            onCreateEdge = { viewModel.createEdge(it) },
            onRemoveEdge = { viewModel.removeEdge(it) },
            onLoadGraph = { viewModel.getGraph() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminModeContent(
    onCreateVertex: (Vertex) -> Unit,
    onCreateEdge: (Edge) -> Unit,
    onRemoveVertex: (Vertex) -> Unit,
    onRemoveEdge: (Vertex) -> Unit,
    onLoadGraph: () -> Flow<Map<Vertex, List<Edge>>>,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(scaffoldState = scaffoldState,
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            BottomSheetContent()
        }) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Cyan)
        ) {
            ARAdminContent(
                onCreateVertex = onCreateVertex,
                onCreateEdge = onCreateEdge,
                onRemoveEdge = onRemoveEdge,
                onRemoveVertex = onRemoveVertex,
                onLoadGraph = onLoadGraph
            )
        }
    }
}

@Composable
fun BottomSheetContent(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SmallTextField(
            modifier = Modifier.weight(0.5f),
            placeholder = stringResource(id = R.string.to_text)
        ) {
            ""
        }
        SmallTextField(
            modifier = Modifier.weight(0.5f),
            placeholder = stringResource(id = R.string.from_text)
        ) {
            ""
        }
    }
}

@Composable
private fun ARAdminContent(
    onCreateVertex: (Vertex) -> Unit,
    onCreateEdge: (Edge) -> Unit,
    onRemoveVertex: (Vertex) -> Unit,
    onRemoveEdge: (Vertex) -> Unit,
    onLoadGraph: () -> Flow<Map<Vertex, List<Edge>>>,
    modifier: Modifier = Modifier, drawerHelper: DrawerHelper = DrawerHelper(),
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val cameraNode = rememberARCameraNode(engine)
    val childNodes = rememberNodesMap()
    val view = rememberView(engine)
    val collisionSystem = rememberCollisionSystem(view)

    val context = LocalContext.current

    var planeRenderer by remember { mutableStateOf(true) }

    val modelInstances = remember { mutableListOf<ModelInstance>() }
    var trackingFailureReason by remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }
    var frame by remember { mutableStateOf<Frame?>(null) }

    var clickedNode by remember { mutableStateOf<Node?>(null) }

    ARScene(
        modifier = modifier.fillMaxSize(),
        childNodes = childNodes.keys.toList(),
        engine = engine,
        view = view,
        modelLoader = modelLoader,
        collisionSystem = collisionSystem,
        sessionConfiguration = { session, config ->
            config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
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
        onSessionUpdated = { _, updatedFrame ->
            frame = updatedFrame
        },
        onSessionCreated = { _ ->
            frame?.getUpdatedPlanes()
                ?.firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
                    CoroutineScope(Dispatchers.Main).launch {
                        onLoadGraph.invoke().collect { graph ->
                            graph.values.map { edges ->
                                edges.map { edge ->
                                    drawerHelper.drawNodesFromDatabase(
                                        drawerHelper,
                                        engine,
                                        modelLoader,
                                        modelInstances,
                                        anchor,
                                        edge,
                                        childNodes
                                    )
                                }
                            }
                        }
                    }
                }
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
                        planeRenderer = false

                        val vertexNode = drawerHelper.drawVertex(
                            engine = engine,
                            modelLoader = modelLoader,
                            modelInstances = modelInstances,
                            anchor = anchor
                        )

                        childNodes[vertexNode] = Vertex(
                            id = Random.nextLong(),
                            data = null,
                            coordinates = vertexNode.worldPosition
                        ).also(onCreateVertex)
                    }
                }
            },
            onLongPress = { _, node ->
                if (node == null) {
                    return@rememberOnGestureListener
                }

                val element = childNodes[node] ?: childNodes[node.parent]

                when(element) {
                    is Edge -> {
                        onRemoveEdge(element.source)
                        node.destroy()
                        Toast.makeText(context, "Edge has been removed", Toast.LENGTH_SHORT).show()
                    }
                    is Vertex -> {
                        onRemoveVertex(element)
                        node.destroy()
                        Toast.makeText(context, "Vertex has been removed", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(context, "Node is null", Toast.LENGTH_SHORT).show()
                    }
                }

            },
            onDoubleTap = { motionEvent, node ->
                if (node == null) {
                    return@rememberOnGestureListener
                }

                if (clickedNode == null) {
                    clickedNode = node
                    return@rememberOnGestureListener
                }

                if (clickedNode != node) {
                    val hitResults = frame?.hitTest(motionEvent.x, motionEvent.y)
                    hitResults?.firstOrNull {
                        it.isValid(
                            depthPoint = true,
                            point = false,
                        )
                    }?.createAnchorOrNull()?.let { anchor ->
                        planeRenderer = false

                        val sourceVertex =
                            (childNodes[clickedNode] ?: childNodes[clickedNode?.parent]) as Vertex
                        val destinationVertex =
                            (childNodes[node] ?: childNodes[node.parent]) as Vertex

                        val edgeNode = drawerHelper.drawEdge(
                            destinationVertex = node,
                            sourceVertex = clickedNode!!,
                            engine = engine,
                            anchor = anchor
                        )

                        childNodes[edgeNode] = Edge(
                            id = Random.nextLong(), sourceVertex, destinationVertex,
                            Vector3.subtract(
                                sourceVertex.coordinates.toVector3(),
                                destinationVertex.coordinates.toVector3()
                            ).length().toLong()
                        ).also(onCreateEdge)
                    }

                    clickedNode = null
                }
            })
    )
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

@DefaultPreview
@Composable
private fun AdminModeContentPreview() {
    ARNavigationTheme {
        AdminModeScreen()
    }
}
