package dev.xixil.navigation.data.database.graph

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.xixil.navigation.data.database.graph.models.EdgeDbo
import dev.xixil.navigation.data.database.graph.models.VertexDbo
import javax.inject.Inject

@Database(entities = [EdgeDbo::class, VertexDbo::class], version = 1, exportSchema = true)
abstract class GraphRoomDatabase : RoomDatabase() {
    abstract fun graphDao(): GraphDao
}

class GraphDatabase(private val database: GraphRoomDatabase) {
    val graph: GraphDao
        get() = database.graphDao()
}

fun GraphDatabase(applicationContext: Context): GraphDatabase {
    val roomDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        GraphRoomDatabase::class.java,
        "graph"
    ).build()

    return GraphDatabase(roomDatabase)
}