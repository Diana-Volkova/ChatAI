package com.example.chatai.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.data.ChatApi
import com.example.chatai.data.LoginRequest
import com.example.chatai.data.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    val api: ChatApi,
    private val sessionManager: SessionManager
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
                val response = api.login(loginRequest)

                if (response.isSuccessful) {

                    val body = response.body() ?: return@launch

                    sessionManager.saveTokens(
                        accessToken = body.access_token,
                        refreshToken = body.refresh_token
                    )

                    _effects.emit(LogInEffect.NavigateToHome)

                } else {
                    // показать ошибку логина
                }

            } catch (e: Exception) {
                // показать ошибку сети
            }
        }
    }
}