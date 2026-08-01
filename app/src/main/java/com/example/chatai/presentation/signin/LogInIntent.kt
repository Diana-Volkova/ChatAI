package com.example.chatai.presentation.signin

sealed class LogInIntent {
    data class LogIn(
        val email: String,
        val password: String
    ) : LogInIntent()
}