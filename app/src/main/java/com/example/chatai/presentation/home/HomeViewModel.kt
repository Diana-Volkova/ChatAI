package com.example.chatai.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.data.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _effects = MutableSharedFlow<AuthEffect>()
    val effects = _effects.asSharedFlow()

    fun logout() {
        // todo разлогиниться на сервере
        sessionManager.clear()
        viewModelScope.launch {
            _effects.emit(AuthEffect.NavigateToLogin)
        }
    }
}