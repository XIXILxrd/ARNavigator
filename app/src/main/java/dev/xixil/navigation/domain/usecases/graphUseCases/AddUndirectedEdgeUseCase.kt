package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.models.Edge
import javax.inject.Inject

class AddUndirectedEdgeUseCase @Inject constructor(private val graphRepository: GraphRepository) {
    suspend operator fun invoke(edge: Edge) = graphRepository.addUndirectedEdge(edge)
}