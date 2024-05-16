package dev.xixil.navigation.presentation.viewmodels

import dev.xixil.navigation.domain.RequestResult
import dev.xixil.navigation.domain.models.Edge
import dev.xixil.navigation.domain.models.Vertex

sealed class ViewModelState {
    object None : ViewModelState()

    class Loading() : ViewModelState()

    class Error(val error: Throwable? = null) : ViewModelState()

    class Success(val graph: Map<Vertex, List<Edge>>) : ViewModelState()
}

fun RequestResult<Map<Vertex, List<Edge>>>.toState(): ViewModelState {
    return when (this) {
        is RequestResult.Error -> ViewModelState.Error(error)
        is RequestResult.Loading -> ViewModelState.Loading()
        is RequestResult.Success -> ViewModelState.Success(data)
    }
}