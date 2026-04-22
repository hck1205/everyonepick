package com.everyonepick.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.everyonepick.core.model.QuickNote
import java.text.DateFormat
import java.util.Date

@Composable
fun HomeRoute(
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        onDraftTitleChange = viewModel::onDraftTitleChange,
        onSaveClick = viewModel::saveNote,
        onOpenSettings = onOpenSettings,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onDraftTitleChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EveryonePick") },
                actions = {
                    TextButton(onClick = onOpenSettings) {
                        Text("Settings")
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 20.dp,
                top = innerPadding.calculateTopPadding() + 20.dp,
                end = 20.dp,
                bottom = innerPadding.calculateBottomPadding() + 20.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                StarterOverviewCard()
            }

            item {
                CreateNoteCard(
                    draftTitle = uiState.draftTitle,
                    canSave = uiState.canSave,
                    onDraftTitleChange = onDraftTitleChange,
                    onSaveClick = onSaveClick,
                )
            }

            item {
                Text(
                    text = "Local Notes",
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            if (uiState.notes.isEmpty()) {
                item {
                    EmptyNotesCard()
                }
            } else {
                items(
                    items = uiState.notes,
                    key = QuickNote::id,
                ) { note ->
                    QuickNoteCard(note = note)
                }
            }
        }
    }
}

@Composable
private fun StarterOverviewCard() {
    Card {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Starter shape",
                style = MaterialTheme.typography.titleLarge,
            )
            Text("Compose UI + Navigation")
            Text("Hilt for dependency injection")
            Text("Room for offline-first local persistence")
            Text("DataStore-driven app preferences")
            Text("Single app module with boundaries ready for future modularization")
        }
    }
}

@Composable
private fun CreateNoteCard(
    draftTitle: String,
    canSave: Boolean,
    onDraftTitleChange: (String) -> Unit,
    onSaveClick: () -> Unit,
) {
    OutlinedCard {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Create a note",
                style = MaterialTheme.typography.titleMedium,
            )
            OutlinedTextField(
                value = draftTitle,
                onValueChange = onDraftTitleChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Title") },
                singleLine = true,
            )
            Button(
                onClick = onSaveClick,
                enabled = canSave,
            ) {
                Text("Save locally")
            }
        }
    }
}

@Composable
private fun EmptyNotesCard() {
    OutlinedCard {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "No notes yet",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Create one above to verify Room, ViewModel, and Compose state are wired correctly.",
            )
        }
    }
}

@Composable
private fun QuickNoteCard(note: QuickNote) {
    val formattedCreatedAt = DateFormat.getDateTimeInstance(
        DateFormat.SHORT,
        DateFormat.SHORT,
    ).format(Date(note.createdAtEpochMillis))

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Text(
                text = formattedCreatedAt,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
