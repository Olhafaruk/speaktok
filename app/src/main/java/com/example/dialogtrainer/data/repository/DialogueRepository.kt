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
            sceneId = sceneId,
            nativeLanguageCode = nativeLanguageCode,
            learningLanguageCode = learningLanguageCode
        )
    }

    suspend fun evaluateAnswer(
        sceneId: String,
        previousLine: DialogueLine,
        userAnswer: String,
        learningLanguageCode: String
    ): Feedback {
        return aiProvider.evaluateUserAnswer(
            sceneId = sceneId,
            previousLine = previousLine,
            userAnswer = userAnswer,
            learningLanguageCode = learningLanguageCode
        )
    }

    suspend fun nextLine(
        sceneId: String,
        previousLine: DialogueLine,
        userAnswer: String,
        learningLanguageCode: String
    ): DialogueLine? {
        return aiProvider.generateNextLine(
            sceneId = sceneId,
            previousLine = previousLine,
            userAnswer = userAnswer,
            learningLanguageCode = learningLanguageCode
        )
    }
    suspend fun listModels(): String {
        return aiProvider.listModels()
    }
}
