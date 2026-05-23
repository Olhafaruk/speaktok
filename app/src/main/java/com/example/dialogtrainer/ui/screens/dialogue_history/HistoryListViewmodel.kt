// ui/screens/dialogue_history/HistoryListViewModel.kt
package com.example.dialogtrainer.ui.screens.dialogue_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dialogtrainer.data.repository.history.DialogueHistoryRepository
import com.example.dialogtrainer.data.repository.history.SavedDialogue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryListViewModel(
    private val repository: DialogueHistoryRepository
) : ViewModel() {

    val dialogues = repository.getAllDialogues()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteDialogue(id: Long) {
        viewModelScope.launch {
            repository.deleteDialogue(id)
        }
    }
}
