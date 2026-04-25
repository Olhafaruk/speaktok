//ui/screens/dialogue_list/DialogueListScreen.kt
package com.example.dialogtrainer.ui.screens.dialogue_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dialogtrainer.data.repository.history.SavedDialogue

@Composable
fun DialogueListScreen(
    viewModel: DialogueListViewModel,
    onOpenDialogue: (Long) -> Unit,
    onStartNewDialogue: () -> Unit
) {
    val dialogues by viewModel.dialogues.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onStartNewDialogue) {
                Icon(Icons.Default.Add, contentDescription = "New dialogue")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(dialogues) { dialogue ->
                DialogueListItem(dialogue, onOpenDialogue)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun DialogueListItem(
    dialogue: SavedDialogue,
    onOpen: (Long) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen(dialogue.id) },
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(dialogue.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Дата: ${dialogue.createdAt}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

