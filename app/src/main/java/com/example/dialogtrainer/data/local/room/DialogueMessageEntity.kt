//DialogueMessageEntity.kt
package com.example.dialogtrainer.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single message inside a dialogue.
 */
@Entity(tableName = "dialogue_messages")
data class DialogueMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    // Foreign key to DialogueEntity.id
    val dialogueId: Long,

    // "AGENT" or "USER"
    val speaker: String,

    // Original message text
    val text: String,

    // Optional translation
    val translation: String? = null,

    // Order of message inside dialogue
    val orderIndex: Int,

    // Optional evaluation score
    val score: Int? = null,

    // Optional corrected version
    val corrected: String? = null
)
