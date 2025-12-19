package com.danielvilha.barbershop.features.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.danielvilha.barbershop.Destinations
import com.danielvilha.barbershop.R
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.CardBackground
import com.danielvilha.barbershop.ui.theme.GoldAccent
import com.danielvilha.barbershop.ui.theme.TextGray
import com.danielvilha.barbershop.ui.theme.TextWhite

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?,
) {
    NavigationBar(containerColor = CardBackground) {
        NavigationBarItem(
            icon = { Icon(painterResource(R.drawable.ic_home), contentDescription = null) },
            label = { Text(stringResource(R.string.start)) },
            selected = currentRoute == Destinations.HOME_ROUTE,
            onClick = {
                navController.navigate(Destinations.HOME_ROUTE) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TextWhite,
                selectedTextColor = TextWhite,
                indicatorColor = GoldAccent.copy(alpha = 0.2f),
                unselectedIconColor = TextGray,
                unselectedTextColor = TextGray
            )
        )
        NavigationBarItem(
            icon = { Icon(painterResource(R.drawable.ic_calendar), contentDescription = null) },
            label = { Text(stringResource(R.string.booking)) },
            selected = currentRoute == Destinations.BOOKING_ROUTE,
            onClick = {
                navController.navigate("${Destinations.BOOKING_ROUTE}/barbershopId=null&barberId=null") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TextWhite,
                selectedTextColor = TextWhite,
                indicatorColor = GoldAccent.copy(alpha = 0.2f),
                unselectedIconColor = TextGray,
                unselectedTextColor = TextGray
            )
        )
    }
}

@LightDarkPreview
@Composable
@ExcludeFromJacocoGeneratedReport
fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    BottomNavigationBar(
        navController = navController,
        currentRoute = Destinations.HOME_ROUTE
    )
}
