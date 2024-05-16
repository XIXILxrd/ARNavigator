package dev.xixil.navigation.data.database.graph.models

data class EdgeDbo(
    val edgeId: Long = ILLEGAL_ID,
    val source: VertexDbo = ILLEGAL_VERTEX,
    val destination: VertexDbo = ILLEGAL_VERTEX,
    val cloudAnchorId: String = ILLEGAL_STRING,
    val weight: Long = ILLEGAL_WEIGHT,
) {
     companion object {
         private const val ILLEGAL_ID = 0L
         private const val ILLEGAL_STRING = ""
         private const val ILLEGAL_WEIGHT = Long.MAX_VALUE
         private val ILLEGAL_VERTEX = VertexDbo()
     }
}