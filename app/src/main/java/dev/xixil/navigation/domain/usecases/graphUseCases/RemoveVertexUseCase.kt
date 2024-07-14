package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.models.Vertex
import javax.inject.Inject

class RemoveVertexUseCase @Inject constructor(private val graphRepository: GraphRepository) {
    suspend operator fun invoke(vertex: Vertex) = graphRepository.removeVertex(vertex)
}