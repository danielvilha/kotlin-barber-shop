package com.danielvilha.barbershop.features.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.barbershop.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userId = auth.currentUser?.uid ?: return@launch

            try {
                val document = firestore.collection("users").document(userId).get().await()
                val user = document.toObject(User::class.java)
                _uiState.update { it.copy(user = user, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Failed to load user data: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.OnPhoneNumberChanged -> {
                _uiState.update {
                    it.copy(
                        user = it.user?.copy(
                            phone = event.phone,
                            ddi = event.ddi
                        )
                    )
                }
            }
            is ProfileUiEvent.OnSaveChanges -> {
                saveUserData()
            }
            is ProfileUiEvent.OnPhotoChanged -> {
                updateProfilePhoto(event.photoUri)
            }
        }
    }

    private fun saveUserData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = _uiState.value.user ?: return@launch

            try {
                firestore.collection("users").document(user.uid).set(user).await()
                _uiState.update { it.copy(isLoading = false, saveSuccess = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Failed to save changes: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun updateProfilePhoto(photoUri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userId = auth.currentUser?.uid ?: return@launch

            try {
                val storageRef = storage.reference.child("profile_images/${userId}.jpg")

                storageRef.putFile(photoUri).await()

                val downloadUrl = storageRef.downloadUrl.await().toString()

                firestore.collection("users").document(userId)
                    .update("photoUrl", downloadUrl)
                    .await()

                _uiState.update {
                    it.copy(
                        user = it.user?.copy(photoUrl = downloadUrl),
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Failed to update photo: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }
}