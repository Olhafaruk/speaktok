//SavedDialogue.kt
package com.example.dialogtrainer.data.repository.history

data class SavedDialogue(
    val id: Long,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long,
    val languageCode: String
)
