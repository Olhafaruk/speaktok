// ui/screens/dialogue_history/HistoryListScreen.kt
package com.example.dialogtrainer.ui.screens.dialogue_history

import android.widget.Button
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dialogtrainer.data.repository.history.SavedDialogue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryListScreen(
    viewModel: HistoryListViewModel,
    onBack: () -> Unit,
    onOpenDialogue: (Long) -> Unit
) {
    val dialogues by viewModel.dialogues.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        if (dialogues.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
              Text(
                  "You don't have any saved dialogues yet.",
                  style = MaterialTheme.typography.titleMedium
              )
                Spacer(Modifier.height(16.dp))

                Button(onClick = { onOpenDialogue(-1) }) {
                    Text("Start a new dialogue")
                }

            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dialogues) { dlg ->
                    HistoryItem(dlg) { onOpenDialogue(dlg.id) }
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(
    dialogue: SavedDialogue,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(dialogue.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("Language: ${dialogue.languageCode}", style = MaterialTheme.typography.bodySmall)


        }
    }
}
