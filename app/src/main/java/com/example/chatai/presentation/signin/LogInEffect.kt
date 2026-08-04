package com.example.chatai.presentation.signin

sealed class LogInEffect {
    object NavigateToHome : LogInEffect()

    data class Error(
        val message: String
    ) : LogInEffect()
}