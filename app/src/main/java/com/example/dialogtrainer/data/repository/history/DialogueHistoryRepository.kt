//data/repository/history/DialogueHistoryRepository.kt
package com.example.dialogtrainer.data.repository.history

import com.example.dialogtrainer.data.model.dialogue.DialogueLine

interface DialogueHistoryRepository {

    suspend fun saveDialogue(
        title: String,
        createdAt: Long,
        updatedAt: Long,
        languageCode: String,
        messages: List<DialogueLine>
    ): Long

    suspend fun getAllDialogues(): List<SavedDialogue>

    suspend fun getDialogueMessages(dialogueId: Long): List<DialogueLine>

    suspend fun deleteDialogue(dialogueId: Long)
}
