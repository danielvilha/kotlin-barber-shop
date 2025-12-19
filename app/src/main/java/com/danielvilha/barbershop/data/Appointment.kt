package com.danielvilha.barbershop.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Appointment(
    @DocumentId
    val id: String = "",
    val barbershopUid: String = "",
    val barberUid: String = "",
    val clientUid: String = "",
    val dateTime: Timestamp = Timestamp.now(),
    val createdAt: Timestamp = Timestamp.now()
)
