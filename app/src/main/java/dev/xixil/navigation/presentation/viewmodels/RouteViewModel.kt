package dev.xixil.navigation.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.domain.pathfinding.Pathfinding
import dev.xixil.navigation.domain.usecases.graphUseCases.GetGraphUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.GetVertexUseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class RouteViewModel @Inject constructor(
    private val getVertexUseCase: Provider<GetVertexUseCase>,
    private val getGraphUseCase: Provider<GetGraphUseCase>,
    private val pathfinding: Provider<Pathfinding>,
) : ViewModel() {

    val graph: StateFlow<ViewModelState<Map<Vertex, List<Edge>>>> = getGraphUseCase.get().invoke()
        .map { request -> request.toState()}
        .stateIn(viewModelScope, SharingStarted.Lazily, ViewModelState.None())

    fun getVertex(data: String): Deferred<ViewModelState<Vertex>> = viewModelScope.async {
        getVertexUseCase.get().invoke(data).toState()
    }


    fun findPath(start: Vertex, destination: Vertex, graph: Map<Vertex, List<Edge>>): List<Vertex> {
        return pathfinding.get().findPath(start = start, finish = destination, graph)
    }
}