//data/repository/UserProfileRepositoryImpl.kt
package com.example.dialogtrainer.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.dialogtrainer.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProfileRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserProfileRepository {

    private object Keys {
        val USERNAME = stringPreferencesKey("username")
        val NATIVE_LANGUAGE = stringPreferencesKey("native_language")
        val LEARNING_LANGUAGES = stringPreferencesKey("learning_languages")
        val INTERESTS = stringPreferencesKey("interests")
        val COUNTRY = stringPreferencesKey("country")
    }

    override val profile: Flow<UserProfile> =
        dataStore.data.map { prefs ->
            UserProfile(
                username = prefs[Keys.USERNAME] ?: "",
                nativeLanguage = prefs[Keys.NATIVE_LANGUAGE] ?: "",
                learningLanguages = prefs[Keys.LEARNING_LANGUAGES]?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
                interests = prefs[Keys.INTERESTS]?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
                country = prefs[Keys.COUNTRY] ?: ""
            )
        }

    override suspend fun updateProfile(profile: UserProfile) {
        dataStore.edit { prefs ->
            prefs[Keys.USERNAME] = profile.username
            prefs[Keys.NATIVE_LANGUAGE] = profile.nativeLanguage
            prefs[Keys.LEARNING_LANGUAGES] = profile.learningLanguages.joinToString(",")
            prefs[Keys.INTERESTS] = profile.interests.joinToString(",")
            prefs[Keys.COUNTRY] = profile.country
        }
    }
}
