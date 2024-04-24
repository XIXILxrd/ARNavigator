package dev.xixil.navigation.data.repository

import dev.xixil.navigation.data.database.graph.GraphDatabase
import dev.xixil.navigation.data.database.graph.models.EdgeDbo
import dev.xixil.navigation.data.utils.toEdge
import dev.xixil.navigation.data.utils.toEdgeDto
import dev.xixil.navigation.data.utils.toVertex
import dev.xixil.navigation.data.utils.toVertexDbo
import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class GraphRepositoryImplementation(
    private val graphDatabase: GraphDatabase,
) : GraphRepository {
    override suspend fun createVertex(vertex: Vertex): Vertex {
        withContext(Dispatchers.IO) {
            graphDatabase.graph.createVertex(vertex.toVertexDbo())
        }

        return vertex
    }

    override suspend fun addUndirectedEdge(edge: Edge) {
        withContext(Dispatchers.IO) {
            graphDatabase.graph.addUndirectedEdge(edge.toEdgeDto())
            graphDatabase.graph.addUndirectedEdge(
                EdgeDbo(
                    edgeId = Random.nextLong(),
                    source = edge.destination.toVertexDbo(),
                    destination = edge.source.toVertexDbo(),
                    weight = edge.weight
                )
            )
        }
    }

    override fun getEdges(source: Vertex): Flow<List<Edge>> {
        return graphDatabase.graph.getEdges(sourceId = source.id).map {
            it.map { edgeDbo ->
                edgeDbo.toEdge()
            }
        }
    }

    override suspend fun getVertex(vertexId: Long): Vertex {
        return withContext(Dispatchers.IO) {
            graphDatabase.graph.getVertex(vertexId).toVertex()
        }
    }

    override fun getGraph(): Flow<Map<Vertex, List<Edge>>> {
        val graph: MutableMap<Vertex, List<Edge>> = mutableMapOf()

        return flow {
            graphDatabase.graph.getAllEdges().collect { edges ->
                edges.forEach { edge ->
                    val source = edge.source.toVertex()

                    if (!graph.containsKey(source)) {
                        graph[source] =
                            edges.filter { it.source == source.toVertexDbo() }
                                .map { edgeDbo -> edgeDbo.toEdge() }
                    }
                }

                emit(graph.toMap())
            }
        }
    }

    override suspend fun removeEdges(source: Vertex) {
        withContext(Dispatchers.IO) {
            graphDatabase.graph.removeEdges(source = source.toVertexDbo())
        }
    }

    override suspend fun removeVertex(vertex: Vertex) {
        withContext(Dispatchers.IO) {
            graphDatabase.graph.removeVertex(vertex = vertex.toVertexDbo())
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            graphDatabase.graph.removeAllEdges()
            graphDatabase.graph.removeAllVertices()
        }
    }
}