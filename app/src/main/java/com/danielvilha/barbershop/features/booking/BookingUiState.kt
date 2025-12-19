package com.danielvilha.barbershop.features.booking

import com.danielvilha.barbershop.data.Barber
import com.danielvilha.barbershop.data.BarbershopDetails
import com.danielvilha.barbershop.data.Service
import java.util.Calendar

data class BookingUiState(
    val allBarbershops: List<BarbershopDetails> = emptyList(),
    val availableServices: List<Service> = emptyList(),
    val availableBarbers: List<Barber> = emptyList(),
    val selectedBarbershopId: String? = null,
    val selectedBarberId: String? = null,
    val selectedBarbershop: BarbershopDetails? = null,
    val selectedService: Service? = null,
    val selectedBarber: Barber? = null,
    val selectedDate: Calendar? = Calendar.getInstance(),
    val selectedTime: String? = null,
    val isLoading: Boolean = true,
    val bookingConfirmed: Boolean = false,
    val error: String? = null
)
