//data/model/UserProfile.kt
package com.example.dialogtrainer.data.model

import com.example.dialogtrainer.data.model.Interest

data class UserProfile(
    val username: String = "",
    val nativeLanguage: String = "",
    val learningLanguages: List<String> = emptyList(),
    val interests: List<Interest> = emptyList(),
    val country: String = "",
    val avatarUri: String? = null,
)


