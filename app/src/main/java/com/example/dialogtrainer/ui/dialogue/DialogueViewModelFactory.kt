package com.example.dialogtrainer.ui.dialogue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dialogtrainer.data.repository.DialogueRepository

class DialogueViewModelFactory(
    private val repository: DialogueRepository,
    private val sceneId: String?,
    private val nativeLanguageCode: String,
    private val learningLanguageCode: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DialogueViewModel::class.java)) {
            return DialogueViewModel(
                repository = repository,
                sceneId = sceneId,
                nativeLanguageCode = nativeLanguageCode,
                learningLanguageCode = learningLanguageCode
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
