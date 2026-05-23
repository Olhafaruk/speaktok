//DialogueDao.kt
package com.example.dialogtrainer.data.local.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for accessing dialogues and messages.
 */
@Dao
interface DialogueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDialogue(dialogue: DialogueEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<DialogueMessageEntity>)

    @Query("SELECT * FROM dialogues ORDER BY createdAt DESC")
    fun getAllDialogues(): Flow<List<DialogueEntity>>

    @Query("SELECT * FROM dialogue_messages WHERE dialogueId = :dialogueId ORDER BY orderIndex ASC")
    suspend fun getMessages(dialogueId: Long): List<DialogueMessageEntity>

    @Query("DELETE FROM dialogue_messages WHERE dialogueId = :dialogueId")
    suspend fun deleteMessages(dialogueId: Long)

    @Query("DELETE FROM dialogues WHERE id = :dialogueId")
    suspend fun deleteDialogue(dialogueId: Long)

    @Transaction
    suspend fun deleteDialogueWithMessages(dialogueId: Long) {
        deleteMessages(dialogueId)
        deleteDialogue(dialogueId)
    }
}

