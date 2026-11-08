package com.example.chatai.presentation.ui.theme.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ThemeState {
    var mode by mutableStateOf(ThemeMode.SYSTEM)
}