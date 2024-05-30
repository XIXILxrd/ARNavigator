package dev.xixil.navigation.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.xixil.navigation.data.auth.GoogleAuthClient
import dev.xixil.navigation.data.repository.AuthRepositoryImplementation
import dev.xixil.navigation.domain.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {
    @Binds
    fun bindAuthRepository(authRepository: AuthRepositoryImplementation): AuthRepository


    companion object {
        @Provides
        fun providesSingInClient(@ApplicationContext applicationContext: Context)  = Identity.getSignInClient(applicationContext)
        @Provides
        @Singleton
        fun providesFirebaseAuth() = FirebaseAuth.getInstance()

        @Provides
        @Singleton
        fun providesGoogleAuthClient(oneTapClient: SignInClient, firebaseAuth: FirebaseAuth) =
            GoogleAuthClient(oneTapClient, firebaseAuth)
    }
}