package com.example.chatai.presentation.ui.password_restore

sealed class PasswordRestoreIntent {
    data class SendResetLink(val email: String) : PasswordRestoreIntent()
}