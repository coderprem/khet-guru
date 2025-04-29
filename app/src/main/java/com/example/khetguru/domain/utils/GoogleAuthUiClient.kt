package com.example.khetguru.domain.utils

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.example.khetguru.R
import com.example.khetguru.domain.model.SignInResult
import com.example.khetguru.domain.model.UserData
import com.example.khetguru.presentation.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.internal.Constants
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val viewModel: AuthViewModel
) {
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(CLIENT_ID)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        viewModel.resetState()
        return try {
            val cred = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleIdToken = cred.googleIdToken
            if (googleIdToken == null) {
                Log.e("GoogleAuthUiClient", "Google ID token is null")
                return SignInResult(
                    errorMessage = "Google ID token is null",
                    data = null
                )
            }
            Log.d("GoogleAuthUiClient", "Google ID token: $googleIdToken")

            val googleCred = GoogleAuthProvider.getCredential(googleIdToken, null)
            val authResult = auth.signInWithCredential(googleCred).await()
            val user = authResult.user

            if (user == null) {
                Log.e("GoogleAuthUiClient", "User is null after sign-in")
                return SignInResult(
                    errorMessage = "User is null after sign-in",
                    data = null
                )
            }

            Log.d("GoogleAuthUiClient", "User signed in: ${user.uid}, ${user.email}")

            SignInResult(
                errorMessage = null,
                data = user.run {
                    UserData(
                        userId = uid,
                        userName = displayName ?: "",
                        profilePicUrl = photoUrl?.toString() ?: "",
                        email = email ?: ""
                    )
                }
            )
        } catch (e: Exception) {
            Log.e("GoogleAuthUiClient", "Error during sign-in: ${e.message}", e)
            if (e is CancellationException) throw e
            SignInResult(
                errorMessage = e.message,
                data = null
            )
        }
    }
    fun getSignedInUserData() : UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            userName = displayName.toString(),
            profilePicUrl = photoUrl?.toString() ?: "",
            email = email.toString()
        )
    }
}