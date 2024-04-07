package dev.xixil.navigation.data.database.graph.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "edges")
data class EdgeDbo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("edgeId")
    val edgeId: Long,
    @Embedded(prefix = "source") val source: VertexDbo,
    @Embedded(prefix = "destination") val destination: VertexDbo,
    @ColumnInfo("weight") val weight: Long,
)