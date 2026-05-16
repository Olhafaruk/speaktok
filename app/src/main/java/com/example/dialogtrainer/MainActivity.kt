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
import com.example.dialogtrainer.ui.screens.DialogueScreen
import com.example.dialogtrainer.ui.screens.MainScreen
import com.example.dialogtrainer.ui.screens.ProfileScreen
import com.example.dialogtrainer.ui.screens.ProfileViewModel
import com.example.dialogtrainer.ui.screens.ProfileViewModelFactory
import com.example.dialogtrainer.ui.screens.dialogue_history.DialogueHistoryScreen
import com.example.dialogtrainer.ui.screens.dialogue_history.DialogueHistoryViewModel
import com.example.dialogtrainer.ui.screens.dialogue_history.DialogueHistoryViewModelFactory
import com.example.dialogtrainer.ui.screens.dialogue_history.HistoryListScreen
import com.example.dialogtrainer.ui.screens.dialogue_history.HistoryListViewModel
import com.example.dialogtrainer.ui.screens.dialogue_history.HistoryListViewModelFactory
import com.example.dialogtrainer.ui.theme.DialogTrainerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DialogTrainerTheme {
                DialogTrainerNavHost()
            }
        }
    }
}

@Composable
fun DialogTrainerNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {

        // MAIN SCREEN
        composable("main") {
            MainScreen(
                onStartDialogue = { navController.navigate("dialogue_new") },
                onOpenHistory = { navController.navigate("dialogues") },
                onOpenProfile = { navController.navigate("profile") }
            )
        }

        // HISTORY LIST SCREEN
        composable("dialogues") {
            val viewModel: HistoryListViewModel = viewModel(
                factory = HistoryListViewModelFactory(AppDependencies.dialogueHistoryRepository)
            )

            HistoryListScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenDialogue = { id ->
                    if (id == -1L) {
                        navController.navigate("dialogue_new")
                    } else {
                        navController.navigate("dialogue_history/$id")
                    }
                }
            )
        }

        // DIALOGUE HISTORY SCREEN
        composable(
            route = "dialogue_history/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L

            if (id == 0L) {
                navController.popBackStack()
                return@composable
            }

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
                    sceneTitle = "New dialogue",
                    onEnd = {
                        navController.popBackStack("main", false)
                    }
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
