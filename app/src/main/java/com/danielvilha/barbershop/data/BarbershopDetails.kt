package com.danielvilha.barbershop.data

import com.google.firebase.firestore.GeoPoint

data class BarbershopDetails(
    val uid: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val phone: String = "",
    val address: Address? = null,
    val services: List<Service> = emptyList(),
    val barbers: List<Barber> = emptyList()
) {
    val displayServices: String
        get() = services.joinToString(" • ") { it.name }

    companion object {
        fun createBarbershops() = listOf(
            BarbershopDetails(
                uid = "1",
                name = "Vintage Barber",
                address = Address(
                    uid = "1",
                    street = "Castle Gate, Lord Edward St",
                    number = "5",
                    city = "Dublin",
                    state = "Co. Dublin",
                    postalCode = "D02 RK54",
                    country = "Ireland",
                    building = "",
                    neighborhood = "",
                    region = "",
                    latLong = GeoPoint(
                        53.34379647211399,
                        -6.269145137236095
                    )
                ),
                phone = "0851657769",
                imageUrl = "https://www.google.com/maps/place/Cut+%2B+Sew+Lord+Edward+",
                services = listOf(
                    Service(
                        uid = "1",
                        name = "Haircut",
                        price = "30.0",
                        duration = "30",
                        currencyCode = "EUR"
                    ),
                    Service(
                        uid = "2",
                        name = "Haircut and Beard trim",
                        price = "40.0",
                        duration = "45",
                        currencyCode = "EUR"
                    ),
                    Service(
                        uid = "3",
                        name = "Beard trim",
                        price = "10.0",
                        duration = "15",
                        currencyCode = "EUR"
                    ),
                ),
                barbers = listOf(
                    Barber(
                        uid = "1",
                        name = "John Doe",
                        imageUrl = "https://www.google.com/maps/place/Cut+%2B+Sew+Lord+Edward",
                        about = "Barber with a lot of experience",
                        availability = listOf("", "", "", "", "", "", ""),
                    )
                )
            ),
            BarbershopDetails(
                uid = "2",
                name = "Vintage Barber",
                address = Address(
                    uid = "2",
                    street = "Castle Gate, Lord Edward St",
                    number = "5",
                    city = "Dublin",
                    state = "Co. Dublin",
                    postalCode = "D02 RK54",
                    country = "Ireland",
                    building = "",
                    neighborhood = "",
                    region = "",
                    latLong = GeoPoint(
                        53.34379647211399,
                        -6.269145137236095
                    )
                ),
                phone = "0851657769",
                imageUrl = "https://www.google.com/maps/place/Cut+%2B+Sew+Lord+Edward+",
                services = listOf(
                    Service(
                        uid = "1",
                        name = "Haircut",
                        price = "30.0",
                        duration = "30",
                        currencyCode = "EUR"
                    ),
                    Service(
                        uid = "2",
                        name = "Haircut and Beard trim",
                        price = "40.0",
                        duration = "45",
                        currencyCode = "EUR"
                    ),
                    Service(
                        uid = "3",
                        name = "Beard trim",
                        price = "10.0",
                        duration = "15",
                        currencyCode = "EUR"
                    ),
                ),
                barbers = listOf(
                    Barber(
                        uid = "1",
                        name = "John Doe",
                        imageUrl = "https://www.google.com/maps/place/Cut+%2B+Sew+Lord+Edward",
                        about = "Barber with a lot of experience",
                        availability = listOf("", "", "", "", "", "", ""),
                    )
                )
            ),
        )
        fun createBarbershop() = BarbershopDetails(
            uid = "1",
            name = "Vintage Barber",
            address = Address(
                uid = "1",
                street = "Castle Gate, Lord Edward St",
                number = "5",
                city = "Dublin",
                state = "Co. Dublin",
                postalCode = "D02 RK54",
                country = "Ireland",
                building = "",
                neighborhood = "",
                region = "",
                latLong = GeoPoint(
                    53.34379647211399,
                    -6.269145137236095
                )
            ),
            phone = "0851657769",
            imageUrl = "https://www.google.com/maps/place/Cut+%2B+Sew+Lord+Edward+",
            services = listOf(
                Service(
                    uid = "1",
                    name = "Haircut",
                    price = "30.0",
                    duration = "30",
                    currencyCode = "EUR"
                ),
                Service(
                    uid = "2",
                    name = "Haircut and Beard trim",
                    price = "40.0",
                    duration = "45",
                    currencyCode = "EUR"
                ),
                Service(
                    uid = "3",
                    name = "Beard trim",
                    price = "10.0",
                    duration = "15",
                    currencyCode = "EUR"
                ),
            ),
            barbers = listOf(
                Barber(
                    uid = "1",
                    name = "John Doe",
                    imageUrl = "https://www.google.com/maps/place/Cut+%2B+Sew+Lord+Edward",
                    about = "Barber with a lot of experience",
                    availability = listOf("", "", "", "", "", "", ""),
                )
            )
        )
    }
}
