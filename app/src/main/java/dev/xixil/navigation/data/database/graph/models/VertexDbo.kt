package dev.xixil.navigation.data.database.graph.models

data class VertexDbo(
    val vertexId: Long = ILLEGAL_ID,
    val data: String? = null,
    val cloudAnchorId: String = ILLEGAL_STRING,
    val x: Float = ILLEGAL_COORDINATE,
    val y: Float = ILLEGAL_COORDINATE,
    val z: Float = ILLEGAL_COORDINATE
) {
    companion object {
        private const val ILLEGAL_ID = 0L
        private const val ILLEGAL_STRING = ""
        private const val ILLEGAL_COORDINATE = Float.NEGATIVE_INFINITY
    }
}