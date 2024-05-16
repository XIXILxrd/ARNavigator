package dev.xixil.navigation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.xixil.navigation.data.repository.GraphRepositoryImplementation
import dev.xixil.navigation.domain.GraphRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindGraphRepository(graphRepository: GraphRepositoryImplementation): GraphRepository

    companion object {
//        @Provides
//        fun providePathfindingAStar(): Pathfinding {
//            return AStar()
//        }
    }
}