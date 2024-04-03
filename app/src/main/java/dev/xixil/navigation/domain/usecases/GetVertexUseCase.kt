package dev.xixil.navigation.domain.usecases

import dev.xixil.navigation.domain.GraphRepository

class GetVertexUseCase<T>(private val graphRepository: GraphRepository<T>) {
    operator fun invoke(vertexId: Int) = graphRepository.getVertex(vertexId)
}