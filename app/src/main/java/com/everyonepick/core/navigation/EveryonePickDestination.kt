package com.everyonepick.core.navigation

sealed class EveryonePickDestination(val route: String) {
    data object Home : EveryonePickDestination("home")

    data object Settings : EveryonePickDestination("settings")
}

