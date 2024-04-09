package dev.xixil.navigation.domain.usecases.recordUseCases

import dev.xixil.navigation.domain.RecordRepository
import dev.xixil.navigation.domain.models.Record

class AddRecordUseCase(private val recordRepository: RecordRepository) {
    suspend operator fun invoke(record: Record) = recordRepository.addRecord(record)
}