//SceneListScreen.kt
package com.example.dialogtrainer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dialogtrainer.data.model.Role
import com.example.dialogtrainer.data.repository.SceneRepository
import com.example.dialogtrainer.ui.iconForRole

@Composable
fun SceneListScreen(
    onSceneSelected: (String) -> Unit
) {
    val repository = SceneRepository()
    val scenes = repository.getScenes()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        scenes.forEach { scene ->

            val firstLine = scene.lines.firstOrNull()
            val previewGerman = firstLine?.textGerman ?: ""
            val previewRole = firstLine?.role ?: Role.NARRATOR

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clickable { onSceneSelected(scene.id.toString()) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = iconForRole(previewRole),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3F2FD))
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Card(
                    modifier = Modifier.weight(1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(scene.title, style = MaterialTheme.typography.titleMedium)
                        Text(
                            previewGerman,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
