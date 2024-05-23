package dev.xixil.navigation.domain

import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import kotlinx.coroutines.flow.Flow

interface GraphRepository {
    suspend fun createVertex(vertex: Vertex)

    suspend fun addUndirectedEdge(edge: Edge)

    fun getEdges(source: Vertex): Flow<RequestResult<List<Edge>>>

//    suspend fun getVertex(vertexId: Long): Vertex

    fun getGraph(): Flow<RequestResult<Map<Vertex, List<Edge>>>>

    fun getAllAudiences(): Flow<RequestResult<List<Vertex>>>

    suspend fun removeEdge(edge: Edge)

    suspend fun removeEdges(source: Vertex)

    suspend fun removeVertex(vertex: Vertex)
}