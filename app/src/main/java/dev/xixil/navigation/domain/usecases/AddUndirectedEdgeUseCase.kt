package dev.xixil.navigation.domain.usecases

import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.models.Vertex

class AddUndirectedEdgeUseCase<T>(private val graphRepository: GraphRepository<T>) {
    suspend operator fun invoke(source: Vertex<T>, destination: Vertex<T>, weight: Long) =
        graphRepository.addUndirectedEdge(source, destination, weight)
}