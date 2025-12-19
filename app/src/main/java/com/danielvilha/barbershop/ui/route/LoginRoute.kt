package com.danielvilha.barbershop.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danielvilha.barbershop.features.login.LoginScreen
import com.danielvilha.barbershop.features.login.LoginViewModel

@Composable
fun LoginRoute(
    onHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onHome = onHome,
    )
}