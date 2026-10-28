package com.example.chatai.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.domain.model.Chat
import com.example.chatai.domain.usecase.ObserveChatsUseCase
import com.example.chatai.domain.usecase.SyncChatsUseCase
import com.example.chatai.presentation.ui.sync.SyncStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeChatsUseCase: ObserveChatsUseCase,
    private val syncChatsUseCase: SyncChatsUseCase
) : ViewModel() {
    val chats: StateFlow<List<Chat>> = observeChatsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Synced)
    val syncStatus = _syncStatus.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _exception = MutableStateFlow<Exception?>(null)
    val exception = _exception.asStateFlow()

    init {
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            _isLoading.value = true
            _exception.value = null
            try {
                syncChatsUseCase()
                _syncStatus.value = SyncStatus.Synced
            } catch (_: IOException) {
                _syncStatus.value = SyncStatus.OfflineCached
            }  catch (e: Exception) {
                _exception.value = e
            } finally {
                _isLoading.value = false
            }
        }
    }
}