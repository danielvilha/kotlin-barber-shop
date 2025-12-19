package com.danielvilha.barbershop.features.profile

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.danielvilha.barbershop.R
import com.danielvilha.barbershop.data.User
import com.danielvilha.barbershop.features.util.ProgressIndicator
import com.danielvilha.barbershop.features.util.RawCountry.Companion.getCountries
import com.danielvilha.barbershop.features.util.RawCountry.Companion.getDeviceCountryIso
import com.danielvilha.barbershop.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.barbershop.ui.preview.LightDarkPreview
import com.danielvilha.barbershop.ui.theme.BarberShopTheme
import com.danielvilha.barbershop.ui.theme.DarkBackground
import com.danielvilha.barbershop.ui.theme.GoldAccent
import com.danielvilha.barbershop.ui.theme.TextGray
import com.danielvilha.barbershop.ui.theme.TextWhite

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(ProfileScreenPreview::class)
    state: ProfileUiState
) {
    BarberShopTheme {
        ProfileScreen(
            state = state,
            navController = NavController(LocalContext.current),
            onEvent = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileUiState,
    navController: NavController,
    onEvent: (ProfileUiEvent) -> Unit
) {
    val context = LocalContext.current
    val countries = remember { getCountries() }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                onEvent(ProfileUiEvent.OnPhotoChanged(it))
            }
        }
    )

    var selectedCountry = remember(state.user?.ddi) {
        val phone = state.user?.ddi
        val deviceIso = getDeviceCountryIso(context)

        val countryFromPhone = if (!phone.isNullOrBlank()) {
            countries.firstOrNull { phone.startsWith(it.code) }
        } else null

        val countryFromDevice = countries.find {
            it.code.equals(deviceIso, ignoreCase = true)
        }

        countryFromPhone ?: countryFromDevice ?: countries.first()
    }

    val phoneDisplayValue = remember(state.user?.phone, selectedCountry) {
        val phone = state.user?.phone
        if (!phone.isNullOrBlank() && phone.startsWith(selectedCountry.code)) {
            phone.removePrefix(selectedCountry.code)
        } else {
            phone ?: ""
        }
    }

    var isCountryDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.profile)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextWhite,
                    navigationIconContentColor = TextWhite
                )
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ProgressIndicator()
                }
            }
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.error, color = Color.Red, modifier = Modifier.padding(16.dp))
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(GoldAccent)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                    ) {
                        AsyncImage(
                            model = state.user?.photoUrl,
                            contentDescription = "Profile Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.ic_placeholder_person),
                            error = painterResource(id = R.drawable.ic_placeholder_person)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = state.user?.name ?: "",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.user?.email ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = phoneDisplayValue,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                onEvent(ProfileUiEvent.OnPhoneNumberChanged(selectedCountry.code, newValue))
                            }
                        },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = TextGray,
                            focusedLabelColor = GoldAccent,
                            unfocusedLabelColor = TextGray
                        ),
                        leadingIcon = {
                            Box {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(start = 8.dp, end = 4.dp)
                                        .clickable { isCountryDropdownExpanded = true }
                                ) {
                                    Text(
                                        text = "${selectedCountry.flag} ${selectedCountry.code}",
                                        color = TextWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Select Country",
                                        tint = TextWhite
                                    )
                                }

                                DropdownMenu(
                                    expanded = isCountryDropdownExpanded,
                                    onDismissRequest = { isCountryDropdownExpanded = false }
                                ) {
                                    countries.forEach { country ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(text = "${country.flag} ${country.name} (${country.code})")
                                            },
                                            onClick = {
                                                selectedCountry = country
                                                isCountryDropdownExpanded = false
                                                onEvent(
                                                    ProfileUiEvent.OnPhoneNumberChanged(
                                                        country.code,
                                                        phoneDisplayValue
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onEvent(ProfileUiEvent.OnSaveChanges) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldAccent,
                            disabledContainerColor = Color.DarkGray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(text = stringResource(R.string.save_changes))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = stringResource(R.string.app_version, "${getAppVersion(context)}"),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

private fun getAppVersion(context: Context): String? {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName
    } catch (e: Exception) {
        Log.e("ProfileScreen", "getAppVersion: ", e.cause)
        "N/A"
    }
}

@ExcludeFromJacocoGeneratedReport
class ProfileScreenPreview : PreviewParameterProvider<ProfileUiState> {
    override val values: Sequence<ProfileUiState>
        get() = sequenceOf(
            ProfileUiState(
                isLoading = false,
                user = User.getUser(),
                error = null,
                saveSuccess = false
            )
        )
}