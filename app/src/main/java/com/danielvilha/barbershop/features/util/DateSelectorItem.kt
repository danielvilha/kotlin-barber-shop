package com.danielvilha.barbershop.features.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.CardBackground
import com.danielvilha.barbershop.ui.theme.GoldAccent
import com.danielvilha.barbershop.ui.theme.TextGray
import com.danielvilha.barbershop.ui.theme.TextWhite
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview() {
    DateSelectorItem(
        date = Calendar.getInstance(),
        isSelected = true,
        onClick = {}
    )
}

@Composable
fun DateSelectorItem(date: Calendar, isSelected: Boolean, onClick: () -> Unit) {
    val zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault())

    val dayFormatter = remember { DateTimeFormatter.ofPattern("dd").withLocale(Locale.getDefault()) }
    val weekDayFormatter = remember { DateTimeFormatter.ofPattern("EEE").withLocale(Locale.getDefault()) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GoldAccent else CardBackground
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(60.dp)
            .height(70.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weekDayFormatter.format(zonedDateTime).uppercase(),
                color = if (isSelected) Color.Black else TextGray,
                fontSize = 12.sp
            )
            Text(
                text = dayFormatter.format(zonedDateTime),
                color = if (isSelected) Color.Black else TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
