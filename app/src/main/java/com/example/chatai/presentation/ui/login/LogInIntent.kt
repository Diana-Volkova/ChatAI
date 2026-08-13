package com.example.chatai.presentation.ui.login

sealed class LogInIntent {
    data class LogIn(
        val email: String,
        val password: String
    ) : LogInIntent()
}