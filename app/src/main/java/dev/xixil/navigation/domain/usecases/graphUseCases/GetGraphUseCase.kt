package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository

class GetGraphUseCase(private val graphRepository: GraphRepository) {
    operator fun invoke() = graphRepository.getGraph()
}