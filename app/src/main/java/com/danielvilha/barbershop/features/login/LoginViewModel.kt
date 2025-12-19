package com.danielvilha.barbershop.features.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.barbershop.data.SessionManager
import com.danielvilha.barbershop.data.User
import com.danielvilha.barbershop.features.auth.GoogleAuthUiClient
import com.danielvilha.barbershop.features.auth.GoogleSignInCredentialResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
    private val googleAuthUiClient: GoogleAuthUiClient,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnGoogleSignInClick -> {
                signInWithGoogle(event.activity)
            }
            is LoginUiEvent.OnFacebookSignInClick -> {}
        }
    }

    private fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val signInResult = googleAuthUiClient.getGoogleSignInCredential(activity)) {
                is GoogleSignInCredentialResult.Success -> {
                    try {
                        val authResult = firebaseAuth.signInWithCredential(signInResult.credential).await()
                        val firebaseUser = authResult.user

                        if (firebaseUser != null) {
                            handleFirestoreForGoogleUser(firebaseUser)
                        } else {
                            _uiState.update { it.copy(error = "Google sign-in failed.", isLoading = false) }
                        }
                    } catch (e: Exception) {
                        _uiState.update { it.copy(error = e.message ?: "An unexpected error occurred.", isLoading = false) }
                    }
                }
                is GoogleSignInCredentialResult.Error -> {
                    _uiState.update { it.copy(error = signInResult.message, isLoading = false) }
                }
                is GoogleSignInCredentialResult.Cancelled -> {
                    _uiState.update { it.copy(error = signInResult.exception.message ?: "Google sign-in cancelled.", isLoading = false) }
                }
            }
        }
    }

    private suspend fun handleFirestoreForGoogleUser(firebaseUser: FirebaseUser) {
        val userDocument = firestore
            .collection("users")
            .document(firebaseUser.uid)
            .get().await()

        if (userDocument.exists()) {
            fetchUserAndSaveSession(firebaseUser.uid)
        } else {
            val newUser = User(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName ?: "New User",
                email = firebaseUser.email ?: "",
                birthdate = "",
                photoUrl = firebaseUser.photoUrl?.toString(),
                phone = firebaseUser.phoneNumber,
            )

            firestore.collection("users").document(newUser.uid).set(newUser).await()
            fetchUserAndSaveSession(newUser.uid)
        }
    }

    private suspend fun fetchUserAndSaveSession(uid: String) {
        try {
            val documentSnapshot = firestore.collection("users").document(uid).get().await()
            val user = documentSnapshot.toObject(User::class.java)

            if (user != null) {
                sessionManager.saveUserSession(user)
                _uiState.update { it.copy(isLoading = false, isSignInSuccessful = true, error = null) }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "User data not found in Firestore.") }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = "Failed to fetch user data: ${e.message}") }
            handleAuthError(e)
        }
    }

    private fun handleAuthError(e: Exception) {
        _uiState.update { state ->
            when (e) {
                is FirebaseAuthInvalidUserException -> {
                    state.copy(error = "User not found. Please check your email or create an account.", isLoading = false)
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    state.copy(error = "The email format is invalid.", isLoading = false)
                }
                else -> {
                    state.copy(error = e.message ?: "An unexpected error occurred. Please try again.", isLoading = false)
                }
            }
        }
    }
}