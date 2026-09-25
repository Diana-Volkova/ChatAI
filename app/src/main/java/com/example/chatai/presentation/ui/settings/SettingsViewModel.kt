package com.example.chatai.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.domain.error.AuthException
import com.example.chatai.domain.usecase.DeleteAccountUseCase
import com.example.chatai.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel@Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {
    private val _effects = MutableSharedFlow<AuthEffect>()
    val effects = _effects.asSharedFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase()

                _effects.emit(AuthEffect.NavigateToLogin)
            } catch (e: AuthException) {
                _error.value = e.message()
            } catch (_: Exception) {
                _error.value = "Не удалось связаться с сервером"
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                deleteAccountUseCase()
                _effects.emit(AuthEffect.NavigateToLogin)
            } catch (e: AuthException) {
                _error.value = e.message()
            } catch (e: IOException) {
                _error.value = "Нет соединения с сервером: " + e.localizedMessage

            } catch (e: Exception) {
                _error.value = "Произошла непредвиденная ошибка" + e.localizedMessage
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}