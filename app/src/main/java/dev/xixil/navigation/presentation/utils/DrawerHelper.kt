package dev.xixil.navigation.presentation.utils


import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Session
import dev.romainguy.kotlin.math.Float3
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.presentation.viewmodels.ViewModelState
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.node.CloudAnchorNode
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
    fun calculateWeight(source: Vertex, destination: Vertex): Long {
        return Vector3.subtract(
            source.coordinates.toVector3(),
            destination.coordinates.toVector3()
        ).length().toLong()
    }

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
            worldPosition = Vector3.add(pointA, pointB).scaled(0.5f).toFloat3()
            worldRotation = rotationFromAToB.toNewQuaternion().toRotation()
        }

        return edgeNode
    }

    fun drawEdge(
        destinationVertex: Float3,
        sourceVertex: Float3,
        engine: Engine,
    ): Node {
        val pointA = sourceVertex.toVector3()
        val pointB = destinationVertex.toVector3()

        val difference = Vector3.subtract(pointA, pointB)
        val directionFromTopToBottom = difference.normalized()
        val rotationFromAToB = Quaternion.lookRotation(directionFromTopToBottom, Vector3.up())

        val edgeNode = CubeNode(
            engine = engine,
            size = Float3(0.01f, 0.01f, difference.length()),
            center = Vector3.zero().toFloat3()
        ).apply {
            worldPosition = Vector3.add(pointA, pointB).scaled(0.5f).toFloat3()
            worldRotation = rotationFromAToB.toNewQuaternion().toRotation()
        }

        return edgeNode
    }

    fun loadGraph(
        graphState: State<ViewModelState>,
        context: Context,
        engine: Engine,
        session: Session,
        modelInstances: MutableList<ModelInstance>,
        modelLoader: ModelLoader,
        childNodes: SnapshotStateMap<Node, Any>,
    ) {
        when (val state = graphState.value) {
            is ViewModelState.Error ->
                Toast.makeText(
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
                state.graph.values.map { listOfEdges ->
                    listOfEdges.forEach { edge ->
                        val sourceNode = ModelNode(
                            modelInstance = modelInstances.apply {
                                if (isEmpty()) {
                                    this += modelLoader.createInstancedModel(
                                        "models/cylinder.glb",
                                        1
                                    )
                                }
                            }.removeLast(), scaleToUnits = 0.1f
                        )

                        CloudAnchorNode.resolve(
                            engine = engine,
                            session = session,
                            edge.source.cloudAnchorId
                        ) { state, node ->
                            if (state == Anchor.CloudAnchorState.SUCCESS && node != null) {
                                sourceNode.parent = node
                                childNodes[sourceNode] = edge.source
                            } else {
                                sourceNode.destroy()
                                Toast.makeText(
                                    context,
                                    "Cant resolve vertex",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        val destinationNode = ModelNode(
                            modelInstance = modelInstances.apply {
                                if (isEmpty()) {
                                    this += modelLoader.createInstancedModel(
                                        "models/cylinder.glb",
                                        1
                                    )
                                }
                            }.removeLast(), scaleToUnits = 0.1f
                        )

                        CloudAnchorNode.resolve(
                            engine = engine,
                            session = session,
                            edge.destination.cloudAnchorId
                        ) { state, node ->
                            if (state == Anchor.CloudAnchorState.SUCCESS && node != null) {
                                destinationNode.parent = node
                                childNodes[destinationNode] = edge.destination
                            } else {
                                Toast.makeText(
                                    context,
                                    "Cant resolve vertex",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }


                        val edgeNode = drawEdge(
                            sourceVertex = edge.source.coordinates,
                            destinationVertex = edge.destination.coordinates,
                            engine = engine
                        )

                        childNodes[edgeNode] = edge

                        CloudAnchorNode.resolve(
                            engine = engine,
                            session = session,
                            cloudAnchorId = edge.cloudAnchorId
                        ) { state, node ->
                            if (state == Anchor.CloudAnchorState.SUCCESS && node != null) {
                                edgeNode.addChildNode(node)
                                childNodes[edgeNode] = edge
                            } else {
                                edgeNode.destroy()
                                Toast.makeText(
                                    context,
                                    "Cant resolve edge",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun <K, V> Map<K, V>.getKeyByValue(value: V): K? {
        for ((key, entryValue) in this) {
            if (entryValue == value) {
                return key
            }
        }
        return null
    }
}