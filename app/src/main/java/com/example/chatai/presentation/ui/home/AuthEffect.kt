package com.example.chatai.presentation.ui.home

sealed class AuthEffect {
    data object NavigateToLogin :
        AuthEffect()

}