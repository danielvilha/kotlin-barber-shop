package com.danielvilha.barbershop.data

import com.google.firebase.Timestamp

data class Booking(
    val uid: String = "",
    val userId: String = "",
    val barbershopId: String = "",
    val serviceId: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
