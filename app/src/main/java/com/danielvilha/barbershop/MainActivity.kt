package com.danielvilha.barbershop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.danielvilha.barbershop.features.splash.SplashScreen
import com.danielvilha.barbershop.ui.main.MainViewModel
import com.danielvilha.barbershop.ui.theme.BarberShopTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            BarberShopTheme {
                val mainState by mainViewModel.uiState.collectAsState()

                if (mainState.isLoading) {
                    SplashScreen()
                } else {
                    val startDestination = if (mainState.isUserLoggedIn) {
                        Destinations.HOME_ROUTE
                    } else {
                        Destinations.LOGIN_ROUTE
                    }

                    BarberNavGraph(startDestination)
                }
            }
        }
    }
}