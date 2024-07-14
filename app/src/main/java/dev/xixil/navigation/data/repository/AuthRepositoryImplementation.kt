package dev.xixil.navigation.data.repository

import android.content.Intent
import android.content.IntentSender
import dev.xixil.navigation.data.auth.GoogleAuthClient
import dev.xixil.navigation.data.utils.toUser
import dev.xixil.navigation.domain.AuthRepository
import dev.xixil.navigation.domain.RequestResult
import dev.xixil.navigation.domain.map
import dev.xixil.navigation.domain.models.User
import dev.xixil.navigation.domain.toRequestResult
import javax.inject.Inject

class AuthRepositoryImplementation @Inject constructor(
    private val googleAuthClient: GoogleAuthClient,
) : AuthRepository {
    override suspend fun signIn(): IntentSender? {
        return googleAuthClient.signIn()
    }

    override suspend fun signInWithIntent(intent: Intent): RequestResult<User> {
        return googleAuthClient.signInWithIntent(intent).toRequestResult().map { it.toUser() }
    }

    override suspend fun signOut() {
        googleAuthClient.signOut()
    }

    override suspend fun getSignedUser(): RequestResult<User> {
        return googleAuthClient.getSignedInUser().toRequestResult().map {
            it.toUser()
        }
    }
}