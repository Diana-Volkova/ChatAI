package com.example.chatai.presentation.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.data.ChatApi
import com.example.chatai.data.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val api: ChatApi
) : ViewModel() {
    private val _effects =
        MutableSharedFlow<RegisterEffect>(
            extraBufferCapacity = 1
        )

    val effects = _effects.asSharedFlow()

    fun dispatch(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.Register -> {
                register(RegisterRequest(intent.email, intent.password))
            }
        }
    }

    private fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            try {
                val response = api.register(registerRequest)
                if (response.isSuccessful) {
                    _effects.emit(RegisterEffect.NavigateToLogIn)
                } else {
                    val message = when (response.code()) {
                        409 -> "Пользователь уже существует"
                        422 -> "Некорректные данные"
                        500 -> "Ошибка сервера"
                        else -> "Ошибка регистрации"
                    }

                    _effects.emit(RegisterEffect.Error(message))
                }
            } catch (e: SocketTimeoutException) {
                _effects.emit(
                    RegisterEffect.Error("Нет подключения к интернету")
                )

            } catch (e: Exception) {
                _effects.emit(RegisterEffect.Error(e.localizedMessage ?: "Неизвестная ошибка"))
            }
        }
    }
}