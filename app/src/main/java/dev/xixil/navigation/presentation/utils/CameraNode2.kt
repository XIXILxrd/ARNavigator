package dev.xixil.navigation.presentation.utils

import androidx.compose.runtime.Composable
import com.google.android.filament.Engine
import io.github.sceneview.ar.node.ARCameraNode
import io.github.sceneview.collision.Vector3
import io.github.sceneview.math.Transform
import io.github.sceneview.math.toFloat3
import io.github.sceneview.rememberNode
import io.github.sceneview.safeDestroyCamera
import io.github.sceneview.safeDestroyEntity
import io.github.sceneview.safeDestroyTransformable

class CameraNode2(engine: Engine) : ARCameraNode(engine = engine) {

    init {
        transform = Transform(position = Vector3.back().toFloat3())
        setExposure(
            aperture = 16.0f,
            sensitivity = 100.0f,
            shutterSpeed = 1.0f / 125.0f
        )
    }

    override fun destroy() {
        engine.safeDestroyCamera(camera)

        runCatching { parent = null }
        engine.safeDestroyTransformable(entity)
        engine.safeDestroyEntity(entity)
    }
}

@Composable
fun rememberCameraNode2(engine: Engine): ARCameraNode = rememberNode {
    CameraNode2(engine)
}