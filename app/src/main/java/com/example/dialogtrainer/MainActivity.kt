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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dialogtrainer.core.AppDependencies
import com.example.dialogtrainer.data.repository.UserProfileRepositoryImpl
import com.example.dialogtrainer.data.repository.userProfileDataStore
import com.example.dialogtrainer.ui.dialogue.DialogueViewModel
import com.example.dialogtrainer.ui.dialogue.DialogueViewModelFactory
import com.example.dialogtrainer.ui.screens.*
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


        composable("main") {
            MainScreen(
                onNavigateToScenes = { navController.navigate("scenes") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }


        composable("scenes") {
            SceneListScreen(
                onSceneSelected = { id ->
                    navController.navigate("scene_detail/$id")
                }
            )
        }

        composable("scene_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            SceneDetailScreen(sceneId = id, navController = navController)
        }


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
        composable(
            route = "dialogue/{sceneId}/{sceneTitle}",
            arguments = listOf(
                navArgument("sceneId") { type = NavType.StringType },
                navArgument("sceneTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val sceneId = backStackEntry.arguments?.getString("sceneId") ?: ""
            val sceneTitle = backStackEntry.arguments?.getString("sceneTitle") ?: ""

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

                val factory = DialogueViewModelFactory(
                    repository = AppDependencies.dialogueRepository,
                    sceneId = sceneId,
                    nativeLanguageCode = nativeLang,
                    learningLanguageCode = learningLang
                )

                val viewModel: DialogueViewModel = viewModel(factory = factory)

                DialogueScreen(
                    viewModel = viewModel,
                    sceneTitle = sceneTitle
                )
            }
        }


    }
}
