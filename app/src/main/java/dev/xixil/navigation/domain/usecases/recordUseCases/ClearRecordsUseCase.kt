package dev.xixil.navigation.domain.usecases.recordUseCases

import dev.xixil.navigation.domain.RecordRepository

class ClearRecordsUseCase(private val recordRepository: RecordRepository) {
    suspend operator fun invoke() = recordRepository.clearRecords()
}