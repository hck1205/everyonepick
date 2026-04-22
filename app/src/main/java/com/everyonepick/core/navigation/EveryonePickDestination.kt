package com.everyonepick.core.navigation

sealed class EveryonePickDestination(val route: String) {
    data object Main : EveryonePickDestination("main")

    data object Settings : EveryonePickDestination("settings")
}
