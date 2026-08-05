package com.example.chatai.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.data.ChatApi
import com.example.chatai.data.RefreshRequest
import com.example.chatai.data.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: ChatApi,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _effects = MutableSharedFlow<AuthEffect>()
    val effects = _effects.asSharedFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            val refreshToken = sessionManager.refreshToken()
            if (refreshToken == null) {
                _error.value = "Сессия уже завершена"
                return@launch
            }

            try {
                val response = api.logout(
                    RefreshRequest(refreshToken)
                )
                if (response.isSuccessful) {
                    sessionManager.clear()
                    _effects.emit(AuthEffect.NavigateToLogin)
                } else {
                    _error.value = when (response.code()) {
                        401 -> "Сессия уже недействительна"
                        500 -> "Ошибка сервера"
                        else -> "Не удалось выйти из аккаунта"
                    }
                }
            } catch (_: Exception) {
                _error.value = "Не удалось связаться с сервером"
            }
        }
    }
}