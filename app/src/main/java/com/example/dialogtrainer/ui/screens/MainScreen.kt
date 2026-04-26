//ui/screens/MainScreen.kt
package com.example.dialogtrainer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onStartDialogue: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Dialog Trainer",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onStartDialogue,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Dialogue")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onOpenHistory,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("History")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onOpenProfile,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Profile")
        }
    }
}
