// ui/screens/dialogue_history/HistoryListViewModel.kt
package com.example.dialogtrainer.ui.screens.dialogue_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dialogtrainer.data.repository.history.DialogueHistoryRepository
import com.example.dialogtrainer.data.repository.history.SavedDialogue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryListViewModel(
    private val repository: DialogueHistoryRepository
) : ViewModel() {

    private val _dialogues = MutableStateFlow<List<SavedDialogue>>(emptyList())
    val dialogues: StateFlow<List<SavedDialogue>> = _dialogues

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _dialogues.value = repository.getAllDialogues()
        }
    }
}
