package com.danielvilha.barbershop.features.booking

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.danielvilha.barbershop.R
import com.danielvilha.barbershop.data.Barber
import com.danielvilha.barbershop.data.Barber.Companion.createBarber
import com.danielvilha.barbershop.data.Barber.Companion.createBarbers
import com.danielvilha.barbershop.data.BarbershopDetails
import com.danielvilha.barbershop.data.Service
import com.danielvilha.barbershop.data.Service.Companion.createService
import com.danielvilha.barbershop.data.Service.Companion.createServices
import com.danielvilha.barbershop.features.util.BarberSelectorItem
import com.danielvilha.barbershop.features.util.BarbershopSelectorItem
import com.danielvilha.barbershop.features.util.DateSelectorItem
import com.danielvilha.barbershop.features.util.TimeSlotItem
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.BarberShopTheme
import com.danielvilha.barbershop.ui.theme.DarkBackground
import com.danielvilha.barbershop.ui.theme.GoldAccent
import com.danielvilha.barbershop.ui.theme.TextGray
import com.danielvilha.barbershop.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(BookingScreenPreview::class)
    state: BookingUiState
) {
    BarberShopTheme {
        BookingScreen(
            state = state,
            navController = NavController(LocalContext.current)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    state: BookingUiState,
    onEvent: (BookingUiEvent) -> Unit = {},
    navController: NavController,
) {
    var selectedDate by remember(state.selectedDate) { mutableStateOf(state.selectedDate ?: Calendar.getInstance()) }
    var selectedTime by remember { mutableStateOf(state.selectedTime) }

    val dates = remember(state.selectedBarber) {
        val barber = state.selectedBarber
        if (barber == null) {
            emptyList<Calendar>()
        } else {
            val availableDates = mutableListOf<Calendar>()
            val cal = Calendar.getInstance()
            val daysToScan = 30

            repeat(daysToScan) {
                val dayOfWeekIndex = cal.get(Calendar.DAY_OF_WEEK) - 1

                val availabilityString = barber.availability.getOrElse(dayOfWeekIndex) { "" }

                if (availabilityString.isNotBlank()) {
                    availableDates.add(cal.clone() as Calendar)
                }

                cal.add(Calendar.DAY_OF_YEAR, 1)
            }
            availableDates
        }
    }

    LaunchedEffect(dates) {
        if (selectedDate !in dates && dates.isNotEmpty()) {
            selectedDate = dates.first()
            selectedTime = null
        }
    }

    LaunchedEffect(state.bookingConfirmed) {
        if (state.bookingConfirmed) {
            navController.previousBackStackEntry?.savedStateHandle?.set("booking_updated", true)
            navController.popBackStack()
        }
    }

    val availableSlots = remember(state.selectedBarber, selectedDate) {
        generateSlotsForDate(state.selectedBarber, selectedDate)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.schedule_appointment)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextWhite,
                    navigationIconContentColor = TextWhite
                )
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (state.selectedBarber != null && selectedTime != null) {
                        onEvent(
                            BookingUiEvent.OnBookingConfirmed(
                                state.selectedBarber,
                                selectedDate,
                                selectedTime!!
                            )
                        )
                    }
                },
                enabled = state.selectedBarber != null && selectedTime != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldAccent,
                    disabledContainerColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 18.dp)
                    .height(56.dp)
            ) {
                Text(text = stringResource(id = R.string.confirm_appointment), color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color(0xFF0F0F0F)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(stringResource(id = R.string.choose_barbershop), color = TextGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.allBarbershops) { barbershop ->
                    BarbershopSelectorItem(
                        barbershop = barbershop,
                        isSelected = barbershop.uid == state.selectedBarbershop?.uid,
                        onClick = {
                            onEvent(BookingUiEvent.OnBarbershopSelected(barbershop))
                            selectedTime = null
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(stringResource(id = R.string.choose_barber), color = TextGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.availableBarbers) { barber ->
                    BarberSelectorItem(
                        barber = barber,
                        isSelected = barber.uid == state.selectedBarber?.uid,
                        onClick = {
                            onEvent(BookingUiEvent.OnBarberSelected(barber))
                            selectedTime = null
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(stringResource(id = R.string.choose_date), color = TextGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(dates) { date ->
                    DateSelectorItem(
                        date = date,
                        isSelected = isSameDay(date, selectedDate),
                        onClick = {
                            selectedDate = date
                            selectedTime = null
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(stringResource(R.string.available_time), color = TextGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            if (state.selectedBarber?.availability?.isEmpty() == true) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.error_scheduled_times), color = TextGray)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableSlots) { slot ->
                        TimeSlotItem(
                            time = slot,
                            isSelected = slot == selectedTime,
                            onClick = { selectedTime = slot }
                        )
                    }
                }
            }
        }
    }
}

private fun generateSlotsForDate(barber: Barber?, date: Calendar?): List<String> {
    if (barber == null || date == null) return emptyList()

    val dayOfWeekIndex = date.get(Calendar.DAY_OF_WEEK) - 1
    val availabilityString = barber.availability.getOrElse(dayOfWeekIndex) { "" }

    if (availabilityString.isBlank()) return emptyList()

    val timeParts = availabilityString.split(" - ")
    if (timeParts.size != 2) return emptyList()

    val timeFormat = SimpleDateFormat("hh:mma", Locale.getDefault())

    try {
        val startTimeString = timeParts[0]
        val endTimeString = timeParts[1]

        val startCal = Calendar.getInstance().apply {
            time = timeFormat.parse(startTimeString)!!
        }
        val endCal = Calendar.getInstance().apply {
            time = timeFormat.parse(endTimeString)!!
        }

        val availableSlots = mutableListOf<String>()
        val currentSlotCal = startCal.clone() as Calendar

        while (currentSlotCal.before(endCal)) {
            val slotTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentSlotCal.time)
            availableSlots.add(slotTime)

            currentSlotCal.add(Calendar.HOUR_OF_DAY, 1)
        }

        return availableSlots

    } catch (e: Exception) {
         Log.e("SlotGenerator", "Error parsing availability: $availabilityString", e)
        return emptyList()
    }
}

private fun isSameDay(cal1: Calendar, cal2: Calendar?): Boolean {
    if (cal2 == null) return false
    return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
}

@ExcludeFromJacocoGeneratedReport
class BookingScreenPreview : PreviewParameterProvider<BookingUiState> {
    override val values: Sequence<BookingUiState>
        get() = sequenceOf(
            BookingUiState(
                allBarbershops = BarbershopDetails.createBarbershops(),
                availableServices = Service.createServices(),
                availableBarbers = Barber.createBarbers(),
                selectedBarbershop = BarbershopDetails.createBarbershop(),
                selectedService = Service.createService(),
                selectedBarber = Barber.createBarber(),
                selectedDate = null,
                selectedTime = null,
            )
        )
}