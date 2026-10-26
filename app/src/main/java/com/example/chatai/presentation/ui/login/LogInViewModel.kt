package com.example.chatai.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.domain.exception.AuthException
import com.example.chatai.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val login: LoginUseCase
) : ViewModel() {

    private val _effects =
        MutableSharedFlow<LogInEffect>(
            extraBufferCapacity = 1
        )

    val effects = _effects.asSharedFlow()

    fun dispatch(intent: LogInIntent) {
        when (intent) {
            is LogInIntent.LogIn -> {
                logIn(
                    email = intent.email,
                    password = intent.password
                )
            }
        }
    }

    private fun logIn(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                login(email, password)

                _effects.emit(
                    LogInEffect.NavigateToHome
                )
            } catch (e: AuthException) {
                _effects.emit(
                    LogInEffect.Error(e)
                )
            } catch (e: SocketTimeoutException) {
                _effects.emit(
                    LogInEffect.Error(e)
                )
            } catch (e: Exception) {
                _effects.emit(
                    LogInEffect.Error(e)
                )
            }
        }
    }
}