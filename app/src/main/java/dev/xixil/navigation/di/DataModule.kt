package dev.xixil.navigation.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.xixil.navigation.data.database.record.RecordsDatabase
import dev.xixil.navigation.data.repository.GraphRepositoryImplementation
import dev.xixil.navigation.data.repository.RecordRepositoryImplementation
import dev.xixil.navigation.domain.GraphRepository
import dev.xixil.navigation.domain.RecordRepository
import dev.xixil.navigation.domain.pathfinding.AStar
import dev.xixil.navigation.domain.pathfinding.Pathfinding
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindGraphRepository(graphRepository: GraphRepositoryImplementation): GraphRepository

    @Binds
    fun bindRecordRepository(recordRepository: RecordRepositoryImplementation): RecordRepository

    companion object {
        @Singleton
        @Provides
        fun providePathfindingAStar(): Pathfinding {
            return AStar()
        }

        @Singleton
        @Provides
        fun provideRecordDatabase(@ApplicationContext context: Context): RecordsDatabase {
            return RecordsDatabase(context)
        }
    }
}