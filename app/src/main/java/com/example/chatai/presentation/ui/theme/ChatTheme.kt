package com.example.chatai.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.chatai.domain.theme.ChatThemeId


val DefaultChatColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color.White,

    background = Color(0xFFF9F9FF),
    onBackground = Color(0xFF1A1B20),

    surface = Color(0xFFF9F9FF),
    onSurface = Color(0xFF1A1B20)
)

val MidnightChatColorScheme = darkColorScheme(
    primary = Color(0xFFB8A0FF),
    onPrimary = Color(0xFF24134F),

    background = Color(0xFF101014),
    onBackground = Color(0xFFE6E1E9),

    surface = Color(0xFF18181D),
    onSurface = Color(0xFFE6E1E9)
)

val ForestChatColorScheme = lightColorScheme(
    primary = Color(0xFF496B45),
    onPrimary = Color.White,

    background = Color(0xFFF5F8F1),
    onBackground = Color(0xFF191D18),

    surface = Color(0xFFE9F0E4),
    onSurface = Color(0xFF191D18)
)

val SunsetChatColorScheme = lightColorScheme(
    primary = Color(0xFFB84F35),
    onPrimary = Color.White,

    background = Color(0xFFFFF7F2),
    onBackground = Color(0xFF211A17),

    surface = Color(0xFFFFE8DC),
    onSurface = Color(0xFF211A17)
)

@Composable
fun ChatTheme(
    theme: ChatThemeId,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        ChatThemeId.DEFAULT -> DefaultChatColorScheme
        ChatThemeId.MIDNIGHT -> MidnightChatColorScheme
        ChatThemeId.FOREST -> ForestChatColorScheme
        ChatThemeId.SUNSET -> SunsetChatColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}