package com.danielvilha.barbershop.features.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.danielvilha.barbershop.R
import com.danielvilha.barbershop.data.BarberDetails
import com.danielvilha.barbershop.data.BarbershopDetails
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.CardBackground
import com.danielvilha.barbershop.ui.theme.GoldAccent
import com.danielvilha.barbershop.ui.theme.TextGray
import com.danielvilha.barbershop.ui.theme.TextWhite

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(BarberCardPreview::class)
    barberDetail: BarberDetails
) {
    BarberCard(
        barberDetail = barberDetail,
        onClick = {}
    )
}

@Composable
fun BarberCard(
    barberDetail: BarberDetails,
    onClick: () -> Unit = {}
) {
    val daysOfWeek = stringArrayResource(R.array.days_of_the_week)
    val workingSchedule = remember(barberDetail.availability) {
        barberDetail.availability.mapIndexedNotNull { index, hours ->
            if (hours.isNotBlank()) {
                "${daysOfWeek.getOrElse(index) { "" }}: $hours"
            } else null
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = barberDetail.imageUrl,
                    contentDescription = barberDetail.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = barberDetail.name,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = barberDetail.about,
                        color = TextGray,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (workingSchedule.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.availability),
                    color = TextWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(workingSchedule) { time ->
                        ScheduleChip(text = time)
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleChip(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFF333333), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = GoldAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@ExcludeFromJacocoGeneratedReport
class BarberCardPreview : PreviewParameterProvider<BarberDetails> {
    override val values: Sequence<BarberDetails>
        get() = sequenceOf(
            BarberDetails(
                uid = "1",
                name = "John Doe",
                about = "Barber with a lot of experience",
                imageUrl = "https://www.google.com/maps/place/Cut+%2B+Sew+Lord+Edward",
                barbershop = BarbershopDetails(
                    uid = "1",
                    name = "Vintage Barber",
                    address = null,
                    phone = "0851657769",
                    imageUrl = "https://www.google.com/maps/place/Cut+%2B+Sew+Lord+Edward+",
                ),
                availability = listOf(
                    "",
                    "",
                    "10:00am - 07:00pm",
                    "10:30am - 07:30pm",
                    "10:20am - 07:20pm",
                    "09:00am - 07:00pm",
                    "09:00am - 05:00pm"
                ),
            )
        )
}