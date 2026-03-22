//ui/screens/ProfileScreen.kt
package com.example.dialogtrainer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import com.example.dialogtrainer.data.model.AppLanguage
import com.example.dialogtrainer.data.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    profile: UserProfile,
    onSaveProfile: (UserProfile) -> Unit
) {
    val scrollState = rememberScrollState()

    var username by remember(profile.username) { mutableStateOf(profile.username) }
    var nativeLanguage by remember(profile.nativeLanguage) { mutableStateOf(profile.nativeLanguage) }
    var country by remember(profile.country) { mutableStateOf(profile.country) }
    var selectedLearningLanguages by remember(profile.learningLanguages) {
        mutableStateOf(profile.learningLanguages.toMutableSet())
    }

    val availableLanguages = AppLanguage.entries

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {

        Text("User Profile", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Text("Native Language", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            availableLanguages.forEach { lang ->
                FilterChip(
                    selected = nativeLanguage == lang.code,
                    onClick = { nativeLanguage = lang.code },
                    label = { Text("${lang.flag} ${lang.code.uppercase()}") }
                )
            }

        }

        Spacer(Modifier.height(24.dp))

        Text("Learning Languages", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            availableLanguages.forEach { lang ->
                val isSelected = selectedLearningLanguages.contains(lang.code)

                FilterChip(
                    selected = isSelected,
                    onClick = {
                        if (isSelected)
                            selectedLearningLanguages.remove(lang.code)
                        else
                            selectedLearningLanguages.add(lang.code)
                    },
                    label = { Text("${lang.flag} ${lang.code.uppercase()}") }
                )
            }

        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text("Country") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                val updated = profile.copy(
                    username = username,
                    nativeLanguage = nativeLanguage,
                    learningLanguages = selectedLearningLanguages.toList(),
                    country = country
                )
                onSaveProfile(updated)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
