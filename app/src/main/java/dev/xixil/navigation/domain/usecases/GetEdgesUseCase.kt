package dev.xixil.navigation.domain.usecases

import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.models.Vertex

class GetEdgesUseCase<T>(private val graphRepository: GraphRepository<T>) {
    operator fun invoke(source: Vertex<T>) = graphRepository.getEdges(source)
}