package dev.xixil.navigation.domain.usecases.recordUseCases

import dev.xixil.navigation.domain.RecordRepository

class DeleteRecordUseCase(private val recordRepository: RecordRepository) {
    suspend operator fun invoke(recordId: Long) = recordRepository.deleteRecord(recordId)
}