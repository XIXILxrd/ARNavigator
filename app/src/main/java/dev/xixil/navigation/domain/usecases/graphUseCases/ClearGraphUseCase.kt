package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository
import javax.inject.Inject

class ClearGraphUseCase @Inject constructor(private val graphRepository: GraphRepository) {
    suspend operator fun invoke() = graphRepository.clear()
}