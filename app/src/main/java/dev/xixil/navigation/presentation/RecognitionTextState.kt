package dev.xixil.navigation.presentation

sealed class RecognitionTextState {
    data class Correct(val audienceNumber: String): RecognitionTextState()
    data object Incorrect: RecognitionTextState()
    data object None: RecognitionTextState()
}