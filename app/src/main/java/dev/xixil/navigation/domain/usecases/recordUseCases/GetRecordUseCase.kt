package dev.xixil.navigation.domain.usecases.recordUseCases

import dev.xixil.navigation.domain.RecordRepository

class GetRecordUseCase(private val recordRepository: RecordRepository) {
    suspend operator fun invoke(recordId: Long) = recordRepository.getRecord(recordId)
}