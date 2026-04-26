//ui/dialogue/DialogueViewModel.kt
package com.example.dialogtrainer.ui.dialogue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dialogtrainer.data.model.dialogue.DialogueLine
import com.example.dialogtrainer.data.model.dialogue.DialogueUiState
import com.example.dialogtrainer.data.repository.DialogueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DialogueViewModel(
    private val repository: DialogueRepository,
    private val sceneId: String?,
    private val nativeLanguageCode: String,
    private val learningLanguageCode: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(DialogueUiState(isLoading = true))
    val uiState: StateFlow<DialogueUiState> = _uiState

    private var lastAgentLine: DialogueLine? = null

    init {
        startDialogue()
    }

    private fun startDialogue() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val firstLine = repository.startDialogue(
                    sceneId ?: "",
                    nativeLanguageCode,
                    learningLanguageCode
                )

                lastAgentLine = firstLine

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentLine = firstLine,
                    userAnswer = "",
                    isFinished = false,
                    feedback = null,
                    errorMessage = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
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
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val feedback = repository.evaluateAnswer(
                    sceneId ?: "",
                    agentLine,
                    answer,
                    learningLanguageCode
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    feedback = feedback
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun onNext() {
        val agentLine = lastAgentLine ?: return
        val answer = _uiState.value.userAnswer.trim()
        if (answer.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val nextLine = repository.nextLine(
                    sceneId ?: "",
                    agentLine,
                    answer,
                    learningLanguageCode,
                    nativeLanguageCode
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
                    errorMessage = e.message
                )
            }
        }
    }

    fun finishDialogue() {
        _uiState.update { state ->
            state.copy(
                isFinished = true,
                feedback = null,
                userAnswer = ""
            )
        }
    }

}
