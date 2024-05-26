package dev.xixil.navigation.presentation.utils


import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Session
import dev.romainguy.kotlin.math.Float3
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
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
        return (Vector3.subtract(
            source.coordinates.toVector3(),
            destination.coordinates.toVector3()
        ).length() * 100).toLong()
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

    fun createAndResolveNode(
        modelInstances: MutableList<ModelInstance>,
        modelLoader: ModelLoader,
        cloudAnchorId: String,
        engine: Engine,
        session: Session,
        onSuccess: (Node) -> Unit,
        onError: () -> Unit,
    ) {
        val modelNode = ModelNode(
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
            cloudAnchorId
        ) { state, node ->
            if (state == Anchor.CloudAnchorState.SUCCESS && node != null) {
                modelNode.parent = node
                onSuccess(modelNode)
            } else {
                modelNode.destroy()
                onError()
            }
        }
    }

    private fun createAndResolveEdge(
        edge: Edge,
        engine: Engine,
        session: Session,
        sourceNode: Node,
        destinationNode: Node,
        childNodes: SnapshotStateMap<Node, Any>,
        context: Context,
    ) {
        val edgeNode = drawEdge(
            sourceVertex = sourceNode,
            destinationVertex = destinationNode,
            engine = engine
        )

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
                    "Can't resolve edge",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun loadGraph(
        graph: Map<Vertex, List<Edge>>,
        context: Context,
        engine: Engine,
        session: Session,
        modelInstances: MutableList<ModelInstance>,
        modelLoader: ModelLoader,
        childNodes: SnapshotStateMap<Node, Any>,
    ) {
        graph.values.forEach { listOfEdges ->
            listOfEdges.forEach { edge ->
                createAndResolveNode(
                    modelInstances = modelInstances,
                    modelLoader = modelLoader,
                    cloudAnchorId = edge.source.cloudAnchorId,
                    engine = engine,
                    session = session,
                    onSuccess = { sourceNode ->
                        childNodes[sourceNode] = edge.source

                        createAndResolveNode(
                            modelInstances = modelInstances,
                            modelLoader = modelLoader,
                            cloudAnchorId = edge.destination.cloudAnchorId,
                            engine = engine,
                            session = session,
                            onSuccess = { destinationNode ->
                                childNodes[destinationNode] = edge.destination
                                createAndResolveEdge(
                                    edge = edge,
                                    engine = engine,
                                    session = session,
                                    sourceNode = sourceNode,
                                    destinationNode = destinationNode,
                                    childNodes = childNodes,
                                    context = context
                                )
                            },
                            onError = {
                                Toast.makeText(
                                    context,
                                    "Can't resolve destination vertex",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },
                    onError = {
                        Toast.makeText(
                            context,
                            "Can't resolve source vertex",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}


//private fun <K, V> Map<K, V>.getKeyByValue(value: V): K? {
//    for ((key, entryValue) in this) {
//        if (entryValue == value) {
//            return key
//        }
//    }
//    return null
//}
