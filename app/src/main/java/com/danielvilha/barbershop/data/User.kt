package com.danielvilha.barbershop.data

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val password: String? = "",
    val birthdate: String? = "",
    val phone: String? = "",
    val ddi: String? = "",
    val photoUrl: String? = "",
    var barberId: List<String>? = emptyList()
) {
    companion object {
        fun getUser() = User(
            uid = "1",
            name = "John Snow",
            email = "snow@email.com"
        )
    }
}
