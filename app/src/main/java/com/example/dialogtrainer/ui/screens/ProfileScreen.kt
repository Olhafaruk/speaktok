//ui/screens/ProfileScreen.kt
package com.example.dialogtrainer.ui.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.dialogtrainer.R
import com.example.dialogtrainer.data.model.*
import com.example.dialogtrainer.ui.components.MultiSelectDropdown
import com.example.dialogtrainer.ui.components.SingleSelectDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profile: UserProfile,
    onSaveProfile: (UserProfile) -> Unit,
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var username by remember(profile.username) { mutableStateOf(value = profile.username) }
    var nativeLanguage by remember(profile.nativeLanguage) { mutableStateOf(value = profile.nativeLanguage) }
    var country by remember(profile.country) { mutableStateOf(value = profile.country) }
    var selectedLearningLanguages by remember(profile.learningLanguages) {
        mutableStateOf(value = profile.learningLanguages.toMutableSet())
    }
    var selectedInterests by remember(profile.interests) {
        mutableStateOf(value = profile.interests.toSet())
    }

    val selectedCountry = Country.fromTitle(country)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}

            val updated = profile.copy(avatarUri = uri.toString())
            onSaveProfile(updated)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {

        val avatarPainter =
            if (!profile.avatarUri.isNullOrBlank()) {
                rememberAsyncImagePainter(model = profile.avatarUri)
            } else {
                painterResource(R.drawable.default_avatar)
            }

        Image(
            painter = avatarPainter,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Change photo")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("User Profile", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // COUNTRY
        SingleSelectDropdown(
            label = "Country",
            options = Country.entries,
            selected = selectedCountry,
            optionLabel = { "${it.flag} ${it.title}" },
            onSelect = { country = it.title },
        )

        Spacer(Modifier.height(24.dp))

        // NATIVE LANGUAGE
        SingleSelectDropdown(
            label = "Native Language",
            options = AppLanguage.entries,
            selected = AppLanguage.fromCode(nativeLanguage),
            optionLabel = { "${it.flag} ${it.code.uppercase()}" },
            onSelect = { nativeLanguage = it.code },
        )

        Spacer(Modifier.height(24.dp))

        // LEARNING LANGUAGES
        MultiSelectDropdown(
            label = "Learning Languages",
            options = AppLanguage.entries,
            selected = selectedLearningLanguages.mapNotNull { AppLanguage.fromCode(it) }.toSet(),
            optionLabel = { "${it.flag} ${it.code.uppercase()}" },
            onSelectionChange = { newSet ->
                selectedLearningLanguages = newSet.map { it.code }.toMutableSet()
            },
        )

        Spacer(Modifier.height(24.dp))

        // INTERESTS
        MultiSelectDropdown(
            label = "Interests",
            options = allInterests,
            selected = selectedInterests,
            optionLabel = { it.title },
            onSelectionChange = { selectedInterests = it },
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                val updated = profile.copy(
                    username = username,
                    nativeLanguage = nativeLanguage,
                    learningLanguages = selectedLearningLanguages.toList(),
                    country = country,
                    interests = selectedInterests.toList(),
                )
                onSaveProfile(updated)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
