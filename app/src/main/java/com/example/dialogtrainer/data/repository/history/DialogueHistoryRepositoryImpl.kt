//DialogueHistoryRepositoryImpl.kt
package com.example.dialogtrainer.data.repository.history

import com.example.dialogtrainer.data.local.room.DialogueDao
import com.example.dialogtrainer.data.local.room.DialogueEntity
import com.example.dialogtrainer.data.local.room.DialogueMessageEntity
import com.example.dialogtrainer.data.model.dialogue.DialogueLine
import com.example.dialogtrainer.data.model.dialogue.Speaker

class DialogueHistoryRepositoryImpl(
    private val dao: DialogueDao
) : DialogueHistoryRepository {

    override suspend fun saveDialogue(
        title: String,
        createdAt: Long,
        updatedAt: Long,
        languageCode: String,
        messages: List<DialogueLine>
    ): Long {

        val dialogueId = dao.insertDialogue(
            DialogueEntity(
                title = title,
                createdAt = createdAt,
                updatedAt = updatedAt,
                languageCode = languageCode
            )
        )

        val entities = messages.mapIndexed { index, msg ->
            DialogueMessageEntity(
                dialogueId = dialogueId,
                speaker = msg.speaker.name,
                text = msg.text,
                translation = msg.translation,
                orderIndex = index
            )
        }

        dao.insertMessages(entities)

        return dialogueId
    }

    override suspend fun getAllDialogues(): List<SavedDialogue> =
        dao.getAllDialogues().map {
            SavedDialogue(
                id = it.id,
                title = it.title,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                languageCode = it.languageCode
            )
        }

    override suspend fun getDialogueMessages(dialogueId: Long): List<DialogueLine> =
        dao.getMessages(dialogueId).map {
            DialogueLine(
                speaker = Speaker.valueOf(it.speaker),
                text = it.text,
                translation = it.translation
            )
        }

    override suspend fun deleteDialogue(dialogueId: Long) {
        dao.deleteDialogueWithMessages(dialogueId)
    }
}
