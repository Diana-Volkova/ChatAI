package com.example.chatai.presentation.ui.theme.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.chatai.presentation.ui.theme.Typography

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB388FF),
    secondary = Color(0xFF80CBC4),
    tertiary = Color(0xFFF48FB1),

    background = Color(0xFF0F0F14),
    surface = Color(0xFF1A1A22),
    surfaceVariant = Color(0xFF242433),

    onPrimary = Color.Black,
    onBackground = Color(0xFFE6E6E6),
    onSurface = Color(0xFFE6E6E6)
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6A1B9A),
    background = Color(0xFFF6F6F8),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFECECF1),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)
val LocalDarkTheme = staticCompositionLocalOf { false }

@Composable
fun ChatAITheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit
) {

    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}