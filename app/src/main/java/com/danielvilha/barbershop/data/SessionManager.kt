package com.danielvilha.barbershop.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    val userFlow: Flow<User?> = dataStore.data.map { preferences ->
        val uid = preferences[PreferencesKeys.USER_UID]
        if (uid != null) {
            User(
                uid = uid,
                name = preferences[PreferencesKeys.USER_NAME] ?: "",
                email = preferences[PreferencesKeys.USER_EMAIL] ?: "",
                birthdate = preferences[PreferencesKeys.USER_BIRTHDATE],
                phone = preferences[PreferencesKeys.USER_PHONE],
                ddi = preferences[PreferencesKeys.USER_DDI],
                photoUrl = preferences[PreferencesKeys.USER_PHOTO_URL]
            )
        } else {
            null
        }
    }

    private object PreferencesKeys {
        val USER_UID = stringPreferencesKey("user_uid")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_BIRTHDATE = stringPreferencesKey("user_birthdate")
        val USER_PHONE = stringPreferencesKey("user_phone")
        val USER_DDI = stringPreferencesKey("user_ddi")
        val USER_PHOTO_URL = stringPreferencesKey("user_photo_url")
    }

    suspend fun saveUserSession(user: User) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_UID] = user.uid
            preferences[PreferencesKeys.USER_NAME] = user.name
            preferences[PreferencesKeys.USER_EMAIL] = user.email
            preferences[PreferencesKeys.USER_BIRTHDATE] = user.birthdate ?: ""
            preferences[PreferencesKeys.USER_PHONE] = user.phone ?: ""
            preferences[PreferencesKeys.USER_DDI] = user.ddi ?: ""
            preferences[PreferencesKeys.USER_PHOTO_URL] = user.photoUrl ?: ""
        }
    }

    suspend fun clearUserSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}