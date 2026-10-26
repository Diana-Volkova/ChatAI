package com.example.chatai.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.domain.usecase.DeleteAccountUseCase
import com.example.chatai.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _effects = MutableSharedFlow<AuthEffect>()
    val effects = _effects.asSharedFlow()

    private val _exception = MutableStateFlow<Exception?>(null)
    val exception = _exception.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase()
                _effects.emit(AuthEffect.NavigateToLogin)
            } catch (e: Exception) {
                _exception.value = e
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                deleteAccountUseCase()
                _effects.emit(AuthEffect.NavigateToLogin)
            } catch (e: Exception) {
                _exception.value = e
            }
        }
    }

    fun clearError() {
        _exception.value = null
    }
}