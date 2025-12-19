package com.danielvilha.barbershop.features.login

data class LoginUiState(
    val successMessage: String? = null,
    val isSignInSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
