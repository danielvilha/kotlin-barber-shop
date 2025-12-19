
package com.danielvilha.barbershop.features.util

sealed class SearchResult {
    data class Barber(val id: String, val name: String) : SearchResult()
    data class Barbershop(val id: String, val name: String) : SearchResult()
}
