// ui/screens/dialogue_history/HistoryListViewModelFactory.kt
package com.example.dialogtrainer.ui.screens.dialogue_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dialogtrainer.data.repository.history.DialogueHistoryRepository

class HistoryListViewModelFactory(
    private val repository: DialogueHistoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryListViewModel(repository) as T
    }
}
