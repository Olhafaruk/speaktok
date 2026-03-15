//data/model/UserProfile.kt
package com.example.dialogtrainer.data.model

data class UserProfile(
    val username: String = "",
    val nativeLanguage: String = "",
    val learningLanguages: List<String> = emptyList(),
    val interests: List<String> = emptyList(),
    val country: String = ""
)


