package com.example.chatai.presentation.ui.theme.chat

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.chatai.domain.theme.ChatThemeId

@Composable
fun ChatTheme(
    theme: ChatThemeId,
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ChatThemes.get(theme, darkTheme),
        content = content
    )
}