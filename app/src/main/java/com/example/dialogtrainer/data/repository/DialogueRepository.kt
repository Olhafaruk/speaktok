//data/repository/DialogueRepository.kt
package com.example.dialogtrainer.data.repository

import com.example.dialogtrainer.data.ai.AiDialogueProvider
import com.example.dialogtrainer.data.model.dialogue.DialogueLine
import com.example.dialogtrainer.data.model.dialogue.Feedback

class DialogueRepository(
    private val aiProvider: AiDialogueProvider
) {

    suspend fun startDialogue(
        sceneId: String,
        nativeLanguageCode: String,
        learningLanguageCode: String
    ): DialogueLine {
        return aiProvider.generateFirstLine(
            sceneId,
            nativeLanguageCode,
            learningLanguageCode
        )
    }

    suspend fun evaluateAnswer(
        sceneId: String,
        previousLine: DialogueLine,
        userAnswer: String,
        learningLanguageCode: String
    ): Feedback {
        return aiProvider.evaluateUserAnswer(
            sceneId,
            previousLine,
            userAnswer,
            learningLanguageCode
        )
    }

    suspend fun nextLine(
        sceneId: String,
        previousLine: DialogueLine,
        userAnswer: String,
        learningLanguageCode: String,
        nativeLanguageCode: String
    ): DialogueLine? {
        return aiProvider.generateNextLine(
            sceneId,
            previousLine,
            userAnswer,
            learningLanguageCode,
            nativeLanguageCode
        )
    }

    suspend fun listModels(): String {
        return aiProvider.listModels()
    }
}

