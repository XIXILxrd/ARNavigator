package dev.xixil.navigation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.domain.usecases.graphUseCases.AddUndirectedEdgeUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.CreateVertexUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.GetGraphUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.RemoveEdgesUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.RemoveVertexUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class AdminModeViewModel @Inject constructor(
    private val createVertexUseCase: Provider<CreateVertexUseCase>,
    private val addUndirectedEdgeUseCase: Provider<AddUndirectedEdgeUseCase>,
    private val removeEdgesUseCase: Provider<RemoveEdgesUseCase>,
    private val removeVertexUseCase: Provider<RemoveVertexUseCase>,
    private val getGraphUseCase: Provider<GetGraphUseCase>,
) : ViewModel() {

    fun getGraph(): Flow<Map<Vertex, List<Edge>>> {
        return getGraphUseCase.get().invoke()
    }

    fun createVertex(vertex: Vertex) {
        viewModelScope.launch {
            createVertexUseCase.get().invoke(vertex)
        }
    }

    fun createEdge(edge: Edge) {
        viewModelScope.launch {
            addUndirectedEdgeUseCase.get().invoke(edge)
        }
    }

    fun removeEdge(source: Vertex) {
        viewModelScope.launch {
            removeEdgesUseCase.get().invoke(source)
        }
    }

    fun removeVertex(vertex: Vertex) {
        viewModelScope.launch {
            removeVertexUseCase.get().invoke(vertex = vertex)
        }
    }
}