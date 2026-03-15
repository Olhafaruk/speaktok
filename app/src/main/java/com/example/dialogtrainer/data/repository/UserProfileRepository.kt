//data/repository/UserProfileRepository.kt
package com.example.dialogtrainer.data.repository

import com.example.dialogtrainer.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    val profile: Flow<UserProfile>
    suspend fun updateProfile(profile: UserProfile)
}
