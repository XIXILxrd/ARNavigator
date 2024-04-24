package dev.xixil.navigation.domain.usecases.recordUseCases

import dev.xixil.navigation.domain.RecordRepository
import javax.inject.Inject

class GetRecordsUseCase @Inject constructor(private val recordRepository: RecordRepository) {
    operator fun invoke() = recordRepository.getRecords()
}