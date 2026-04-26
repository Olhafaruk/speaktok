//com/example/dialogtrainer/core/AppDependencies.kt
package com.example.dialogtrainer.core

import android.content.Context
import androidx.room.Room
import com.example.dialogtrainer.BuildConfig
import com.example.dialogtrainer.data.ai.gemini.GeminiAiDialogueProvider
import com.example.dialogtrainer.data.local.room.AppDatabase
import com.example.dialogtrainer.data.repository.DialogueRepository
import com.example.dialogtrainer.data.repository.history.DialogueHistoryRepository
import com.example.dialogtrainer.data.repository.history.DialogueHistoryRepositoryImpl

object AppDependencies {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "dialogues.db"
        ).build()
    }

    val dialogueHistoryRepository: DialogueHistoryRepository by lazy {
        DialogueHistoryRepositoryImpl(
            dao = database.dialogueDao()
        )
    }

    val aiDialogueRepository: DialogueRepository by lazy {
        DialogueRepository(
            aiProvider = GeminiAiDialogueProvider(
                apiKey = BuildConfig.GEMINI_API_KEY
            )
        )
    }
}
