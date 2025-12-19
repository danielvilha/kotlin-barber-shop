package com.danielvilha.barbershop.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.danielvilha.barbershop.Destinations
import com.danielvilha.barbershop.R
import com.danielvilha.barbershop.data.BookingDetails
import com.danielvilha.barbershop.data.User
import com.danielvilha.barbershop.features.navigation.BottomNavigationBar
import com.danielvilha.barbershop.features.util.BarberCard
import com.danielvilha.barbershop.features.util.BarberSearchBar
import com.danielvilha.barbershop.features.util.BarbershopCard
import com.danielvilha.barbershop.features.util.BookingBanner
import com.danielvilha.barbershop.features.util.LogoutConfirmationDialog
import com.danielvilha.barbershop.features.util.ProgressIndicator
import com.danielvilha.barbershop.features.util.SearchResult
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.BarberShopTheme
import com.danielvilha.barbershop.ui.theme.DarkBackground
import com.danielvilha.barbershop.ui.theme.GoldAccent
import com.danielvilha.barbershop.ui.theme.TextGray
import com.danielvilha.barbershop.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Date

enum class SearchType {
    BARBER,
    LOCATION
}

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(HomeScreenPreview::class)
    state: HomeUiState
) {
    BarberShopTheme {
        HomeScreen(
            navController = rememberNavController(),
            state = state,
            onEvent = {}
        )
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    state: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit
) {

    var searchType by remember { mutableStateOf(SearchType.BARBER) }

    var menuExpanded by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val currentLocale = LocalConfiguration.current.locales[0]
    val currentDate = remember {
        val date = Date()
        val formatter = SimpleDateFormat("EEEE, d MMM", currentLocale)
        formatter.format(date).replaceFirstChar { if (it.isLowerCase()) it.titlecase(currentLocale) else it.toString() }
    }

    val bookingUpdated = navController.currentBackStackEntry
        ?.savedStateHandle?.get<Boolean>("booking_updated") ?: false

    LaunchedEffect(bookingUpdated) {
        if (bookingUpdated) {
            onEvent(HomeUiEvent.RefreshUpcomingAppointment)
            navController.currentBackStackEntry?.savedStateHandle?.set("booking_updated", false)
        }
    }

    Row {
        if (showLogoutDialog) {
            LogoutConfirmationDialog(
                onConfirm = {
                    showLogoutDialog = false
                    onEvent(HomeUiEvent.OnLogout)
                    navController.navigate(Destinations.LOGIN_ROUTE)
                },
                onDismiss = {
                    showLogoutDialog = false
                }
            )
        }

        Scaffold(
            topBar = {
                BarberSearchBar(
                    searchQuery = state.searchQuery,
                    onQueryChange = { query ->
                        onEvent(HomeUiEvent.OnQueryChange(query))
                    },
                    onSearch = { searQuery ->
                        onEvent(HomeUiEvent.OnSearch(searQuery))
                    },
                    searchResults = state.searchResults,
                    onItemSelected = {
                        onEvent(HomeUiEvent.OnItemSelected(it))
                    },
                    searchType = searchType,
                    onBackClick = {
                        onEvent(HomeUiEvent.OnBackClick)
                    },
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            bottomBar = {
                BottomNavigationBar(navController = navController, currentRoute = Destinations.HOME_ROUTE)
            },
            containerColor = DarkBackground
        ) { paddingValues ->
            when {
                state.showSearchResults -> {
                    SearchResultsList(
                        results = state.searchResults,
                        onItemSelected = {
                            onEvent(HomeUiEvent.OnItemSelected(it))
                        },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
                state.isLoading -> ProgressIndicator()
                else -> {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = 16.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.welcome_message, state.user?.name ?: ""),
                                    color = TextWhite,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = currentDate, color = TextGray, fontSize = 14.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(GoldAccent)
                            ) {
                                AsyncImage(
                                    model = state.user?.photoUrl,
                                    contentDescription = stringResource(
                                        id = R.string.photo_by,
                                        state.user?.name ?: ""
                                    ),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Box {
                                IconButton(onClick = { menuExpanded = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = stringResource(R.string.menu_options),
                                        tint = TextWhite
                                    )
                                }

                                DropdownMenu(
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.settings)) },
                                        onClick = {
                                            menuExpanded = false
                                            navController.navigate(Destinations.PROFILE_ROUTE)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.logout)) },
                                        onClick = {
                                            menuExpanded = false
                                            showLogoutDialog = true
                                        }
                                    )
                                }
                            }
                        }

                        if (state.booking != null) {
                            Spacer(modifier = Modifier.height(24.dp))
                            BookingBanner(state.booking)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(R.string.recommended),
                            color = TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(state.recommendedBarbershops) { barbershop ->
                                BarbershopCard(
                                    barbershopDetails = barbershop,
                                    onClick = {
                                        navController.navigate(
                                            route = "${Destinations.BOOKING_ROUTE}/barbershopId=${barbershop.uid}&barberId=null"
                                        )
                                    }
                                )
                            }
                            items(state.recommendedBarbers) { barber ->
                                BarberCard(
                                    barberDetail = barber,
                                    onClick = {
                                        navController.navigate(
                                            route = "${Destinations.BARBER_DETAIL_ROUTE}/${barber.uid}"
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultsList(
    results: List<SearchResult>,
    onItemSelected: (SearchResult) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(results) { result ->
            SearchResultItem(
                item = result,
                onClick = { onItemSelected(result) }
            )
        }
    }
}

@Composable
fun SearchResultItem(
    item: SearchResult,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon: ImageVector
        val name: String

        when (item) {
            is SearchResult.Barber -> {
                icon = Icons.Default.Person
                name = item.name
            }
            is SearchResult.Barbershop -> {
                icon = Icons.Default.Store
                name = item.name
            }
        }

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextGray
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = name,
            color = TextWhite,
            fontSize = 16.sp
        )
    }
}

@ExcludeFromJacocoGeneratedReport
class HomeScreenPreview : PreviewParameterProvider<HomeUiState> {
    override val values: Sequence<HomeUiState>
        get() = sequenceOf(
            HomeUiState(
                user = User(
                    uid = "1",
                    name = "John Snow",
                    email = "snow@email.com"
                ),
                booking = BookingDetails(
                    date = Date(),
                    barbershopName = "Barbershop Name",
                    serviceName = "Service Name"
                ),
                barbershops = listOf(),
            ),
        )
}


