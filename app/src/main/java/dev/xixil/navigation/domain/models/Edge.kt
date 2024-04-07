package dev.xixil.navigation.domain.models


data class Edge(
    val id: Long,
    val source: Vertex,
    val destination: Vertex,
    val weight: Long,
) {
    override fun toString(): String {
        return "$destination"
    }
}