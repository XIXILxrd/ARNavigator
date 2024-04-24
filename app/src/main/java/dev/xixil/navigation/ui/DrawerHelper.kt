package dev.xixil.navigation.ui


import com.google.ar.core.Anchor
import dev.romainguy.kotlin.math.Float3
import dev.xixil.navigation.domain.models.Vertex
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.math.Scale
import io.github.sceneview.node.Node

class DrawerHelper {
    companion object {
        private val arrowScale = Scale(0.5f, 0.5f, 0.5f)
        private val pathScale = Scale(0.1f)
        private const val pathModel = "models/cylinder.glb"
        private const val selectionModel = "models/cone.glb"
        private const val arrowAnimationDelay = 2L
        private const val arrowAnimationPart = 15
        private const val bias = 0.15f
    }

    suspend fun drawVertex(
        vertex: Vertex,
        surfaceView: ARSceneView,
        anchor: Anchor? = null,
    ): Node {
        TODO()
    }

    suspend fun drawArNode(
        model: String,
        scale: Scale,
        position: Float3,
        surfaceView: ARSceneView,
        anchor: Anchor? = null,
    ): Node {


        TODO()
    }
}


