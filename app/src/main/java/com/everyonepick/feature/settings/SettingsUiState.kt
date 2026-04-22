package com.everyonepick.feature.settings

import com.everyonepick.core.datastore.ThemeMode

data class SettingsUiState(
    val selectedThemeMode: ThemeMode = ThemeMode.SYSTEM,
)

