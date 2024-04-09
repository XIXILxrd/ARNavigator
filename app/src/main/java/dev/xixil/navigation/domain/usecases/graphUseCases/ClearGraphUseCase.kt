package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository

class ClearGraphUseCase(private val graphRepository: GraphRepository) {
    suspend operator fun invoke() = graphRepository.clear()
}