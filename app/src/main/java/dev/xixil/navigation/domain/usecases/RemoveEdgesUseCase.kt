package dev.xixil.navigation.domain.usecases

import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.models.Vertex

class RemoveEdgesUseCase(private val graphRepository: GraphRepository) {
    suspend operator fun invoke(source: Vertex) = graphRepository.removeEdges(source)
}