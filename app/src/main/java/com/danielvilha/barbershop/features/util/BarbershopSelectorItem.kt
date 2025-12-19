package com.danielvilha.barbershop.features.util

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.danielvilha.barbershop.data.BarbershopDetails
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.GoldAccent
import com.danielvilha.barbershop.ui.theme.TextGray

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun BarbershopSelectorItemPreview() {
    BarbershopSelectorItem(
        barbershop = BarbershopDetails.createBarbershop(),
        isSelected = true,
        onClick = {}
    )
}

@Composable
fun BarbershopSelectorItem(
    barbershop: BarbershopDetails,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) GoldAccent else Color.Transparent,
                    shape = CircleShape
                )
        ) {
            AsyncImage(
                model = barbershop.imageUrl,
                contentDescription = barbershop.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = barbershop.name,
            color = if (isSelected) GoldAccent else TextGray,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}