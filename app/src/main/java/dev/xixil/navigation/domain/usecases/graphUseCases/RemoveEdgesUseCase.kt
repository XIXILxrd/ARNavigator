package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.models.Vertex
import javax.inject.Inject

class RemoveEdgesUseCase @Inject constructor(private val graphRepository: GraphRepository) {
    suspend operator fun invoke(source: Vertex) = graphRepository.removeEdges(source)
}