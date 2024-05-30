package dev.xixil.navigation.domain

import android.content.Intent
import android.content.IntentSender
import dev.xixil.navigation.domain.models.User

interface AuthRepository {

    suspend fun signIn(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): RequestResult<User>

    suspend fun signOut()

    suspend fun getSignedUser(): RequestResult<User>
}