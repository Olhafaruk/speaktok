//SceneDetailScreen.kt
package com.example.dialogtrainer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dialogtrainer.data.model.Role
import com.example.dialogtrainer.data.repository.SceneRepository
import com.example.dialogtrainer.ui.iconForRole

@Composable
fun SceneDetailScreen(
    sceneId: String,
    navController: NavController,
) {
    val repository = SceneRepository()
    val scene = repository.getScenes().firstOrNull { it.id.toString() == sceneId }

    if (scene == null) {
        Text("Scene not found")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(scene.title, style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        scene.lines.forEach { line ->
            var showTranslation by remember { mutableStateOf(value = true) }
            val isUser = line.role == Role.CUSTOMER

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
            ) {
                if (!isUser) {
                    Icon(
                        imageVector = iconForRole(line.role),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE3F2FD))
                            .padding(6.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }

                Column(
                    modifier = Modifier
                        .widthIn(max = 260.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isUser) 16.dp else 0.dp,
                                bottomEnd = if (isUser) 0.dp else 16.dp,
                            )
                        )
                        .background(
                            if (isUser) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .padding(12.dp)
                        .clickable { showTranslation = !showTranslation }
                ) {
                    Text(line.textGerman, style = MaterialTheme.typography.bodyLarge)

                    if (showTranslation) {
                        Text(
                            line.textTranslation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                if (isUser) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = iconForRole(line.role),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE3F2FD))
                            .padding(6.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
        }
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate("dialogue/${scene.id}/${scene.title}")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Dialogue")
        }
    }
}
