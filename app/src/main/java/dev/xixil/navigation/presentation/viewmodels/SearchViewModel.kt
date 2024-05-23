package dev.xixil.navigation.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.xixil.navigation.domain.models.Record
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.domain.usecases.graphUseCases.GetAllAudiencesUseCase
import dev.xixil.navigation.domain.usecases.recordUseCases.GetRecordsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

class SearchViewModel @Inject constructor(
    private val getAllAudiencesUseCase: Provider<GetAllAudiencesUseCase>,
    private val getRecordsUseCase: Provider<GetRecordsUseCase>,
) : ViewModel() {
    val audiencesState: StateFlow<ViewModelState<List<Vertex>>> =
        getAllAudiencesUseCase.get().invoke()
            .map { result -> result.toState() }
            .stateIn(viewModelScope, SharingStarted.Lazily, ViewModelState.None())

    val recordsState: StateFlow<ViewModelState<List<Record>>> = getRecordsUseCase.get().invoke()
        .map { result -> ViewModelState.Success(result) }
        .stateIn(viewModelScope, SharingStarted.Lazily, ViewModelState.None())
}