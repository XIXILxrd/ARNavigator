package dev.xixil.navigation.domain.usecases

import dev.xixil.navigation.domain.GraphRepository

class GetVertexUseCase(private val graphRepository: GraphRepository) {
    operator fun invoke(vertexId: Long) = graphRepository.getVertex(vertexId)
}