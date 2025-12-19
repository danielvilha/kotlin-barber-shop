package com.danielvilha.barbershop.features.details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.danielvilha.barbershop.Destinations
import com.danielvilha.barbershop.R
import com.danielvilha.barbershop.data.Barber
import com.danielvilha.barbershop.data.Barber.Companion.createBarber
import com.danielvilha.barbershop.features.util.ProgressIndicator
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.BarberShopTheme
import com.danielvilha.barbershop.ui.theme.DarkBackground
import com.danielvilha.barbershop.ui.theme.GoldAccent
import com.danielvilha.barbershop.ui.theme.TextGray
import com.danielvilha.barbershop.ui.theme.TextWhite

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(DetailScreenPreview ::class)
    state: BarberDetailUiState
) {
    BarberShopTheme {
        DetailScreen(
            state = state,
            navController = NavController(LocalContext.current),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailScreen(
    state: BarberDetailUiState,
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.barber?.name ?: stringResource(id = R.string.details)) },
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
        containerColor = DarkBackground
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ProgressIndicator()
                }
            }
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.error, color = Color.Red, modifier = Modifier.padding(16.dp))
                }
            }
            state.barber != null -> {
                BarberDetailsContent(
                    barber = state.barber,
                    navController = navController,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun BarberDetailsContent(
    barber: Barber,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val daysOfWeek = stringArrayResource(R.array.days_of_the_week)
    val workingSchedule = remember(barber.availability) {
        barber.availability.mapIndexedNotNull { index, hours ->
            if (hours.isNotBlank()) {
                daysOfWeek.getOrElse(index) { "" } to hours
            } else null
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            AsyncImage(
                model = barber.imageUrl,
                contentDescription = stringResource(R.string.photo_by, barber.name),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(190.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_barber_user),
                error = painterResource(id = R.drawable.ic_barber_user)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = barber.name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.about),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = barber.about,
                    fontSize = 14.sp,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.availability),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        items(workingSchedule) { (day, hours) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = day,
                    color = GoldAccent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(14.sp.value.dp)
                ) {
                    val pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(4f, 8f),
                        phase = 0f
                    )

                    drawLine(
                        color = GoldAccent,
                        start = androidx.compose.ui.geometry.Offset(0f, size.height / 2),
                        end = androidx.compose.ui.geometry.Offset(size.width, size.height / 2),
                        pathEffect = pathEffect,
                        strokeWidth = 2f
                    )
                }
                Text(
                    text = hours,
                    color = GoldAccent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                navController.navigate(
                    route = "${Destinations.BOOKING_ROUTE}/barbershopId=${barber.barbershopUid}&barberId=${barber.uid}"
                ) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.book_now),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
class DetailScreenPreview : PreviewParameterProvider<BarberDetailUiState> {
    override val values: Sequence<BarberDetailUiState>
        get() = sequenceOf(
            BarberDetailUiState(
                barber = Barber.createBarber(),
                isLoading = false
            ),
        )
}