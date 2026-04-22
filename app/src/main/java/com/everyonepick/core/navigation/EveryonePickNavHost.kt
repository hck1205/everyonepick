package com.everyonepick.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.everyonepick.feature.home.HomeRoute
import com.everyonepick.feature.settings.SettingsRoute

@Composable
fun EveryonePickNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = EveryonePickDestination.Home.route,
        modifier = modifier,
    ) {
        composable(EveryonePickDestination.Home.route) {
            HomeRoute(
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

