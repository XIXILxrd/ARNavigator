package dev.xixil.navigation.domain

import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import kotlinx.coroutines.flow.Flow

interface GraphRepository<T> {
    suspend fun createVertex(vertex: Vertex<T>)

    suspend fun addUndirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Long)

    fun getEdges(source: Vertex<T>): Flow<List<Edge<T>>>

    fun getVertex(vertexId: Int): Flow<Vertex<T>>

    suspend fun removeEdges(source: Vertex<T>)

    suspend fun removeVertex(vertex: Vertex<T>)
}