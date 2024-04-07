package dev.xixil.navigation.data.database.graph

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.xixil.navigation.data.database.graph.models.EdgeDbo
import dev.xixil.navigation.data.database.graph.models.VertexDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface GraphDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createVertex(vertex: VertexDbo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUndirectedEdge(edge: EdgeDbo)

    @Query("SELECT * FROM edges WHERE sourcevertexId=:sourceId")
    fun getEdges(sourceId: Long): Flow<List<EdgeDbo>>

    @Query("SELECT * FROM edges")
    fun getAllEdges(): Flow<List<EdgeDbo>>

    @Query("SELECT * FROM vertices WHERE vertexId=:vertexId")
    fun getVertex(vertexId: Long): Flow<VertexDbo>

    @Query("SELECT * FROM vertices")
    fun getVertices(): Flow<List<VertexDbo>>

    @Delete
    suspend fun removeEdges(source: VertexDbo)

    @Delete
    suspend fun removeVertex(vertex: VertexDbo)

    @Query("DELETE FROM edges")
    suspend fun removeAllEdges()

    @Query("DELETE FROM vertices")
    suspend fun removeAllVertices()
}