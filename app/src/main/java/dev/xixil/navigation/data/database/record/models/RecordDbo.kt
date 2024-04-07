package dev.xixil.navigation.data.database.record.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records")
data class RecordDbo(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo("start") val start: String,
    @ColumnInfo("end") val end: String,
    @ColumnInfo("time") val time: Long,
)