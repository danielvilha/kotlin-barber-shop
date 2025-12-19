package com.danielvilha.barbershop.data

import com.danielvilha.barbershop.data.Barber.Companion.createBarber
import com.danielvilha.barbershop.data.Service.Companion.createService

data class BarberService(
    val uid: String,
    val barber: Barber,
    val service: Service
) {
    companion object {
        fun createBarberService(): BarberService {
            return BarberService(
                uid = "",
                barber = Barber.createBarber(),
                service = Service.createService()
            )
        }
    }
}