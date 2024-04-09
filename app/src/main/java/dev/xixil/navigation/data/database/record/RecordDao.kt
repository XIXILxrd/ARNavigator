package dev.xixil.navigation.data.database.record

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.xixil.navigation.data.database.record.models.RecordDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecord(record: RecordDbo)

    @Query("SELECT * FROM records WHERE id=:recordId")
    fun getRecord(recordId: Long): RecordDbo

    @Query("SELECT * FROM records LIMIT 15")
    fun getRecords(): Flow<List<RecordDbo>>

    @Query("DELETE FROM records WHERE id=:recordId")
    suspend fun deleteRecord(recordId: Long)

    @Query("DELETE FROM records")
    suspend fun clearRecords()
}