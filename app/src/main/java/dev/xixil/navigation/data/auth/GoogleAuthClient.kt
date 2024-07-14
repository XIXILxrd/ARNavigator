package dev.xixil.navigation.data.auth

import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dev.xixil.navigation.BuildConfig
import dev.xixil.navigation.domain.models.UserDbo
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@Suppress("DEPRECATION")
class GoogleAuthClient @Inject constructor(
    private val oneTapClient: SignInClient,
    private val firebaseAuth: FirebaseAuth,
) {

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): Result<UserDbo> {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = firebaseAuth.signInWithCredential(googleCredentials).await().user

            if (user != null) {
                Result.success(
                    UserDbo(
                        id = user.uid,
                        name = user.displayName,
                        profilePictureUrl = user.photoUrl.toString()
                    )
                )
            } else {
                Result.failure(Throwable("User is null"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Result.failure<UserDbo>(e)
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            firebaseAuth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): Result<UserDbo> {
        val request = firebaseAuth.currentUser

        return if (request != null) Result.success(
            UserDbo(
                id = request.uid,
                name = request.displayName,
                profilePictureUrl = request.photoUrl.toString()
            )
        )
        else Result.failure(Throwable("Nothing found"))
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .build()
            )
            .setAutoSelectEnabled(false)
            .build()
    }
}