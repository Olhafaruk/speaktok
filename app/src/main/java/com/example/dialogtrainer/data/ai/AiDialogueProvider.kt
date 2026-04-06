//data/ai/gemini/AiDialogueProvider.kt
package com.example.dialogtrainer.data.ai

import com.example.dialogtrainer.data.model.dialogue.DialogueLine
import com.example.dialogtrainer.data.model.dialogue.Feedback

interface AiDialogueProvider {

    suspend fun generateFirstLine(
        sceneId: String,
        nativeLanguageCode: String,
        learningLanguageCode: String
    ): DialogueLine

    suspend fun evaluateUserAnswer(
        sceneId: String,
        previousLine: DialogueLine,
        userAnswer: String,
        learningLanguageCode: String
    ): Feedback

    suspend fun generateNextLine(
        sceneId: String,
        previousLine: DialogueLine,
        userAnswer: String,
        learningLanguageCode: String
    ): DialogueLine?

    suspend fun listModels(): String
}
