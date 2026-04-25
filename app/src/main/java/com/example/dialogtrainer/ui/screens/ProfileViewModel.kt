//ui/screens/ProfileViewModel.kt
package com.example.dialogtrainer.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dialogtrainer.data.model.UserProfile
import com.example.dialogtrainer.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: UserProfileRepository,
) : ViewModel() {

    val profile: StateFlow<UserProfile?> =
        repository.profile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun saveProfile(updated: UserProfile) {
        viewModelScope.launch {
            repository.updateProfile(updated)
        }
    }

    fun updateAvatar(uri: String) {
        viewModelScope.launch {
            val current = profile.value ?: return@launch
            val updated = current.copy(avatarUri = uri)
            repository.updateProfile(updated)
        }
    }

}
