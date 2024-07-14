package dev.xixil.navigation.data.database.record

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.xixil.navigation.data.database.record.models.RecordDbo
import javax.inject.Inject

@Database(entities = [RecordDbo::class], version = 1, exportSchema = false)
abstract class RecordsRoomDatabase: RoomDatabase() {
    abstract fun recordDao(): RecordDao
}

class RecordsDatabase @Inject constructor(private val database: RecordsRoomDatabase) {
    val records: RecordDao
        get() = database.recordDao()
}

fun RecordsDatabase(applicationContext: Context): RecordsDatabase {
    val roomDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        RecordsRoomDatabase::class.java,
        "records"
    ).build()

    return RecordsDatabase(roomDatabase)
}
