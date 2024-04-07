package dev.xixil.navigation.domain

import dev.xixil.navigation.domain.models.Record
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    suspend fun addRecord(record: Record)

    fun getRecord(recordId: Long): Record

    fun getRecords(): Flow<List<Record>>

    suspend fun clearRecords()
}