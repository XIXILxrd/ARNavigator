package dev.xixil.navigation.domain.usecases.recordUseCases

import dev.xixil.navigation.domain.RecordRepository
import javax.inject.Inject

class ClearRecordsUseCase @Inject constructor(private val recordRepository: RecordRepository) {
    suspend operator fun invoke() = recordRepository.clearRecords()
}