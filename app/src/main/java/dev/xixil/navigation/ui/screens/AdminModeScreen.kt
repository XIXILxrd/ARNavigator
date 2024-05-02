package dev.xixil.navigation.ui.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import dev.romainguy.kotlin.math.Float3
import dev.xixil.navigation.R
import dev.xixil.navigation.ui.annotations.DefaultPreview
import dev.xixil.navigation.ui.common.SmallPrimitiveButton
import dev.xixil.navigation.ui.common.SmallTextField
import dev.xixil.navigation.ui.theme.ARNavigationTheme
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.collision.Quaternion
import io.github.sceneview.collision.Vector3
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.toFloat3
import io.github.sceneview.math.toNewQuaternion
import io.github.sceneview.math.toRotation
import io.github.sceneview.math.toVector3
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView


@Composable
fun AdminModeScreen() {
    ARNavigationTheme {
        AdminModeContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminModeContent() {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(scaffoldState = scaffoldState,
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
                    ""
                }
                SmallTextField(
                    modifier = Modifier.weight(0.5f),
                    placeholder = stringResource(id = R.string.from_text)
                ) {
                    ""
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                SmallPrimitiveButton(text = stringResource(R.string.remove_button_text)) {

                }
                SmallPrimitiveButton(text = stringResource(R.string.link_button_text)) {

                }
                SmallPrimitiveButton(text = stringResource(R.string.add_button_text)) {

                }
            }
        }) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Cyan)
        ) {
            ARAdminContent()
        }
    }
}

@Composable
private fun ARAdminContent(modifier: Modifier = Modifier) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val cameraNode = rememberARCameraNode(engine)
    val childNodes = rememberNodes()
    val view = rememberView(engine)
    val collisionSystem = rememberCollisionSystem(view)

    var planeRenderer by remember { mutableStateOf(true) }

    val modelInstances = remember { mutableListOf<ModelInstance>() }
    var trackingFailureReason by remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }
    var frame by remember { mutableStateOf<Frame?>(null) }

    var clickedNode by remember { mutableStateOf<Node?>(null) }

    ARScene(
        modifier = modifier.fillMaxSize(),
        childNodes = childNodes,
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
        onGestureListener = rememberOnGestureListener(onSingleTapConfirmed = { motionEvent, node ->
            if (node == null) {
                val hitResults = frame?.hitTest(motionEvent.x, motionEvent.y)
                hitResults?.firstOrNull {
                    it.isValid(
                        depthPoint = true,
                        point = false,
                    )
                }?.createAnchorOrNull()?.let { anchor ->
                    planeRenderer = false
                    childNodes += createVertexAnchor(
                        engine = engine,
                        modelLoader = modelLoader,
                        materialLoader = materialLoader,
                        modelInstances = modelInstances,
                        anchor = anchor
                    )
                }
            }
        }, onLongPress = { _, node ->
            removeNode(node)
        }, onDoubleTap = { motionEvent, node ->
            if (node == null) {
                return@rememberOnGestureListener
            }

            if (clickedNode == null) {
                clickedNode = node
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
                    childNodes += createEdgeAnchor(
                        destinationVertex = node,
                        sourceVertex = clickedNode!!,
                        engine = engine,
                        anchor = anchor
                    )
                }
                clickedNode = null
            }
        })
    )
}

private fun createEdgeAnchor(
    destinationVertex: Node,
    sourceVertex: Node,
    engine: Engine,
    anchor: Anchor,
): Node {
    val pointA = sourceVertex.worldPosition.toVector3()
    val pointB = destinationVertex.worldPosition.toVector3()

    val difference = Vector3.subtract(pointA, pointB)
    val directionFromTopToBottom = difference.normalized()
    val rotationFromAToB = Quaternion.lookRotation(directionFromTopToBottom, Vector3.up())

    val anchorNode = AnchorNode(engine = engine, anchor = anchor)
    val edgeNode = CubeNode(
        engine = engine,
        size = Float3(0.01f, 0.01f, difference.length()),
        center = Vector3.zero().toFloat3()
    ).apply {
        parent = anchorNode
        worldPosition = Vector3.add(pointA, pointB).scaled(0.5f).toFloat3()
        worldRotation = rotationFromAToB.toNewQuaternion().toRotation()
    }

    return edgeNode
}

private fun removeNode(node: Node?) {
    node?.destroy()
}

private fun createVertexAnchor(
    engine: Engine,
    modelLoader: ModelLoader,
    materialLoader: MaterialLoader,
    modelInstances: MutableList<ModelInstance>,
    anchor: Anchor,
): Node {
    val anchorNode = AnchorNode(engine = engine, anchor = anchor)
    val modelNode = ModelNode(
        modelInstance = modelInstances.apply {
            if (isEmpty()) {
                this += modelLoader.createInstancedModel("models/cylinder.glb", 1)
            }
        }.removeLast(), scaleToUnits = 0.1f
    ).apply {
        isEditable = true
        isPositionEditable = false
        isRotationEditable = false
        isShadowCaster = false
        isShadowReceiver = false
    }

    val boundingBoxNode = CubeNode(
        engine,
        size = modelNode.extents,
        center = modelNode.center,
        materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
    ).apply {
        isVisible = false
    }

    modelNode.addChildNode(boundingBoxNode)
    anchorNode.addChildNode(modelNode)

    listOf(modelNode, anchorNode).forEach {
        it.onEditingChanged = { editingTransforms ->
            boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
        }
    }

    return modelNode
}

@DefaultPreview
@Composable
private fun AdminModeContentPreview() {
    ARNavigationTheme {
        AdminModeContent()
    }
}
