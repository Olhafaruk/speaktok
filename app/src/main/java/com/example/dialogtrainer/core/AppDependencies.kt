//com/example/dialogtrainer/core/AppDependencies.kt
package com.example.dialogtrainer.core

import com.example.dialogtrainer.data.ai.AiDialogueProvider
import com.example.dialogtrainer.data.ai.gemini.GeminiAiDialogueProvider
import com.example.dialogtrainer.data.repository.DialogueRepository
import com.example.dialogtrainer.BuildConfig

object AppDependencies {

    private val aiProvider: AiDialogueProvider by lazy {
        GeminiAiDialogueProvider(
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }

    val dialogueRepository: DialogueRepository by lazy {
        DialogueRepository(aiProvider)
    }
}
