package com.example.chatai.presentation.ui.login

sealed class LogInEffect {
    object NavigateToHome : LogInEffect()

    data class Error(
        val exception: Exception
    ) : LogInEffect()
}