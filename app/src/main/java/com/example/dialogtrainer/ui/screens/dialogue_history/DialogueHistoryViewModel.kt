//app/ui/screens/dialogue_history/DialogueHistoryViewModel.kt
package com.example.dialogtrainer.ui.screens.dialogue_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dialogtrainer.data.model.dialogue.DialogueLine
import com.example.dialogtrainer.data.repository.history.DialogueHistoryRepository
import com.example.dialogtrainer.data.repository.history.SavedDialogue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DialogueHistoryViewModel(
    private val dialogueId: Long,
    private val repository: DialogueHistoryRepository
) : ViewModel() {

    private val _dialogue = MutableStateFlow<SavedDialogue?>(null)
    val dialogue: StateFlow<SavedDialogue?> = _dialogue

    private val _messages = MutableStateFlow<List<DialogueLine>>(emptyList())
    val messages: StateFlow<List<DialogueLine>> = _messages

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _dialogue.value = repository.getAllDialogues().find { it.id == dialogueId }
            _messages.value = repository.getDialogueMessages(dialogueId)
        }
    }

    fun delete(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteDialogue(dialogueId)
            onDeleted()
        }
    }
}

class DialogueHistoryViewModelFactory(
    private val dialogueId: Long,
    private val repository: DialogueHistoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DialogueHistoryViewModel(dialogueId, repository) as T
    }
}
