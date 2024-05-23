package dev.xixil.navigation.domain.usecases.graphUseCases

import dev.xixil.navigation.domain.GraphRepository
import javax.inject.Inject

class GetAllAudiencesUseCase @Inject constructor(private val repository: GraphRepository) {
    operator fun invoke() = repository.getAllAudiences()
}