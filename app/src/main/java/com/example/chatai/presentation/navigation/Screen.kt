package com.example.chatai.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object HomeScreen : Screen()

    @Serializable
    data class ChatScreen(
        val chatId: Int
    ) : Screen()

    @Serializable
    object LogInScreen : Screen()

    @Serializable
    object RegisterScreen : Screen()

    @Serializable
    object SettingsScreen : Screen()
}