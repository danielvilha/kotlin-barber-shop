package com.danielvilha.barbershop.features.login

import android.app.Activity

sealed class LoginUiEvent {
    data class OnGoogleSignInClick(val activity: Activity): LoginUiEvent()
    data class OnFacebookSignInClick(val activity: Activity): LoginUiEvent()
}