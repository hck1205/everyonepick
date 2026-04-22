package com.everyonepick.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.everyonepick.feature.main.MainRoute
import com.everyonepick.feature.settings.SettingsRoute

@Composable
fun EveryonePickNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = EveryonePickDestination.Main.route,
        modifier = modifier,
    ) {
        composable(EveryonePickDestination.Main.route) {
            MainRoute(
                onOpenSettings = {
                    navController.navigate(EveryonePickDestination.Settings.route) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(EveryonePickDestination.Settings.route) {
            SettingsRoute(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
