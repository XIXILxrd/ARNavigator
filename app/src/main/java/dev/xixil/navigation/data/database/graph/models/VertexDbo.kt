package dev.xixil.navigation.data.database.graph.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vertices")
data class VertexDbo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("vertexId")
    val vertexId: Long,
    @ColumnInfo("data") val data: String? = null,
    @ColumnInfo("coordinateX") val coordinateX: Float,
    @ColumnInfo("coordinateY") val coordinateY: Float,
    @ColumnInfo("coordinateZ") val coordinateZ: Float,
)