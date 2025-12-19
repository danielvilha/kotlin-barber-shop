package com.danielvilha.barbershop.features.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.barbershop.data.Barber
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class BarberDetailViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var barberId: String = savedStateHandle.get<String>("barberId")!!

    private val _uiState = MutableStateFlow(BarberDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (barberId.isNotBlank()) {
            fetchBarberDetails()
        } else {
            _uiState.update { it.copy(isLoading = false, error = "Barber ID is missing.") }
        }
    }

    private fun fetchBarberDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val document = firestore.collection("barbers").document(barberId).get().await()
                val barber = document.toObject(Barber::class.java)

                if (barber != null) {
                    _uiState.update {
                        it.copy(barber = barber, isLoading = false, error = null)
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Barber not found.")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Failed to fetch details: ${e.message}")
                }
            }
        }
    }
}