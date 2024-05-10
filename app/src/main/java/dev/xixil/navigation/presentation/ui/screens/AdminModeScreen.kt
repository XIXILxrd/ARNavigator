package dev.xixil.navigation.presentation.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.Anchor.CloudAnchorState
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import com.google.ar.core.TrackingState
import dev.romainguy.kotlin.math.Float3
import dev.xixil.navigation.R
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.presentation.ui.annotations.DefaultPreview
import dev.xixil.navigation.presentation.ui.common.SmallTextField
import dev.xixil.navigation.presentation.ui.theme.ARNavigationTheme
import dev.xixil.navigation.presentation.utils.DrawerHelper
import dev.xixil.navigation.presentation.viewmodels.AdminModeViewModel
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.CloudAnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.collision.Vector3
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.toVector3
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
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
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        modifier = Modifier.fillMaxWidth(),
        scaffoldState = scaffoldState,
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            BottomSheetContent()
        }) { innerPadding ->
        ARAdminContent(
            onCreateVertex = onCreateVertex,
            onCreateEdge = onCreateEdge,
            onRemoveEdge = onRemoveEdge,
            onRemoveVertex = onRemoveVertex,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
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
    modifier: Modifier = Modifier, drawerHelper: DrawerHelper = DrawerHelper(),
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

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
    var clickedNode by remember { mutableStateOf<Node?>(null) }
    val context = LocalContext.current

    ARScene(
        modifier = modifier.fillMaxSize(),
        childNodes = childNodes.keys.toList(),
        engine = engine,
        view = view,
        lifecycle = lifecycleOwner.lifecycle,
        modelLoader = modelLoader,
        collisionSystem = collisionSystem,
        sessionConfiguration = { session, config ->
            config.focusMode = Config.FocusMode.AUTO
            config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                true -> Config.DepthMode.AUTOMATIC
                else -> Config.DepthMode.DISABLED
            }
            config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
            config.lightEstimationMode = Config.LightEstimationMode.DISABLED
            config.cloudAnchorMode = Config.CloudAnchorMode.ENABLED
        },
        cameraNode = cameraNode,
        planeRenderer = planeRenderer,
        onTrackingFailureChanged = {
            trackingFailureReason = it
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
                        planeRenderer = false

                        scope.launch {
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

                            CloudAnchorNode(engine, anchor).apply {
                                addChildNode(vertexNode)
                            }.also {
                                it.host(
                                    cameraNode.session!!, 1
                                ) { cloudId, state ->
                                    childNodes[it] = Vertex(
                                        id = Random.nextLong(),
                                        data = cloudId,
                                        coordinates = vertexNode.worldPosition
                                    ).also(onCreateVertex)

                                    Toast.makeText(
                                        context,
                                        "$cloudId is $state",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    Log.d("CloudAnchorTest", "$cloudId")
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

                val element = childNodes[node] ?: childNodes[node.parent]

                when (element) {
                    is Edge -> {
                        onRemoveEdge(element.source)
                        node.destroy()
                        node.parent?.destroy()
                    }

                    is Vertex -> {
                        onRemoveVertex(element)
                        node.destroy()
                        node.parent?.destroy()
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

                    childNodes[edgeNode] = Edge(
                        id = Random.nextLong(), sourceVertex, destinationVertex,
                        Vector3.subtract(
                            sourceVertex.coordinates.toVector3(),
                            destinationVertex.coordinates.toVector3()
                        ).length().toLong()
                    ).also(onCreateEdge)

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
