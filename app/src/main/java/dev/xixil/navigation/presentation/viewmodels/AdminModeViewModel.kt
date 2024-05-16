package dev.xixil.navigation.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.xixil.navigation.domain.RequestResult
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.domain.usecases.graphUseCases.AddUndirectedEdgeUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.CreateVertexUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.GetGraphUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.RemoveEdgeUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.RemoveEdgesUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.RemoveVertexUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class AdminModeViewModel @Inject constructor(
    private val createVertexUseCase: Provider<CreateVertexUseCase>,
    private val addUndirectedEdgeUseCase: Provider<AddUndirectedEdgeUseCase>,
    private val removeEdgesUseCase: Provider<RemoveEdgesUseCase>,
    private val removeEdgeUseCase: Provider<RemoveEdgeUseCase>,
    private val removeVertexUseCase: Provider<RemoveVertexUseCase>,
    private val getGraphUseCase: Provider<GetGraphUseCase>,
) : ViewModel() {

    val graphState: StateFlow<ViewModelState> =
        getGraphUseCase.get().invoke()
            .map(RequestResult<Map<Vertex, List<Edge>>>::toState)
            .stateIn(viewModelScope, SharingStarted.Lazily, ViewModelState.None)

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

    fun removeEdges(source: Vertex) {
        viewModelScope.launch {
            removeEdgesUseCase.get().invoke(source)
        }
    }

    fun removeEdge(edge: Edge) {
        viewModelScope.launch {
            removeEdgeUseCase.get().invoke(edge)
        }
    }


    fun removeVertex(vertex: Vertex) {
        viewModelScope.launch {
            removeVertexUseCase.get().invoke(vertex = vertex)
        }
    }
}