package com.danielvilha.barbershop.features.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.danielvilha.barbershop.R
import com.danielvilha.barbershop.data.BarbershopDetails
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.CardBackground
import com.danielvilha.barbershop.ui.theme.TextGray
import com.danielvilha.barbershop.ui.theme.TextWhite

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(BarbershopCardPreview::class)
    barbershopDetails: BarbershopDetails
) {
    BarbershopCard(barbershopDetails)
}

@Composable
fun BarbershopCard(
    barbershopDetails: BarbershopDetails,
    onClick: () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.DarkGray)
            ) {
                AsyncImage(
                    model = barbershopDetails.imageUrl,
                    contentDescription = barbershopDetails.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Black.copy(alpha = 0.7f),
                                ),
                                startY = 0f
                            )
                        )
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = barbershopDetails.name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = barbershopDetails.displayServices, color = TextGray, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = CardBackground),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, Color(0xFF333333))
                ) {
                    Text(text = stringResource(R.string.book), color = TextWhite)
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
class BarbershopCardPreview : PreviewParameterProvider<BarbershopDetails> {
    override val values: Sequence<BarbershopDetails>
        get() = sequenceOf(BarbershopDetails.createBarbershop())
}
