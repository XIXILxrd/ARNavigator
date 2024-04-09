package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.models.Edge

class AddUndirectedEdgeUseCase(private val graphRepository: GraphRepository) {
    suspend operator fun invoke(edge: Edge) = graphRepository.addUndirectedEdge(edge)
}