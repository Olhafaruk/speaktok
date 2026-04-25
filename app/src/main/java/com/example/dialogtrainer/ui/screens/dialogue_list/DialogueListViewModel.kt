//ui/screens/dialogue_list/DialogueListViewModel.kt
package com.example.dialogtrainer.ui.screens.dialogue_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dialogtrainer.data.repository.history.DialogueHistoryRepository
import com.example.dialogtrainer.data.repository.history.SavedDialogue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DialogueListViewModel(
    private val repository: DialogueHistoryRepository
) : ViewModel() {

    private val _dialogues = MutableStateFlow<List<SavedDialogue>>(emptyList())
    val dialogues: StateFlow<List<SavedDialogue>> = _dialogues

    init {
        loadDialogues()
    }

    private fun loadDialogues() {
        viewModelScope.launch {
            _dialogues.value = repository.getAllDialogues()
        }
    }

    class Factory(
        private val repository: DialogueHistoryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DialogueListViewModel(repository) as T
        }
    }
}
