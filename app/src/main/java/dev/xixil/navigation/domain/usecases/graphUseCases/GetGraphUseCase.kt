package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository
import javax.inject.Inject

class GetGraphUseCase(private val graphRepository: GraphRepository) {
    operator fun invoke() = graphRepository.getGraph()
}