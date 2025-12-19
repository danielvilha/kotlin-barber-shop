package com.danielvilha.barbershop.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.danielvilha.barbershop.features.profile.ProfileScreen
import com.danielvilha.barbershop.features.profile.ProfileViewModel

@Composable
fun ProfileRoute(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        navController = navController,
        onEvent = viewModel::onEvent
    )
}