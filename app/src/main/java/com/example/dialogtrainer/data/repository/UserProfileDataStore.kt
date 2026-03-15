//data/repository/UserProfileDataStore.kt
package com.example.dialogtrainer.data.repository

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.userProfileDataStore by preferencesDataStore(name = "user_profile")
