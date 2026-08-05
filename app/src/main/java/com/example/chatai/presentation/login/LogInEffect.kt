package com.example.chatai.presentation.login

sealed class LogInEffect {
    object NavigateToHome : LogInEffect()

    data class Error(
        val message: String
    ) : LogInEffect()
}