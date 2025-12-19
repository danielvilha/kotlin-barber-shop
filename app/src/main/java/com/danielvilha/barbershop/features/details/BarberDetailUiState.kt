package com.danielvilha.barbershop.features.details

import com.danielvilha.barbershop.data.Barber

data class BarberDetailUiState(
    val barber: Barber? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
