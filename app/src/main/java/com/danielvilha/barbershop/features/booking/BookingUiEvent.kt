package com.danielvilha.barbershop.features.booking

import com.danielvilha.barbershop.data.Barber
import com.danielvilha.barbershop.data.BarbershopDetails
import java.util.Calendar

sealed class BookingUiEvent {
    data class OnBarbershopSelected(val barbershop: BarbershopDetails) : BookingUiEvent()
    data class OnBarberSelected(val barber: Barber) : BookingUiEvent()
    data class OnBookingConfirmed(
        val barber: Barber,
        val date: Calendar?,
        val time: String?
    ) : BookingUiEvent()
}
