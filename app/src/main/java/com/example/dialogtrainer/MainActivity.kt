//MainActivity.kt
package com.example.dialogtrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dialogtrainer.ui.screens.MainScreen
import com.example.dialogtrainer.ui.screens.SceneDetailScreen
import com.example.dialogtrainer.ui.screens.SceneListScreen
import com.example.dialogtrainer.ui.theme.DialogTrainerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DialogTrainerApp()
        }
    }
}

@Composable
fun DialogTrainerApp() {
    DialogTrainerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "main"
            ) {
                composable("main") {
                    MainScreen(navController)
                }
                composable("scene_list") {
                    SceneListScreen(navController)
                }
                composable("scene_detail/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
                    SceneDetailScreen(navController, id)
                }

            }
        }
    }
}
