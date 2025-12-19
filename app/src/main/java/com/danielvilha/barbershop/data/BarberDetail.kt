package com.danielvilha.barbershop.data

data class BarberDetails(
    val uid: String = "",
    val name: String = "",
    val about: String = "",
    val imageUrl: String = "",
    val barbershop: BarbershopDetails? = BarbershopDetails.createBarbershop(),
    val availability: List<String> = listOf(),
) {
    constructor(barber: Barber) : this(
        uid = barber.uid,
        name = barber.name,
        about = barber.about,
        imageUrl = barber.imageUrl,
        availability = barber.availability
    )
    companion object {
        fun createBarbers(): List<BarberDetails> = listOf(
            BarberDetails(
                uid = "1",
                name = "John Doe",
                about = "Barber",
                imageUrl = "https://www.google.com/",
                barbershop = BarbershopDetails.createBarbershop(),
                availability = listOf(
                    "",
                    "09:00am - 06:00pm",
                    "09:00am - 06:00pm",
                    "",
                    "09:00am - 06:00pm",
                    "10:00am - 07:00pm",
                    "10:30am - 07:30pm"
                )
            )
        )

        fun createBarber(): BarberDetails = BarberDetails(
            uid = "1",
            name = "John Doe",
            about = "Barber",
            imageUrl = "https://www.google.com/",
            barbershop = BarbershopDetails.createBarbershop(),
            availability = listOf(
                "",
                "09:00am - 06:00pm",
                "09:00am - 06:00pm",
                "",
                "09:00am - 06:00pm",
                "10:00am - 07:00pm",
                "10:30am - 07:30pm"
            )
        )

        fun Companion.toBarberDetails(barber: Barber): BarberDetails {
            return BarberDetails(
                uid = barber.uid,
                name = barber.name,
                about = barber.about,
                imageUrl = barber.imageUrl,
                barbershop = BarbershopDetails(
                    uid = barber.barbershopUid,
                    name = "",
                    imageUrl = "",
                    phone = "",
                    address = null,
                    services = emptyList(),
                    barbers = emptyList()
                ),
                availability = barber.availability
            )
        }
    }
}
