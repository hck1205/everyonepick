package com.everyonepick.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Evergreen,
    onPrimary = Mist,
    primaryContainer = EvergreenContainer,
    onPrimaryContainer = Spruce,
    secondary = Spruce,
    background = Mist,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Fog,
    outline = Slate,
)

private val DarkColors = darkColorScheme(
    primary = MintAccent,
    onPrimary = Night,
    primaryContainer = Spruce,
    onPrimaryContainer = EvergreenContainer,
    secondary = EvergreenContainer,
    background = Night,
    onBackground = Fog,
    surface = NightSurface,
    onSurface = Fog,
    surfaceVariant = Spruce,
    outline = Slate,
)

@Composable
fun EveryonePickTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = EveryonePickTypography,
        content = content,
    )
}

