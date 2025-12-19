package com.danielvilha.barbershop.data

import com.google.firebase.firestore.GeoPoint

data class Address(
    val uid: String = "",
    val street: String = "",
    val number: Any? = null,
    val city: String = "",
    val state: String = "",
    val postalCode: String = "",
    val country: String = "",
    val building: String = "",
    val neighborhood: String = "",
    val region: String = "",
    val latLong: GeoPoint = GeoPoint(0.0, 0.0),
) {
    val displayString: String
        get() = "$street, ${number?.toString() ?: ""}, $city, $postalCode"
}
