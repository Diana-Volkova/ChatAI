package com.example.chatai.presentation.register

sealed class RegisterEffect {
    object NavigateToLogIn : RegisterEffect()

    data class Error(
        val message: String
    ) : RegisterEffect()
}