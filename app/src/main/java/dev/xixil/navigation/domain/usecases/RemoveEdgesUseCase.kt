package dev.xixil.navigation.domain.usecases

import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.models.Vertex

class RemoveEdgesUseCase<T>(private val graphRepository: GraphRepository<T>) {
    suspend operator fun invoke(source: Vertex<T>) = graphRepository.removeEdges(source)
}