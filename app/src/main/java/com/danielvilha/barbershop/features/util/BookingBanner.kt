package com.danielvilha.barbershop.features.util

import android.icu.util.Calendar
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.danielvilha.barbershop.R
import com.danielvilha.barbershop.data.BookingDetails
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.CardBackground
import com.danielvilha.barbershop.ui.theme.TextGray
import com.danielvilha.barbershop.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(BookingBannerPreview::class)
    booking: BookingDetails
) {
    BookingBanner(booking)
}

@Composable
fun BookingBanner(booking: BookingDetails) {
    val formattedDateString = formatBookingDate(booking.date)
    val bannerText = "$formattedDateString - ${booking.barbershopName}"

    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            ) {
                AsyncImage(
                    model = "",
                    contentDescription = stringResource(
                        id = R.string.photo_by,
                        booking.barbershopName
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .height(48.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = booking.serviceName,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = bannerText,
                    color = TextGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun formatBookingDate(date: java.util.Date): String {    val bookingCalendar =
    Calendar.getInstance().apply { time = date }
    val todayCalendar = Calendar.getInstance()

    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormatter.format(date)

    if (bookingCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
        bookingCalendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR)) {
        return stringResource(id = R.string.today_at, timeString)
    }

    todayCalendar.add(Calendar.DAY_OF_YEAR, 1)
    if (bookingCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
        bookingCalendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR)) {
        return stringResource(id = R.string.tomorrow_at, timeString) // "Amanhã, 16:00"
    }

    val dayOfWeekFormatter = SimpleDateFormat("EEEE", Locale.getDefault())
    val dayOfWeekString = dayOfWeekFormatter.format(date)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    return stringResource(id = R.string.day_of_week_at, dayOfWeekString, timeString) // "Quinta-feira, 16:00"
}

@ExcludeFromJacocoGeneratedReport
class BookingBannerPreview : PreviewParameterProvider<BookingDetails> {
    override val values: Sequence<BookingDetails>
        get() = sequenceOf(
            BookingDetails(
                date = java.util.Date(),
                barbershopName = "Barbershop Name",
                serviceName = "Service Name"
            )
        )
}