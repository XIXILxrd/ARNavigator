package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository

class GetVertexUseCase(private val graphRepository: GraphRepository) {
    suspend operator fun invoke(vertexId: Long) = graphRepository.getVertex(vertexId)
}