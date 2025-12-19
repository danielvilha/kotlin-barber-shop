package com.danielvilha.barbershop.data

data class Barbershop(
    val uid: String = "",
    val name: String = "",
    val addressUid: String = "",
    val phone: String = "",
    val imageUrl: String = "",
    val servicesUid: List<String> = emptyList(),
    val barbersUid: List<String> = emptyList(),
)
