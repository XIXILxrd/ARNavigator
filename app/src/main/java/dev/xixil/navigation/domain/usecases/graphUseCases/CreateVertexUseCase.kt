package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.models.Vertex

class CreateVertexUseCase(private val graphRepository: GraphRepository) {
    suspend operator fun invoke(vertex: Vertex) = graphRepository.createVertex(vertex)

}