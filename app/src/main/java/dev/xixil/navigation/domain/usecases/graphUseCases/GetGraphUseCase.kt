package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository
import javax.inject.Inject

class GetGraphUseCase @Inject constructor(private val graphRepository: GraphRepository) {
    operator fun invoke() = graphRepository.getGraph()
}