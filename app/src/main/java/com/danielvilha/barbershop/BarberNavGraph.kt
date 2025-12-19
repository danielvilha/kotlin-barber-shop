package com.danielvilha.barbershop

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danielvilha.barbershop.ui.route.BarberDetailRoute
import com.danielvilha.barbershop.ui.route.BookingRoute
import com.danielvilha.barbershop.ui.route.HomeRoute
import com.danielvilha.barbershop.ui.route.LoginRoute
import com.danielvilha.barbershop.ui.route.ProfileRoute

object Destinations {
    const val LOGIN_ROUTE = "login"
    const val HOME_ROUTE = "home"
    const val BARBER_DETAIL_ROUTE = "barber_detail"
    const val BOOKING_ROUTE = "booking"
    const val PROFILE_ROUTE = "profile"
}


@Composable
fun BarberNavGraph(startDestination: String) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Destinations.LOGIN_ROUTE) {
            LoginRoute(
                onHome = {
                    navController.navigate(Destinations.HOME_ROUTE) {
                        popUpTo(Destinations.LOGIN_ROUTE) { inclusive = true }
                    }
                },
            )
        }

        composable(Destinations.HOME_ROUTE) {
            HomeRoute(navController = navController)
        }

        composable(
            route = "${Destinations.BARBER_DETAIL_ROUTE}/{barberId}",
            arguments = listOf(navArgument("barberId") { type = NavType.StringType })
        ) {
            BarberDetailRoute(navController = navController)
        }

        composable(
            route = "${Destinations.BOOKING_ROUTE}/barbershopId={barbershopId}&barberId={barberId}",
            listOf(
                navArgument("barbershopId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("barberId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            BookingRoute(navController = navController)
        }

        composable(Destinations.PROFILE_ROUTE) {
            ProfileRoute(navController = navController)
        }
    }
}
