package com.everyonepick.feature.settings

import com.everyonepick.core.preferences.ThemeMode

data class SettingsUiState(
    val selectedThemeMode: ThemeMode = ThemeMode.SYSTEM,
)

