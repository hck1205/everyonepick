package com.everyonepick.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
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
import com.everyonepick.core.datastore.ThemeMode

@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        uiState = uiState,
        onBack = onBack,
        onThemeModeSelected = viewModel::onThemeModeSelected,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onBack: () -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedCard {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Theme mode",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    ThemeMode.entries.forEach { themeMode ->
                        ThemeModeRow(
                            themeMode = themeMode,
                            isSelected = uiState.selectedThemeMode == themeMode,
                            onSelect = { onThemeModeSelected(themeMode) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeModeRow(
    themeMode: ThemeMode,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = themeMode.label(),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = themeMode.description(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun ThemeMode.label(): String = when (this) {
    ThemeMode.SYSTEM -> "Follow system"
    ThemeMode.LIGHT -> "Always light"
    ThemeMode.DARK -> "Always dark"
}

private fun ThemeMode.description(): String = when (this) {
    ThemeMode.SYSTEM -> "Use the device preference."
    ThemeMode.LIGHT -> "Force the app into light mode."
    ThemeMode.DARK -> "Force the app into dark mode."
}

