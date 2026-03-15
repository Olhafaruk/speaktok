//ui/screens/MainScreen.kt
package com.example.dialogtrainer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onNavigateToScenes: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Dialog Trainer")

        Button(
            onClick = onNavigateToScenes,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Scenes")
        }

        Button(
            onClick = onNavigateToProfile,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Profile")
        }
    }
}
