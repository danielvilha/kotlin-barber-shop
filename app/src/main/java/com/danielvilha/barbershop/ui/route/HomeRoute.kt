package com.danielvilha.barbershop.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.danielvilha.barbershop.Destinations
import com.danielvilha.barbershop.features.home.HomeScreen
import com.danielvilha.barbershop.features.home.HomeViewModel
import com.danielvilha.barbershop.features.navigation.NavigationEvent

@Composable
fun HomeRoute(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.ToBookingScreen -> {
                    navController.navigate("${Destinations.BOOKING_ROUTE}/barbershopId=${event.barbershopId}&barberId=null")
                }
                is NavigationEvent.ToBarberDetails -> {
                    navController.navigate("${Destinations.BARBER_DETAIL_ROUTE}/${event.barberId}")
                }
                is NavigationEvent.ToProfileScreen -> {
                    navController.navigate(Destinations.PROFILE_ROUTE)
                }
            }
        }
    }

    HomeScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}