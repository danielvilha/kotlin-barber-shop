package com.danielvilha.barbershop.features.auth

import android.app.Activity
import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.danielvilha.barbershop.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import java.util.concurrent.CancellationException

sealed class GoogleSignInCredentialResult {
    data class Success(val credential: AuthCredential) : GoogleSignInCredentialResult()
    data class Error(val message: String) : GoogleSignInCredentialResult()
    data class Cancelled(val exception: CancellationException) : GoogleSignInCredentialResult()
}

class GoogleAuthUiClient(
    private val context: Context,
) {
    private val credentialManager = CredentialManager.create(context)
    suspend fun getGoogleSignInCredential(activity: Activity): GoogleSignInCredentialResult {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(context.getString(R.string.google_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                context = activity,
                request = request
            )

            handleCredential(result.credential)

        } catch (e: CancellationException) {
            GoogleSignInCredentialResult.Cancelled(e)
        } catch (e: Exception) {
            e.printStackTrace()
            GoogleSignInCredentialResult.Error(e.message ?: "Unexpected error getting Google credential.")
        }
    }

    private fun handleCredential(credential: Credential): GoogleSignInCredentialResult {
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val firebaseCredential = GoogleAuthProvider.getCredential(
                    googleIdTokenCredential.idToken,
                    null
                )
                GoogleSignInCredentialResult.Success(firebaseCredential)
            } catch (e: Exception) {
                GoogleSignInCredentialResult.Error("Failed to create Google Auth provider credential: ${e.message}")
            }
        }

        return GoogleSignInCredentialResult.Error("Unexpected credential type: ${credential.type}")
    }
}