package dev.xixil.navigation.presentation.utils


import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import dev.romainguy.kotlin.math.Float3
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
        anchor: Anchor,
        modelLoader: ModelLoader,
        modelInstances: MutableList<ModelInstance>,
    ): Node {
        val anchorNode = AnchorNode(engine, anchor).apply {
            isEditable = false
            isPositionEditable = false
            isRotationEditable = false
        }
        val modelNode = ModelNode(
            modelInstance = modelInstances.apply {
                if (isEmpty()) {
                    this += modelLoader.createInstancedModel("models/cylinder.glb", 1)
                }
            }.removeLast(), scaleToUnits = 0.1f
        ).apply {
            parent = anchorNode
            isEditable = false
            isPositionEditable = false
            isRotationEditable = false
            isShadowCaster = false
            isShadowReceiver = false
        }

        return anchorNode
    }

    fun drawEdge(
        destinationVertex: Node,
        sourceVertex: Node,
        engine: Engine,
    ): Node {
        val pointA = sourceVertex.worldPosition.toVector3()
        val pointB = destinationVertex.worldPosition.toVector3()

        val difference = Vector3.subtract(pointA, pointB)
        val directionFromTopToBottom = difference.normalized()
        val rotationFromAToB = Quaternion.lookRotation(directionFromTopToBottom, Vector3.up())

        val edgeNode = CubeNode(
            engine = engine,
            size = Float3(0.01f, 0.01f, difference.length()),
            center = Vector3.zero().toFloat3()
        ).apply {
            position = Vector3.add(pointA, pointB).scaled(0.5f).toFloat3()
            rotation = rotationFromAToB.toNewQuaternion().toRotation()
        }

        return edgeNode
    }
}