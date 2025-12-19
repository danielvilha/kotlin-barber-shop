package com.danielvilha.barbershop.features.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.barbershop.data.Appointment
import com.danielvilha.barbershop.data.Barber
import com.danielvilha.barbershop.data.BarbershopDetails
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState = _uiState.asStateFlow()

    private val barbershopId: String? = savedStateHandle.get<String>("barbershopId")
    private val barberId: String? = savedStateHandle.get<String>("barberId")

    init {
        loadInitialData()
    }

    fun onEvent(event: BookingUiEvent) {
        when (event) {
            is BookingUiEvent.OnBarbershopSelected -> {
                viewModelScope.launch {
                    try {
                        val barbershop = event.barbershop
                        val barbersDocs = firestore.collection("barbers")
                            .whereEqualTo("barbershopUid", barbershop.uid).get().await()
                        val availableBarbers = barbersDocs.toObjects(Barber::class.java)

                        _uiState.update {
                            it.copy(
                                selectedBarbershop = barbershop,
                                availableBarbers = availableBarbers,
                                selectedBarber = null,
                                selectedService = null,
                                selectedDate = null,
                                selectedTime = null
                            )
                        }
                    } catch (e: Exception) {
                        _uiState.update { it.copy(error = "Failed to update barbers: ${e.message}") }
                    }
                }
            }
            is BookingUiEvent.OnBarberSelected -> {
                val barbershop = _uiState.value.allBarbershops.find { it.uid == event.barber.barbershopUid }
                _uiState.update {
                    it.copy(
                        selectedBarbershop = barbershop,
                        selectedBarber = event.barber
                    )
                }
            }
            is BookingUiEvent.OnBookingConfirmed -> {
                onBookingConfirmed(event.barber, event.date, event.time)
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val barbershopDocs = firestore.collection("barbershops").get().await()
                val allBarbershops = barbershopDocs.toObjects(BarbershopDetails::class.java)
                _uiState.update { it.copy(allBarbershops = allBarbershops) }

                when {
                    barberId != null -> {
                        val barberDoc = firestore.collection("barbers").document(barberId).get().await()
                        val barber = barberDoc.toObject(Barber::class.java)
                        if (barber != null) {
                            val barbershop = allBarbershops.find { it.uid == barber.barbershopUid }

                            val barbersDocs = firestore.collection("barbers")
                                .whereEqualTo("barbershopUid", barber.barbershopUid).get().await()
                            val availableBarbers = barbersDocs.toObjects(Barber::class.java)

                            _uiState.update {
                                it.copy(
                                    selectedBarbershop = barbershop,
                                    selectedBarber = barber,
                                    availableServices = barbershop?.services ?: emptyList(),
                                    availableBarbers = availableBarbers
                                )
                            }
                        }
                    }
                    barbershopId != null -> {
                        val barbershop = allBarbershops.find { it.uid == barbershopId }

                        val barbersDocs = firestore.collection("barbers")
                            .whereEqualTo("barbershopUid", barbershopId).get().await()
                        val availableBarbers = barbersDocs.toObjects(Barber::class.java)

                        _uiState.update {
                            it.copy(
                                selectedBarbershop = barbershop,
                                availableServices = barbershop?.services ?: emptyList(),
                                availableBarbers = availableBarbers
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to load data: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onBookingConfirmed(barber: Barber, date: Calendar?, time: String?) {
        if (date == null) {
            _uiState.update { it.copy(error = "Invalid date.") }
            return
        }
        if (time?.isBlank() == true) {
            _uiState.update { it.copy(error = "Invalid time.") }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val clientUid = auth.currentUser?.uid
                if (clientUid == null) {
                    _uiState.update { it.copy(error = "User not logged in.") }
                    return@launch
                }

                val (hour, minute) = time!!.split(":").map { it.toInt() }
                val appointmentDateTime = (date.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val newAppointment = Appointment(
                    clientUid = clientUid,
                    barberUid = barber.uid,
                    barbershopUid = barber.barbershopUid,
                    dateTime = Timestamp(appointmentDateTime.time),
                    createdAt = Timestamp.now()
                )

                firestore.collection("appointments")
                    .add(newAppointment)
                    .await()

                _uiState.update { it.copy(isLoading = false, bookingConfirmed = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
