package com.example.chatai.presentation.ui.register

sealed class RegisterEffect {
    object NavigateToLogIn : RegisterEffect()

    data class Error(
        val exception: Exception
    ) : RegisterEffect()
}