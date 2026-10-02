package com.example.chatai.presentation.ui.password_restore


sealed class PasswordRestoreEffect {

    object EmailSent : PasswordRestoreEffect()

    data class Error(
        val message: String
    ) : PasswordRestoreEffect()
}