//com/example/dialogtrainer/core/AppDependencies.kt
package com.example.dialogtrainer.core

import android.content.Context
import androidx.room.Room
import com.example.dialogtrainer.BuildConfig
import com.example.dialogtrainer.data.ai.AiDialogueProvider
import com.example.dialogtrainer.data.ai.gemini.GeminiAiDialogueProvider
import com.example.dialogtrainer.data.local.room.AppDatabase
import com.example.dialogtrainer.data.local.room.DialogueDao
import com.example.dialogtrainer.data.repository.DialogueRepository
import com.example.dialogtrainer.data.repository.history.DialogueHistoryRepository
import com.example.dialogtrainer.data.repository.history.DialogueHistoryRepositoryImpl

object AppDependencies {

    lateinit var appContext: Context

    // Gemini provider
    private val aiProvider: AiDialogueProvider by lazy {
        GeminiAiDialogueProvider(
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }

    // Repository for generating dialogues (Gemini)
    val aiDialogueRepository: DialogueRepository by lazy {
        DialogueRepository(aiProvider)
    }

    // Room database
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "dialogues.db"
        ).build()
    }

    val dialogueDao: DialogueDao by lazy {
        database.dialogueDao()
    }

    // Repository for saving/loading dialogue history (Room)
    val dialogueHistoryRepository: DialogueHistoryRepository by lazy {
        DialogueHistoryRepositoryImpl(dialogueDao)
    }
}
