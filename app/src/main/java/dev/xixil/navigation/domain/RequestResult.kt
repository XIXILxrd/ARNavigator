package dev.xixil.navigation.domain

sealed class RequestResult<out T> {
    class Error <T : Any>(val error: Throwable? = null) : RequestResult<T>()
    class Loading<T : Any>: RequestResult<T>()
    data class Success <T : Any>(val data: T): RequestResult<T>()
}

fun <I : Any, O : Any> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Error -> RequestResult.Error(error)
        is RequestResult.Loading -> RequestResult.Loading()
        is RequestResult.Success -> RequestResult.Success(mapper(data))
    }
}

fun <T : Any> Result<T>.toRequestResult(): RequestResult<T> {
    return when {
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error()
        else -> {
            error("Impossible branch")
        }
    }
}