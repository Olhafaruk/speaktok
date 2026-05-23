//ui/screens/DialogueScreen.kt
package com.example.dialogtrainer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dialogtrainer.core.AppDependencies
import com.example.dialogtrainer.data.model.dialogue.Speaker
import com.example.dialogtrainer.ui.dialogue.DialogueViewModel
import kotlinx.coroutines.launch

@Composable
fun DialogueScreen(
    viewModel: DialogueViewModel,
    sceneTitle: String,
    nativeLang: String,
    learningLang: String,
    onEnd: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    var showSaveDialog by remember { mutableStateOf(false) }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Dialogue?") },
            text = { Text("Do you want to save this dialogue in history?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSaveDialog = false
                        scope.launch {
                            val messages = viewModel.messages
                            if (messages.isNotEmpty()) {


                                val aiTitle = AppDependencies.aiDialogueRepository.generateTitle(messages)

                                val now = System.currentTimeMillis()
                                val title = aiTitle.ifBlank {
                                    messages.firstOrNull { it.speaker == Speaker.AGENT }?.text
                                        ?: "Dialogue"
                                }

                                AppDependencies.dialogueHistoryRepository.saveDialogue(
                                    title = title,
                                    createdAt = now,
                                    updatedAt = now,
                                    languageCode = learningLang,
                                    messages = messages
                                )
                            }
                            onEnd()
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showSaveDialog = false
                        onEnd()
                    }
                ) {
                    Text("Don't save")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = sceneTitle,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage ?: "Error",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            state.isFinished -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Dialogue finished. Great job!")
                }
            }

            else -> {
                state.currentLine?.let { line ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = if (line.speaker == Speaker.AGENT) "Agent" else "You",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = line.text,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.userAnswer,
                    onValueChange = viewModel::onUserAnswerChange,
                    label = { Text("Your answer") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                )

                Spacer(Modifier.height(8.dp))

                state.feedback?.let { fb ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text("Score: ${fb.score}/100", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Corrected: ${fb.corrected}", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Comment: ${fb.comment}", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.onSendAnswer() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Check")
                    }
                    Button(
                        onClick = { viewModel.onNext() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Next")
                    }
                    Button(
                        onClick = {
                            viewModel.finishDialogue()
                            showSaveDialog = true
                        }
                    ) {
                        Text("End")
                    }
                }
            }
        }
    }
}
