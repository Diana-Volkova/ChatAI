package com.example.chatai.presentation.ui.settings

sealed class AuthEffect {
    data object NavigateToLogin :
        AuthEffect()

}