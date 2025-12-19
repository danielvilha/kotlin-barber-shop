package com.danielvilha.barbershop.data

import java.util.Date

data class BookingDetails(
    val date: Date,
    val barbershopName: String,
    val serviceName: String
)
