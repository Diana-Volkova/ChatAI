package com.example.chatai.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB388FF),       // акцент (фиолетовый)
    secondary = Color(0xFF80CBC4),     // вторичный акцент (не обязателен)
    tertiary = Color(0xFFF48FB1),

    background = Color(0xFF0F0F14),    // основной фон (очень важно)
    surface = Color(0xFF1A1A22),       // карточки сообщений
    surfaceVariant = Color(0xFF242433),

    onPrimary = Color.Black,
    onBackground = Color(0xFFE6E6E6),
    onSurface = Color(0xFFE6E6E6)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6A1B9A),
    background = Color(0xFFF6F6F8),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFECECF1),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)
@Composable
fun ChatAITheme(content: @Composable () -> Unit) {

    val darkTheme = when (ThemeState.mode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}