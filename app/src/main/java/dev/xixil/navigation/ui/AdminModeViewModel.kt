package dev.xixil.navigation.ui

import androidx.lifecycle.ViewModel
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.domain.usecases.graphUseCases.AddUndirectedEdgeUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.CreateVertexUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.GetGraphUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.RemoveEdgesUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.RemoveVertexUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Provider


class AdminModeViewModel(
    private val createVertexUseCase: Provider<CreateVertexUseCase>,
    private val addUndirectedEdgeUseCase: Provider<AddUndirectedEdgeUseCase>,
    private val removeEdgesUseCase: Provider<RemoveEdgesUseCase>,
    private val removeVertexUseCase: Provider<RemoveVertexUseCase>,
    private val getGraphUseCase: Provider<GetGraphUseCase>,
) : ViewModel() {

    fun getGraph(): Flow<Map<Vertex, List<Edge>>> {
        return getGraphUseCase.get().invoke()
    }
}