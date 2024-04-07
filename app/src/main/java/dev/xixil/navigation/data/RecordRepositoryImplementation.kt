package dev.xixil.navigation.data

import dev.xixil.navigation.data.database.record.RecordsDatabase
import dev.xixil.navigation.domain.RecordRepository
import dev.xixil.navigation.domain.models.Record
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordRepositoryImplementation @Inject constructor(
    private val recordsDatabase: RecordsDatabase,
) : RecordRepository {
    override suspend fun addRecord(record: Record) {
        TODO("Not yet implemented")
    }

    override fun getRecord(recordId: Long): Record {
        TODO("Not yet implemented")
    }

    override fun getRecords(): Flow<List<Record>> {
        TODO("Not yet implemented")
    }

    override suspend fun clearRecords() {
        TODO("Not yet implemented")
    }
}