package dev.xixil.navigation.domain.usecases

import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.models.Vertex

class CreateVertexUseCase<T>(private val graphRepository: GraphRepository<T>) {
    suspend operator fun invoke(vertex: Vertex<T>) = graphRepository.createVertex(vertex)

}