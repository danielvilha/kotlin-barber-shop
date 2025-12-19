package com.danielvilha.barbershop.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.barbershop.data.Address
import com.danielvilha.barbershop.data.Appointment
import com.danielvilha.barbershop.data.Barber
import com.danielvilha.barbershop.data.BarberDetails
import com.danielvilha.barbershop.data.BarberDetails.Companion.toBarberDetails
import com.danielvilha.barbershop.data.Barbershop
import com.danielvilha.barbershop.data.BarbershopDetails
import com.danielvilha.barbershop.data.Booking
import com.danielvilha.barbershop.data.BookingDetails
import com.danielvilha.barbershop.data.Service
import com.danielvilha.barbershop.data.SessionManager
import com.danielvilha.barbershop.data.User
import com.danielvilha.barbershop.features.navigation.NavigationEvent
import com.danielvilha.barbershop.features.util.SearchResult
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationChannel = Channel<NavigationEvent>()
    val navigationEvents = _navigationChannel.receiveAsFlow()

    init {
        sessionManager.userFlow
            .onEach { user ->
                _uiState.update { it.copy(user = user) }
            }
            .launchIn(viewModelScope)

        fetchUserData()
        fetchBookingData()
        fetchBarbershopsDetails()
        fetchBarberDetails()
        loadUpcomingAppointment()
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                _uiState.update { it.copy(error = "User not logged in") }
                return@launch
            }

            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val user = document.toObject(User::class.java)
                        _uiState.update { it.copy(user = user) }
                    } else {
                        _uiState.update { it.copy(error = "User not found") }
                    }
                }
                .addOnFailureListener { exception ->
                    _uiState.update { it.copy(error = "Error fetching user data: ${exception.message}") }
                }
        }
    }

    private fun fetchBookingData() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid

            try {
                val now = Timestamp(Date())
                val bookingQuery = firestore.collection("bookings")
                    .whereEqualTo("userId", userId)
                    .whereGreaterThanOrEqualTo("timestamp", now)
                    .orderBy("timestamp")
                    .limit(1)
                    .get()
                    .await()

                if (bookingQuery.isEmpty) {
                    return@launch
                }

                val bookingDoc = bookingQuery.documents.first()
                val booking = bookingDoc.toObject(Booking::class.java) ?: return@launch

                val barbershopName = firestore.collection("barbershops")
                    .document(booking.barbershopId)
                    .get().await().getString("name") ?: "Unknown Barbershop"

                val serviceName = firestore.collection("services")
                    .document(booking.serviceId)
                    .get().await().getString("name") ?: "Unknown Service"

                val bookingDetails = BookingDetails(
                    barbershopName = barbershopName,
                    serviceName = serviceName,
                    date = booking.timestamp.toDate()
                )

                _uiState.update { it.copy(booking = bookingDetails) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Failure to fetch bookings: ${e.message}",
                    )
                }
            }
        }
    }

    private fun fetchBarbershopsDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val barbershopDocs = firestore.collection("barbershops").get().await()
                val barbershops = barbershopDocs.toObjects(Barbershop::class.java)

                val addressUids = barbershops.mapNotNull { it.addressUid.takeIf { uid -> uid.isNotBlank() } }
                val serviceUids = barbershops.flatMap { it.servicesUid }.distinct()

                val addressesDeferred = async {
                    addressUids.chunked(30).flatMap { chunk ->
                        if (chunk.isEmpty()) emptyList()
                        else firestore.collection("addresses").whereIn(FieldPath.documentId(), chunk).get().await().toObjects(Address::class.java)
                    }.associateBy { it.uid }
                }

                val servicesDeferred = async {
                    serviceUids.chunked(30).flatMap { chunk ->
                        if (chunk.isEmpty()) emptyList()
                        else firestore
                            .collection("services")
                            .whereIn(FieldPath.documentId(), chunk)
                            .get()
                            .await()
                            .toObjects(Service::class.java)
                    }.associateBy { it.uid }
                }

                val barberDocs = firestore.collection("barbers").get().await()
                val barbers = barberDocs.toObjects(Barber::class.java)
                val barbersByBarbershop = barbers.groupBy { it.barbershopUid }

                val addressesMap = addressesDeferred.await()
                val servicesMap = servicesDeferred.await()

                val barbershopDetailsList = barbershops.map { barbershop ->
                    BarbershopDetails(
                        uid = barbershop.uid,
                        name = barbershop.name,
                        imageUrl = barbershop.imageUrl,
                        phone = barbershop.phone,
                        address = addressesMap[barbershop.addressUid],
                        services = barbershop.servicesUid.mapNotNull { uid -> servicesMap[uid] },
                        barbers = barbersByBarbershop[barbershop.uid] ?: emptyList()
                    )
                }

                val recommendedBarbershops = barbershopDetailsList.shuffled().take(2)

                _uiState.update {
                    it.copy(
                        recommendedBarbershops = recommendedBarbershops,
                        barbershops = barbershopDetailsList,
                        isLoading = false,
                        error = null
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Failure to search for barbershops: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun fetchBarberDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val barberDocs = firestore.collection("barbers").get().await()
                val barbers = barberDocs.toObjects(Barber::class.java)

                val recommendedBarbers = barbers.shuffled().take(5).map { barber ->
                    BarberDetails.toBarberDetails(barber)
                }

                _uiState.update {
                    it.copy(
                        recommendedBarbers = recommendedBarbers,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Failure to search for barbers: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun loadUpcomingAppointment() {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user == null) {
                _uiState.update { it.copy(upcomingAppointment = null) }
                return@launch
            }

            try {
                val snapshot = firestore.collection("appointments")
                    .whereEqualTo("clientUid", user.uid)
                    .whereGreaterThanOrEqualTo("dateTime", Timestamp.now())
                    .orderBy("dateTime", Query.Direction.ASCENDING)
                    .limit(1)
                    .get()
                    .await()

                if (!snapshot.isEmpty) {
                    val appointment = snapshot.documents.first().toObject(Appointment::class.java)
                    _uiState.update { it.copy(upcomingAppointment = appointment) }
                } else {
                    _uiState.update { it.copy(upcomingAppointment = null) }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to fetch upcoming appointment", e)
                _uiState.update { it.copy(error = "Could not load appointment.: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }

    }

    fun refreshUpcomingAppointment() {
        loadUpcomingAppointment()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnSearch -> onSearch(event.search)
            is HomeUiEvent.OnItemSelected -> onItemSelected(event.searchResult)
            is HomeUiEvent.OnQueryChange -> onQueryChange(event.searchQuery)
            is HomeUiEvent.OnSearchTypeChange -> onSearchTypeChange(event.searchType)
            is HomeUiEvent.OnBarbershopClick -> onBarbershopClick(event.barbershopId)
            is HomeUiEvent.OnBackClick -> onBackClick()
            is HomeUiEvent.OnLogout -> onLogout()
            is HomeUiEvent.RefreshUpcomingAppointment -> refreshUpcomingAppointment()
        }
    }

    private fun onBackClick() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                searchResults = emptyList(),
                showSearchResults = false
            )
        }
    }

    private fun onLogout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            _uiState.update { it.copy(user = null) }
            auth.signOut()
            sessionManager.clearUserSession()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun onSearchTypeChange(searchType: SearchType) {
        _uiState.update { it.copy(searchType = searchType) }
    }

    private fun onQueryChange(searchQuery: String) {
        _uiState.update { it.copy(searchQuery = searchQuery) }
        if (searchQuery.length > 2) {
            searchBarbers(searchQuery)
        } else if (searchQuery.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    private fun searchBarbers(searchQuery: String) {
        viewModelScope.launch {
            try {
                val results = mutableListOf<SearchResult>()

                val barbershopQuery = firestore.collection("barbershops")
                    .whereGreaterThanOrEqualTo("name", searchQuery)
                    .whereLessThanOrEqualTo("name", searchQuery + '\uf8ff')
                    .get()
                    .await()

                val barbershops = barbershopQuery.toObjects(Barbershop::class.java)
                results.addAll(barbershops.map {
                    SearchResult.Barbershop(id = it.uid, name = it.name)
                })

                val barberQuery = firestore.collection("barbers")
                    .whereGreaterThanOrEqualTo("name", searchQuery)
                    .whereLessThanOrEqualTo("name", searchQuery + '\uf8ff')
                    .get()
                    .await()

                val barbers = barberQuery.toObjects(Barber::class.java)
                results.addAll(barbers.map {
                    SearchResult.Barber(id = it.uid, name = it.name)
                })

                _uiState.update {
                    it.copy(
                        searchResults = results.distinct(),
                        showSearchResults = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Failure to search: ${e.message}",
                    )
                }
            }
        }
    }

    private fun onSearch(searchQuery: String) {
        _uiState.update {
            it.copy(
                searchQuery = searchQuery,
                showSearchResults = true
            )
        }
    }

    private fun onItemSelected(item: SearchResult) {
        viewModelScope.launch {
            when (item) {
                is SearchResult.Barber -> {
                    _navigationChannel.send(NavigationEvent.ToBarberDetails(item.id))
                }

                is SearchResult.Barbershop -> {
                    println("Selected barbershop: ${item.name} (ID: ${item.id})")
                     _navigationChannel.send(NavigationEvent.ToBookingScreen(item.id))
                }
            }

            onBackClick()
        }
    }

    private fun onBarbershopClick(barbershopId: String) {
        viewModelScope.launch {
            _navigationChannel.send(NavigationEvent.ToBookingScreen(barbershopId))
        }
    }
}
