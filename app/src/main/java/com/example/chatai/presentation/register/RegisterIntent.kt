package com.example.chatai.presentation.register

sealed class RegisterIntent {
    data class Register(
        val email: String,
        val password: String
    ) : RegisterIntent()
}