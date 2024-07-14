package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository
import javax.inject.Inject

class GetVertexUseCase @Inject constructor(private val graphRepository: GraphRepository) {
    suspend operator fun invoke(name: String) = graphRepository.getVertex(name)
}