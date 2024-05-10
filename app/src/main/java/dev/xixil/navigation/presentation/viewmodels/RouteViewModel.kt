package dev.xixil.navigation.presentation.viewmodels

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.xixil.navigation.data.TextRecognitionAnalyzer
import dev.xixil.navigation.domain.models.Vertex
import dev.xixil.navigation.domain.pathfinding.Pathfinding
import dev.xixil.navigation.domain.usecases.graphUseCases.GetGraphUseCase
import dev.xixil.navigation.domain.usecases.graphUseCases.GetVertexUseCase
import dev.xixil.navigation.presentation.RecognitionTextState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class RouteViewModel @Inject constructor(
//    private val getVertexUseCase: Provider<GetVertexUseCase>,
//    private val getGraphUseCase: Provider<GetGraphUseCase>,
//    private val textRecognitionAnalyzer: TextRecognitionAnalyzer,
//    private val pathfinding: Provider<Pathfinding>,
) : ViewModel() {

//    private val _audienceNumber = MutableStateFlow<RecognitionTextState>(RecognitionTextState.None)
//    val audienceNumber: StateFlow<RecognitionTextState>
//        get() = _audienceNumber
//
//    suspend fun findPath(start: Vertex, destination: Vertex): List<Vertex> {
//        val graph = getGraphUseCase.get().invoke().last()
//
//        return pathfinding.get().findPath(start = start, finish = destination, graph)
//    }
}