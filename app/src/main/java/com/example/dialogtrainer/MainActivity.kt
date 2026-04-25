//MainActivity.kt
package com.example.dialogtrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.dialogtrainer.core.AppDependencies
import com.example.dialogtrainer.data.repository.UserProfileRepositoryImpl
import com.example.dialogtrainer.data.repository.userProfileDataStore
import com.example.dialogtrainer.ui.dialogue.DialogueViewModel
import com.example.dialogtrainer.ui.dialogue.DialogueViewModelFactory
import com.example.dialogtrainer.ui.screens.*
import com.example.dialogtrainer.ui.screens.dialogue_history.DialogueHistoryScreen
import com.example.dialogtrainer.ui.screens.dialogue_history.DialogueHistoryViewModel
import com.example.dialogtrainer.ui.screens.dialogue_history.DialogueHistoryViewModelFactory
import com.example.dialogtrainer.ui.screens.dialogue_list.DialogueListScreen
import com.example.dialogtrainer.ui.screens.dialogue_list.DialogueListViewModel
import com.example.dialogtrainer.ui.theme.DialogTrainerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DialogTrainerTheme {
                DialogTrainerApp()
            }
        }
    }
}

@Composable
fun DialogTrainerApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {

        // MAIN SCREEN
        composable("main") {
            MainScreen(
                onNavigateToScenes = { navController.navigate("dialogues") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }

        // DIALOGUE LIST SCREEN
        composable("dialogues") {
            val viewModel: DialogueListViewModel = viewModel(
                factory = DialogueListViewModel.Factory(AppDependencies.dialogueHistoryRepository)

            )

            DialogueListScreen(
                viewModel = viewModel,
                onOpenDialogue = { id ->
                    navController.navigate("dialogue_history/$id")
                },
                onStartNewDialogue = {
                    navController.navigate("dialogue_new")
                }
            )
        }

        // DIALOGUE HISTORY SCREEN
        composable(
            route = "dialogue_history/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L

            val viewModel: DialogueHistoryViewModel = viewModel(
                factory = DialogueHistoryViewModelFactory(
                    dialogueId = id,
                    repository = AppDependencies.dialogueHistoryRepository
                )
            )

            DialogueHistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // NEW DIALOGUE SCREEN
        composable("dialogue_new") {
            val context = LocalContext.current
            val profileRepository = UserProfileRepositoryImpl(context.userProfileDataStore)

            val profileState = profileRepository.profile.collectAsState(initial = null)
            val profile = profileState.value

            if (profile == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val nativeLang = profile.nativeLanguage.ifBlank { "uk" }
                val learningLang = profile.learningLanguages.firstOrNull().orEmpty().ifBlank { "en" }

                val viewModel: DialogueViewModel = viewModel(
                    factory = DialogueViewModelFactory(
                        repository = AppDependencies.aiDialogueRepository,
                        sceneId = null,
                        nativeLanguageCode = nativeLang,
                        learningLanguageCode = learningLang
                    )
                )

                DialogueScreen(
                    viewModel = viewModel,
                    sceneTitle = "Новый диалог"
                )
            }
        }

        // PROFILE SCREEN
        composable("profile") {
            val context = LocalContext.current
            val repository = UserProfileRepositoryImpl(context.userProfileDataStore)
            val factory = ProfileViewModelFactory(repository)
            val viewModel: ProfileViewModel = viewModel(factory = factory)

            val profileState = viewModel.profile.collectAsState()

            when (val profile = profileState.value) {
                null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    ProfileScreen(
                        profile = profile,
                        onSaveProfile = viewModel::saveProfile
                    )
                }
            }
        }
    }
}
