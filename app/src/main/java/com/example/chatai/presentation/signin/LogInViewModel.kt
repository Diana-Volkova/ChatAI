package com.example.chatai.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.data.ChatApi
import com.example.chatai.data.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    val api: ChatApi
) : ViewModel() {
    private val _effects = MutableSharedFlow<LogInEffect>(
        extraBufferCapacity = 1
    )

    val effects = _effects.asSharedFlow()

    fun dispatch(intent: LogInIntent) {
        when (intent) {
            is LogInIntent.LogIn -> {
                logIn(LoginRequest(email = intent.email, password = intent.password))
            }
        }
    }

    fun logIn(loginRequest: LoginRequest) {
        viewModelScope.launch {
            try {
                api.login(loginRequest)

                _effects.emit(LogInEffect.NavigateToHome)
            } catch (e: Exception) {
                // обновить state с ошибкой
            }
        }
    }
}