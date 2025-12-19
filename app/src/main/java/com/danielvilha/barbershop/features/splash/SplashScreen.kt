package com.danielvilha.barbershop.features.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.danielvilha.barbershop.features.util.ProgressIndicator
import com.danielvilha.barbershop.ui.theme.CardBackground

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CardBackground),
        contentAlignment = Alignment.Center
    ) {
        ProgressIndicator()
    }
}