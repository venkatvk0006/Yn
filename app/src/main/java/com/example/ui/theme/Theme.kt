package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = HyperPrimaryBlue,
    secondary = HyperSecondaryCyan,
    tertiary = HyperAccentPurple,
    background = HyperBackgroundDark,
    surface = HyperSurfaceDark,
    surfaceVariant = HyperCardSurface,
    onPrimary = TextPrimaryDark,
    onSecondary = TextPrimaryDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = HyperCardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = HyperPrimaryBlue,
    secondary = HyperSecondaryCyan,
    tertiary = HyperAccentPurple,
    background = HyperBackgroundDark,
    surface = HyperSurfaceDark,
    surfaceVariant = HyperCardSurface,
    onPrimary = TextPrimaryDark,
    onSecondary = TextPrimaryDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = HyperCardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

