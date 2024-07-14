package dev.xixil.navigation.presentation.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.xixil.navigation.domain.AuthRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    val intent = viewModelScope.async {
        authRepository.signIn()
    }

    fun singIn(intent: Intent) = viewModelScope.launch {
        authRepository.signInWithIntent(intent).toState()
    }

    fun getSignedUser() = viewModelScope.async {
        authRepository.getSignedUser().toState()
    }

    fun signOut() = viewModelScope.launch { authRepository.signOut() }
}