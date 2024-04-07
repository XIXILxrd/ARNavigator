package dev.xixil.navigation.domain

import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import kotlinx.coroutines.flow.Flow

interface GraphRepository {
    suspend fun createVertex(vertex: Vertex): Vertex

    suspend fun addUndirectedEdge(edge: Edge)

    fun getEdges(source: Vertex): Flow<List<Edge>>

    fun getVertex(vertexId: Long): Flow<Vertex>

    fun getGraph(): Flow<Map<Vertex, List<Edge>>>

    suspend fun removeEdges(source: Vertex)

    suspend fun removeVertex(vertex: Vertex)

    suspend fun clear()
}