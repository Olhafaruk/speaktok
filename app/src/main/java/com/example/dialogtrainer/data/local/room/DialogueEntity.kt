//DialogueEntity.kt
package com.example.dialogtrainer.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a saved dialogue metadata.
 * Stored in Room database.
 */
@Entity(tableName = "dialogues")
data class DialogueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    // Auto-generated title from Gemini, e.g. "Ordering food — 2026-04-19"
    val title: String,

    // Unix timestamp
    val createdAt: Long,

    // Unix timestamp
    val updatedAt: Long,

    // ISO language code, e.g. "de", "en"
    val languageCode: String,

    // Optional scene identifier (null for open dialogues)
    val sceneId: String? = null
)
