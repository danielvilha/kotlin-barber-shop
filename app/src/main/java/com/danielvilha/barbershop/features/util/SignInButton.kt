package com.danielvilha.barbershop.features.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielvilha.barbershop.R
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.TextWhite

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview() {
    Column {
        SignInButton(
            onEvent = {},
            painter = painterResource(id = R.drawable.ic_google_logo),
            text = "Login Google",
        )
        Spacer(modifier = Modifier.padding(8.dp))
        SignInButton(
            onEvent = {},
            painter = painterResource(id = R.drawable.ic_facebook_logo),
            text = "Login Facebook",
        )
    }
}

@Composable
fun SignInButton(
    onEvent: () -> Unit,
    painter: Painter,
    text: String
) {
    Button(
        onClick = { onEvent() },
        modifier = Modifier
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = TextWhite,
            contentColor = Color(0xFFF2F2F2)
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painter,
                contentDescription = stringResource(R.string.google_logo),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F1F1F)
            )
        }
    }
}