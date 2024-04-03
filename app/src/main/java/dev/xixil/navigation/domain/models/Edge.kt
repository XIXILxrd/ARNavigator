package dev.xixil.navigation.domain.models


//ребро
data class Edge<T>(
    val id: Long,
    val source: Vertex<T>,
    val destination: Vertex<T>,
    val weight: Long,
)