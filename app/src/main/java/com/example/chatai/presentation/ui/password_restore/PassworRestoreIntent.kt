package com.example.chatai.presentation.ui.password_restore

sealed class PassworRestoreIntent {
    data class SendResetLink(val email: String) : PassworRestoreIntent()
}