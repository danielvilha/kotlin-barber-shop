package com.danielvilha.barbershop.data

import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.util.Log

private const val TAG = "Service"
data class Service(
    val uid: String = "",
    val name: String = "",
    val duration: Any? = null,
    val price: Any? = null,
    val currencyCode: String = ""
) {
    val formattedPrice: String
        get() {
            val format = NumberFormat.getCurrencyInstance()
            try {
                format.currency = Currency.getInstance(currencyCode.ifBlank { "EUR" })
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
            }

            val priceAsDouble = when (price) {
                is Number -> price.toDouble()
                is String -> price.toDoubleOrNull() ?: 0.0
                else -> 0.0
            }
            return format.format(priceAsDouble)
        }

    val formattedDuration: String
        get() {
            return when (duration) {
                is Number -> {
                    val totalMinutes = duration.toLong()
                    if (totalMinutes <= 0) {
                        ""
                    } else {
                        val hours = totalMinutes / 60
                        val minutes = totalMinutes % 60
                        buildString {
                            if (hours > 0) {
                                append("${hours}h")
                            }
                            if (minutes > 0) {
                                if (isNotEmpty()) append(" ")
                                append("${minutes}min")
                            }
                        }
                    }
                }
                is String -> duration
                else -> ""
            }
        }

    companion object {
        fun Companion.createServices() = listOf(
            Service(
                uid = "1",
                name = "Haircut",
                duration = "30",
                price = "40",
                currencyCode = "EUR"
            )
        )

        fun Companion.createService() = Service(
            uid = "1",
            name = "Haircut",
            duration = "30",
            price = "40",
            currencyCode = "EUR"
        )
    }
}
