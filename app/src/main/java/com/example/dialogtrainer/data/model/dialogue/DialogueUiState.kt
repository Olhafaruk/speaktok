//data/model/dialogue/DialogueUiState.kt
package com.example.dialogtrainer.data.model.dialogue

data class DialogueUiState(
    val currentLine: DialogueLine? = null,
    val userAnswer: String = "",
    val feedback: Feedback? = null,
    val isLoading: Boolean = false,
    val isFinished: Boolean = false,
    val errorMessage: String? = null
)
