package com.danielvilha.barbershop.ui.main

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.barbershop.data.SessionManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        checkUserSession()
    }

    private fun checkUserSession() {
        viewModelScope.launch {
            val firebaseUser = firebaseAuth.currentUser
            val sessionUser = sessionManager.userFlow.first()
            val isLoggedIn = firebaseUser != null && sessionUser != null && firebaseUser.uid == sessionUser.uid

            _uiState.value = MainUiState(isLoading = false, isUserLoggedIn = isLoggedIn)
        }
    }
}