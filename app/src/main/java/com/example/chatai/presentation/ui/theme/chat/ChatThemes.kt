package com.example.chatai.presentation.ui.theme.chat

import androidx.compose.material3.ColorScheme
import com.example.chatai.domain.theme.ChatThemeId
import com.example.chatai.presentation.ui.theme.app.DarkColorScheme
import com.example.chatai.presentation.ui.theme.app.LightColorScheme
import com.example.chatai.presentation.ui.theme.chat.forest.ForestDarkColorScheme
import com.example.chatai.presentation.ui.theme.chat.forest.ForestLightColorScheme
import com.example.chatai.presentation.ui.theme.chat.midnight.MidnightDarkColorScheme
import com.example.chatai.presentation.ui.theme.chat.midnight.MidnightLightColorScheme
import com.example.chatai.presentation.ui.theme.chat.sunset.SunsetDarkColorScheme
import com.example.chatai.presentation.ui.theme.chat.sunset.SunsetLightColorScheme

object ChatThemes {

    fun get(
        theme: ChatThemeId,
        darkTheme: Boolean
    ): ColorScheme {
        return when (theme) {
            ChatThemeId.DEFAULT ->
                if (darkTheme) DarkColorScheme
                else LightColorScheme

            ChatThemeId.MIDNIGHT ->
                if (darkTheme) MidnightDarkColorScheme
                else MidnightLightColorScheme

            ChatThemeId.FOREST ->
                if (darkTheme) ForestDarkColorScheme
                else ForestLightColorScheme

            ChatThemeId.SUNSET ->
                if (darkTheme) SunsetDarkColorScheme
                else SunsetLightColorScheme
        }
    }
}