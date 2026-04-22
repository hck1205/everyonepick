package com.everyonepick.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everyonepick.core.preferences.ThemeMode
import com.everyonepick.core.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class AppStateUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
)

@HiltViewModel
class AppStateViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    val uiState = userPreferencesRepository.userPreferences
        .map { preferences -> AppStateUiState(themeMode = preferences.themeMode) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppStateUiState(),
        )
}
