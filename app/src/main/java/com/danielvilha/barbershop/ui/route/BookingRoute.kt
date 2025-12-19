package com.danielvilha.barbershop.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.danielvilha.barbershop.features.booking.BookingScreen
import com.danielvilha.barbershop.features.booking.BookingViewModel

@Composable
fun BookingRoute(
    navController: NavController,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    BookingScreen(
        state = state,
        onEvent = viewModel::onEvent,
        navController = navController
    )
}