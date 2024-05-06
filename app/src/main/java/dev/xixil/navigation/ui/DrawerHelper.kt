package dev.xixil.navigation.ui


import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import dev.romainguy.kotlin.math.Float3
import dev.xixil.navigation.domain.models.Edge
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.collision.Quaternion
import io.github.sceneview.collision.Vector3
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.toFloat3
import io.github.sceneview.math.toNewQuaternion
import io.github.sceneview.math.toRotation
import io.github.sceneview.math.toVector3
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node

class DrawerHelper {
    fun drawVertex(
        engine: Engine,
        modelLoader: ModelLoader,
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
            isEditable = false
            isPositionEditable = false
            isRotationEditable = false
            isShadowCaster = false
            isShadowReceiver = false
        }

        anchorNode.addChildNode(modelNode)

        return anchorNode
    }

    fun drawEdge(
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

    fun drawNodesFromDatabase(
        drawerHelper: DrawerHelper,
        engine: Engine,
        modelLoader: ModelLoader,
        modelInstances: MutableList<ModelInstance>,
        anchor: Anchor,
        edge: Edge,
        childNodes: SnapshotStateMap<Node, Any>,
    ) {
        val sourceVertex = drawerHelper.drawVertex(
            engine = engine,
            modelLoader = modelLoader,
            modelInstances = modelInstances,
            anchor = anchor
        ).apply {
            worldPosition = edge.source.coordinates
        }

        val destinationVertex = drawerHelper.drawVertex(
            engine = engine,
            modelLoader = modelLoader,
            modelInstances = modelInstances,
            anchor = anchor
        ).apply {
            worldPosition = edge.destination.coordinates
        }

        val edgeNode = drawerHelper.drawEdge(
            sourceVertex = sourceVertex,
            destinationVertex = destinationVertex,
            engine = engine,
            anchor = anchor
        )

        childNodes[sourceVertex] = edge.source
        childNodes[destinationVertex] = edge.destination
        childNodes[edgeNode] = edge
    }
}