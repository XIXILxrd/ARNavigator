package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.models.Vertex

class GetEdgesUseCase(private val graphRepository: GraphRepository) {
    operator fun invoke(source: Vertex) = graphRepository.getEdges(source)
}