package com.everyonepick.core.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.everyonepick.core.designsystem.theme.EveryonePickTheme
import com.everyonepick.core.navigation.EveryonePickNavHost

@Composable
fun AppRoot(
    viewModel: AppStateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    EveryonePickTheme(themeMode = uiState.themeMode) {
        Surface(modifier = Modifier.fillMaxSize()) {
            EveryonePickNavHost(navController = navController)
        }
    }
}

