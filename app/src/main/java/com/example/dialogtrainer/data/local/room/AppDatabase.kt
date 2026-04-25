//AppDatabase.kt
package com.example.dialogtrainer.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Main Room database for storing dialogues.
 */
@Database(
    entities = [
        DialogueEntity::class,
        DialogueMessageEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dialogueDao(): DialogueDao
}
