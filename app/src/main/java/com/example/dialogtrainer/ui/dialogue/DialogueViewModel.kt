//ui/dialogue/DialogueViewModel.kt
package com.example.dialogtrainer.ui.dialogue

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dialogtrainer.data.model.dialogue.DialogueLine
import com.example.dialogtrainer.data.model.dialogue.DialogueUiState
import com.example.dialogtrainer.data.model.dialogue.Speaker
import com.example.dialogtrainer.data.repository.DialogueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DialogueViewModel(
    private val repository: DialogueRepository,
    private val sceneId: String,
    private val nativeLanguageCode: String,
    private val learningLanguageCode: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(DialogueUiState(isLoading = true))
    val uiState: StateFlow<DialogueUiState> = _uiState

    private var lastAgentLine: DialogueLine? = null

    init {

        viewModelScope.launch {
            try {
                val models = repository.listModels()
                Log.d("GEMINI_MODELS", models)
            } catch (e: Exception) {
                Log.e("GEMINI_MODELS", "Error listing models: ${e.message}")
            }
        }


        startDialogue()
    }

    private fun startDialogue() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                feedback = null
            )

            try {
                val firstLine = repository.startDialogue(
                    sceneId = sceneId,
                    nativeLanguageCode = nativeLanguageCode,
                    learningLanguageCode = learningLanguageCode
                )
                lastAgentLine = firstLine
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentLine = firstLine,
                    userAnswer = "",
                    feedback = null,
                    isFinished = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to start dialogue"
                )
            }
        }
    }

    fun onUserAnswerChange(newText: String) {
        _uiState.value = _uiState.value.copy(userAnswer = newText)
    }

    fun onSendAnswer() {
        val agentLine = lastAgentLine ?: return
        val answer = _uiState.value.userAnswer.trim()
        if (answer.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val feedback = repository.evaluateAnswer(
                    sceneId = sceneId,
                    previousLine = agentLine,
                    userAnswer = answer,
                    learningLanguageCode = learningLanguageCode
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    feedback = feedback
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to evaluate answer"
                )
            }
        }
    }

    fun onNext() {
        val agentLine = lastAgentLine ?: return
        val answer = _uiState.value.userAnswer.trim()
        if (answer.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val nextLine = repository.nextLine(
                    sceneId = sceneId,
                    previousLine = agentLine,
                    userAnswer = answer,
                    learningLanguageCode = learningLanguageCode
                )

                if (nextLine == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isFinished = true
                    )
                } else {
                    lastAgentLine = nextLine
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        currentLine = nextLine,
                        userAnswer = "",
                        feedback = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to get next line"
                )
            }
        }
    }
}
