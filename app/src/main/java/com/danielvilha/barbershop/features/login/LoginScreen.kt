package com.danielvilha.barbershop.features.login

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielvilha.barbershop.R
import com.danielvilha.barbershop.features.util.OrHorizontalDivider
import com.danielvilha.barbershop.features.util.SignInButton
import com.danielvilha.barbershop.features.util.ProgressIndicator
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.BarberShopTheme
import com.danielvilha.barbershop.ui.theme.CardBackground
import com.danielvilha.barbershop.ui.theme.TextGray
import com.danielvilha.barbershop.ui.theme.TextWhite

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(LoginScreenPreview::class)
    state: LoginUiState
) {
    BarberShopTheme {
        LoginScreen(
            state = state,
            onEvent = {},
            onHome = {},
        )
    }
}

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    onHome: () -> Unit,
) {
    val activity = LocalActivity.current as Activity
    val context = LocalContext.current

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()

            onHome()
        }
    }

    LaunchedEffect(key1 = state.error) {
        state.error?.let {
            Toast.makeText(
                context,
                it,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Row {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CardBackground)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isLoading -> ProgressIndicator()
                else -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(140.dp),
                        tint = Color.Unspecified
                    )

                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Text(
                        text = stringResource(R.string.schedule_in_seconds),
                        fontSize = 16.sp,
                        color = TextGray,
                        modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
                    )

                    Spacer(modifier = Modifier.height(100.dp))

                    SignInButton(
                        onEvent = { onEvent(LoginUiEvent.OnGoogleSignInClick(activity)) },
                        painter = painterResource(R.drawable.ic_google_logo),
                        text = stringResource(R.string.login_with_google)
                    )
                    OrHorizontalDivider()
                    SignInButton(
                        onEvent = { onEvent(LoginUiEvent.OnFacebookSignInClick(activity)) },
                        painter = painterResource(R.drawable.ic_facebook_logo),
                        text = stringResource(R.string.login_with_facebook)
                    )

                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
class LoginScreenPreview : PreviewParameterProvider<LoginUiState> {
    override val values: Sequence<LoginUiState>
        get() = sequenceOf(
            LoginUiState(
                isSignInSuccessful = true,
                isLoading = false,
                error = "Test Error",
            )
        )
}