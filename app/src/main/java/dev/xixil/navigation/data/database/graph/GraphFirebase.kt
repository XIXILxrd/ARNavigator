package dev.xixil.navigation.data.database.graph

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import dev.xixil.navigation.data.database.graph.models.EdgeDbo
import dev.xixil.navigation.data.database.graph.models.VertexDbo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.random.Random

class GraphFirebase @Inject constructor() {
    private val edgesCollectionRef = Firebase.firestore.collection(EDGE_COLLECTION_PATH)
    private val verticesCollectionRef = Firebase.firestore.collection(VERTEX_COLLECTION_PATH)

    fun addVertex(vertexDbo: VertexDbo) = CoroutineScope(Dispatchers.IO).launch {
        try {
            verticesCollectionRef.add(vertexDbo).await()
        } catch (e: Exception) {
            Log.w(TAG, "${e.message}")
        }
    }

    fun addEdge(edgeDbo: EdgeDbo) = CoroutineScope(Dispatchers.IO).launch {
        try {
            edgesCollectionRef.add(edgeDbo).await()
            edgesCollectionRef.add(
                edgeDbo.copy(
                    edgeId = Random.nextLong(),
                    source = edgeDbo.destination,
                    destination = edgeDbo.source
                )
            ).await()
        } catch (e: Exception) {
            Log.w(TAG, "${e.message}")
        }
    }

    fun getEdges(source: VertexDbo) = flow {
        try {
            val result = mutableListOf<EdgeDbo>()

            val querySnapshot = edgesCollectionRef.whereEqualTo("source", source).get().await()
            for (document in querySnapshot.documents) {
                document.toObject<EdgeDbo>()?.let {
                    result.add(it)
                }
            }
            emit(Result.success(result.toList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getAllEdges() = flow {
        val result = mutableListOf<EdgeDbo>()

        val querySnapshot = edgesCollectionRef.get().await()

        try {
            for (document in querySnapshot.documents) {
                document.toObject<EdgeDbo>()?.let {
                    result.add(it)
                }
            }
            emit(Result.success(result.toList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getVertex(data: String) = flow {
        val querySnapshot = verticesCollectionRef.whereEqualTo("data", data).limit(1).get().await()

        if (querySnapshot.documents.isNotEmpty()) {
            try {
                querySnapshot.documents.last().toObject<VertexDbo>()?.let {
                    emit(Result.success(it))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

    fun removeVertex(vertexDbo: VertexDbo) = CoroutineScope(Dispatchers.IO).launch {
        val querySnapshot =
            verticesCollectionRef.whereEqualTo("vertexId", vertexDbo.vertexId).get().await()

        val deleteEdgeBySourceQuery =
            edgesCollectionRef.whereEqualTo("source", vertexDbo).limit(1).get().await()
        val deleteEdgeByDestinationQuery =
            edgesCollectionRef.whereEqualTo("destination", vertexDbo).limit(1).get()
                .await()


        if (querySnapshot.documents.isNotEmpty() || (deleteEdgeBySourceQuery.documents.isNotEmpty() && deleteEdgeByDestinationQuery.documents.isNotEmpty())) {
            try {
                verticesCollectionRef.document(querySnapshot.documents.first().id).delete().await()

                edgesCollectionRef.document(deleteEdgeBySourceQuery.documents.first().id).delete()
                    .await()

                edgesCollectionRef.document(deleteEdgeByDestinationQuery.documents.first().id)
                    .delete()
                    .await()
            } catch (e: Exception) {
                Log.w(TAG, "${e.message}")
            }
        }
    }

    fun removeEdges(source: VertexDbo) = CoroutineScope(Dispatchers.IO).launch {
        val deleteSourceQuery = edgesCollectionRef.whereEqualTo("source", source).get().await()
        val deleteDestinationQuery =
            edgesCollectionRef.whereEqualTo("destination", source).get().await()

        if (deleteSourceQuery.documents.isNotEmpty() && deleteDestinationQuery.documents.isNotEmpty()) {
            for (document in deleteSourceQuery.documents) {
                try {
                    edgesCollectionRef.document(document.id).delete().await()
                } catch (e: Exception) {
                    Log.w(TAG, "${e.message}")
                }
            }

            for (document in deleteDestinationQuery.documents) {
                try {
                    edgesCollectionRef.document(document.id).delete().await()
                } catch (e: Exception) {
                    Log.w(TAG, "${e.message}")
                }
            }
        }
    }

    fun getAllVertices() = flow {
        val result = mutableListOf<VertexDbo>()

        val querySnapshot = verticesCollectionRef.get().await()
        try {
            for (document in querySnapshot.documents) {
                document.toObject<VertexDbo>()?.let {
                    result.add(it)
                }
            }
            emit(Result.success(result.toList()))
        } catch (e: Exception) {
            emit(Result.failure(e))
            Log.w(TAG, "${e.message}")
        }
    }

    fun removeEdge(edgeDbo: EdgeDbo) = CoroutineScope(Dispatchers.IO).launch {
        val deleteEdgeBySourceQuery =
            edgesCollectionRef.whereEqualTo("source", edgeDbo.source).limit(1).get().await()
        val deleteEdgeByDestinationQuery =
            edgesCollectionRef.whereEqualTo("destination", edgeDbo.source).limit(1).get()
                .await()

        if (deleteEdgeBySourceQuery.documents.isNotEmpty() && deleteEdgeByDestinationQuery.documents.isNotEmpty()) {
            try {
                edgesCollectionRef.document(deleteEdgeBySourceQuery.documents.first().id).delete()
                    .await()

                edgesCollectionRef.document(deleteEdgeByDestinationQuery.documents.first().id)
                    .delete()
                    .await()
            } catch (e: Exception) {
                Log.w(TAG, "${e.message}")
            }
        }
    }

    companion object {
        private const val EDGE_COLLECTION_PATH = "edges"
        private const val VERTEX_COLLECTION_PATH = "vertices"

        private const val TAG = "GraphFirebase"
    }
}