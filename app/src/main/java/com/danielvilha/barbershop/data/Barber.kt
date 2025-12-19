package com.danielvilha.barbershop.data

data class Barber(
    val uid: String = "",
    val name: String = "",
    val about: String = "",
    val imageUrl: String = "",
    val barbershopUid: String = "",
    val availability: List<String> = listOf(),
) {
    companion object {
        fun Companion.createBarbers() = listOf(
            Barber(
                uid = "1",
                name = "John Snow",
                about = "Senior barber with a lot of experience.",
                imageUrl = "https://www.google.com/maps/place/Cut+%2B+Sew+Lord+Edward",
                barbershopUid = "1",
                availability = listOf(
                    "",
                    "09:00am - 06:00pm",
                    "09:00am - 06:00pm",
                    "",
                    "09:00am - 06:00pm",
                    "10:00am - 07:00pm",
                    "10:30am - 07:30pm"
                )
            ),
            Barber(
                uid = "1",
                name = "John Snow",
                about = "Senior barber with a lot of experience.",
                imageUrl = "https://www.google.com/maps/place/Cut+%2B+Sew+Lord+Edward",
                barbershopUid = "1",
                availability = listOf(
                    "",
                    "09:00am - 06:00pm",
                    "09:00am - 06:00pm",
                    "",
                    "09:00am - 06:00pm",
                    "10:00am - 07:00pm",
                    "10:30am - 07:30pm"
                )
            ),
            Barber(
                uid = "1",
                name = "John Snow",
                about = "Senior barber with a lot of experience.",
                imageUrl = "https://www.google.com/maps/place/Cut+%2B+Sew+Lord+Edward",
                barbershopUid = "1",
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

        fun Companion.createBarber(): Barber {
            return Barber(
                uid = "1",
                name = "John Snow",
                about = "Senior barber with a lot of experience.",
                imageUrl = "https://www.google.com/maps/place/Cut+%2B+Sew+Lord+Edward",
                barbershopUid = "1",
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
        }
    }
}
