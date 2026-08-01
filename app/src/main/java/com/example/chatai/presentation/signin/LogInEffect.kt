package com.example.chatai.presentation.signin

sealed interface LogInEffect {
    data object NavigateToHome : LogInEffect
}