package com.danielvilha.barbershop.features.home

import com.danielvilha.barbershop.data.Appointment
import com.danielvilha.barbershop.data.BarberDetails
import com.danielvilha.barbershop.data.BarbershopDetails
import com.danielvilha.barbershop.data.BookingDetails
import com.danielvilha.barbershop.data.User
import com.danielvilha.barbershop.features.util.SearchResult

data class HomeUiState(
    val user: User? = User(
        uid = "",
        name = "",
        email = "",
        password = null,
        birthdate = null,
        phone = null,
        barberId = emptyList(),
    ),
    val searchType: SearchType = SearchType.BARBER,
    val searchQuery: String = "",
    val booking: BookingDetails? = null,
    val barbers: List<BarberDetails>? = emptyList(),
    val barbershops: List<BarbershopDetails>? = emptyList(),
    val recommendedBarbershops: List<BarbershopDetails> = emptyList(),
    val recommendedBarbers: List<BarberDetails> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchResults: List<SearchResult> = emptyList(),
    val isSearching: Boolean = false,
    val showSearchResults: Boolean = false,
    val upcomingAppointment: Appointment? = null
)
