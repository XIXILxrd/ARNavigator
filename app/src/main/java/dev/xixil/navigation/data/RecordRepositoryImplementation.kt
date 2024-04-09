package dev.xixil.navigation.data

import dev.xixil.navigation.data.database.record.RecordsDatabase
import dev.xixil.navigation.data.utils.toRecord
import dev.xixil.navigation.data.utils.toRecordDbo
import dev.xixil.navigation.domain.RecordRepository
import dev.xixil.navigation.domain.models.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecordRepositoryImplementation @Inject constructor(
    private val recordsDatabase: RecordsDatabase,
) : RecordRepository {
    override suspend fun addRecord(record: Record) {
        withContext(Dispatchers.IO) {
            recordsDatabase.records.addRecord(record.toRecordDbo())
        }
    }

    override suspend fun getRecord(recordId: Long): Record {
        return withContext(Dispatchers.IO) {
            recordsDatabase.records.getRecord(recordId).toRecord()
        }
    }

    override fun getRecords(): Flow<List<Record>> {
        return recordsDatabase.records.getRecords().map { records ->
            records.map {
                it.toRecord()
            }
        }
    }

    override suspend fun deleteRecord(recordId: Long) {
        withContext(Dispatchers.IO) {
            recordsDatabase.records.deleteRecord(recordId)
        }
    }

    override suspend fun clearRecords() {
        withContext(Dispatchers.IO) {
            recordsDatabase.records.clearRecords()
        }
    }
}