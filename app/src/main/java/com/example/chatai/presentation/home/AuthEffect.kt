package com.example.chatai.presentation.home

sealed class AuthEffect {
    data object NavigateToLogin :
        AuthEffect()

}