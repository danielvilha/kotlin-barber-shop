package com.danielvilha.barbershop.features.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.danielvilha.barbershop.R
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.TextGray

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview() {
    Box {
        OrHorizontalDivider()
    }
}

@Composable
fun OrHorizontalDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(top = 32.dp, bottom = 32.dp)
        )

        Text(
            text = stringResource(R.string.or),
            modifier = Modifier.padding(horizontal = 8.dp),
            color = TextGray
        )

        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
        )
    }
}