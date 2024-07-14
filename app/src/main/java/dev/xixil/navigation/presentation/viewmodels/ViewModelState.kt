package dev.xixil.navigation.presentation.viewmodels

import dev.xixil.navigation.domain.RequestResult

sealed class ViewModelState<T> {
    class None<T>() : ViewModelState<T>()

    class Loading<T>() : ViewModelState<T>()

    class Error<T>(val error: Throwable? = null) : ViewModelState<T>()

    class Success<T: Any>(val data: T) : ViewModelState<T>()
}

fun <T: Any> RequestResult<T>.toState(): ViewModelState<T> {
    return when (this) {
        is RequestResult.Error -> ViewModelState.Error(error)
        is RequestResult.Loading -> ViewModelState.Loading()
        is RequestResult.Success -> ViewModelState.Success(data)
    }
}
