package com.danielvilha.barbershop.features.navigation

sealed class NavigationEvent {
    data class ToBookingScreen(val barbershopId: String) : NavigationEvent()
    data class ToBarberDetails(val barberId: String) : NavigationEvent()
    object ToProfileScreen : NavigationEvent()
}
