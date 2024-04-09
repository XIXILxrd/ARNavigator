package dev.xixil.navigation.domain.usecases.recordUseCases

import dev.xixil.navigation.domain.RecordRepository

class GetRecordsUseCase(private val recordRepository: RecordRepository) {
    operator fun invoke() = recordRepository.getRecords()
}