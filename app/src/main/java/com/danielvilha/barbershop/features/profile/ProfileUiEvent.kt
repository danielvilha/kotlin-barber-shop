package com.danielvilha.barbershop.features.profile

import android.net.Uri

sealed class ProfileUiEvent {
    data class OnPhoneNumberChanged(val ddi: String, val phone: String) : ProfileUiEvent()
    data class OnPhotoChanged(val photoUri: Uri) : ProfileUiEvent()
    object OnSaveChanges : ProfileUiEvent()
}