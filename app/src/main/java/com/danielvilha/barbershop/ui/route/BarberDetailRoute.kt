package com.danielvilha.barbershop.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.danielvilha.barbershop.features.details.DetailScreen
import com.danielvilha.barbershop.features.details.BarberDetailViewModel

@Composable
fun BarberDetailRoute(
    navController: NavController,
    viewModel: BarberDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    DetailScreen(
        state = state,
        navController = navController,
    )
}