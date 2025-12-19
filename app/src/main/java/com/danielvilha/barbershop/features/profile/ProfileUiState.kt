package com.danielvilha.barbershop.features.profile

import com.danielvilha.barbershop.data.User

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val saveSuccess: Boolean = false
)
