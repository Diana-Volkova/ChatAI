package com.example.chatai.presentation.ui.register

sealed class RegisterIntent {
    data class Register(
        val email: String,
        val password: String
    ) : RegisterIntent()
}