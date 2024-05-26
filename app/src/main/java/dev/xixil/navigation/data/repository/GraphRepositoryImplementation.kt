package dev.xixil.navigation.data.repository

import dev.xixil.navigation.data.database.graph.GraphFirebase
import dev.xixil.navigation.data.database.graph.models.EdgeDbo
import dev.xixil.navigation.data.database.graph.models.VertexDbo
import dev.xixil.navigation.data.utils.toEdge
import dev.xixil.navigation.data.utils.toEdgeDbo
import dev.xixil.navigation.data.utils.toVertex
import dev.xixil.navigation.data.utils.toVertexDbo
import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.RequestResult
import dev.xixil.navigation.domain.map
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.domain.toRequestResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GraphRepositoryImplementation @Inject constructor(
    private val graphFirebase: GraphFirebase,
) : GraphRepository {
    override suspend fun createVertex(vertex: Vertex) {
        graphFirebase.addVertex(
            vertex.toVertexDbo()
        )
    }

    override suspend fun addUndirectedEdge(edge: Edge) {
        graphFirebase.addEdge(edgeDbo = edge.toEdgeDbo())
    }

    override fun getEdges(source: Vertex): Flow<RequestResult<List<Edge>>> {
        val start = flowOf<RequestResult<List<EdgeDbo>>>(RequestResult.Loading())
        val request = graphFirebase.getEdges(source = source.toVertexDbo())
            .map { result -> result.toRequestResult() }

        return merge(start, request).map { result: RequestResult<List<EdgeDbo>> ->
            result.map { edgesDbo ->
                edgesDbo.map { edgeDbo ->
                    edgeDbo.toEdge()
                }
            }
        }
    }

    override suspend fun getVertex(name: String): RequestResult<Vertex> {
        return graphFirebase.getVertex(name).toRequestResult().map { it.toVertex() }
    }

    override fun getGraph(): Flow<RequestResult<Map<Vertex, List<Edge>>>> {
        //:)
        val start = flowOf<RequestResult<Map<Vertex, List<Edge>>>>(RequestResult.Loading())

        val allEdgesFlow = graphFirebase.getAllEdges().map { result -> result.toRequestResult() }
        val allVerticesFlow =
            graphFirebase.getAllVertices().map { result -> result.toRequestResult() }

        val result =
            allVerticesFlow.combineTransform(allEdgesFlow) { verticesResult: RequestResult<List<VertexDbo>>, edgesResult: RequestResult<List<EdgeDbo>> ->
                val map = mutableMapOf<Vertex, List<Edge>>()
                verticesResult.map { vertices ->
                    vertices.forEach {
                        map[it.toVertex()]
                    }
                }

                edgesResult.map { edges ->
                    edges.forEach { edge ->
                        map[edge.source.toVertex()] =
                            edges.filter { it.source == edge.source }.map { it.toEdge() }
                    }
                }

                if (map.isNotEmpty()) {
                    emit(RequestResult.Success(map.toMap()))
                } else {
                    emit(RequestResult.Error(Throwable("Graph is empty")))
                }
            }

        return merge(start, result)
    }

    override fun getAllAudiences(): Flow<RequestResult<List<Vertex>>> {
        val start = flowOf<RequestResult<List<VertexDbo>>>(RequestResult.Loading())

        val request = graphFirebase.getAllAudience().map { result ->
            result.toRequestResult()
        }

        return merge(start, request).map { requestResult ->
            requestResult.map { list ->
                list.map {
                    it.toVertex()
                }
            }
        }
    }

    override suspend fun removeEdge(edge: Edge) {
        graphFirebase.removeEdge(edge.toEdgeDbo())
    }

    override suspend fun removeEdges(source: Vertex) {
        graphFirebase.removeEdges(source = source.toVertexDbo())
    }

    override suspend fun removeVertex(vertex: Vertex) {
        graphFirebase.removeVertex(vertexDbo = vertex.toVertexDbo())
    }
}