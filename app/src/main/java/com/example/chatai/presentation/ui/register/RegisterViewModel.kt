package com.example.chatai.presentation.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.domain.error.AuthException
import com.example.chatai.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val register: RegisterUseCase,
) : ViewModel() {
    private val _effects =
        MutableSharedFlow<RegisterEffect>(
            extraBufferCapacity = 1
        )

    val effects = _effects.asSharedFlow()

    fun dispatch(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.Register -> {
                registerUser(
                    email = intent.email,
                    password = intent.password,
                )
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                register(email, password)

                _effects.emit(RegisterEffect.NavigateToLogIn)
            } catch (e: AuthException) {
                _effects.emit(
                    RegisterEffect.Error(e.message())
                )
            } catch (e: SocketTimeoutException) {
                _effects.emit(
                    RegisterEffect.Error("Нет подключения к интернету: " + e.localizedMessage)
                )
            } catch (e: Exception) {
                _effects.emit(RegisterEffect.Error(e.localizedMessage ?: "Неизвестная ошибка"))
            }
        }
    }
}