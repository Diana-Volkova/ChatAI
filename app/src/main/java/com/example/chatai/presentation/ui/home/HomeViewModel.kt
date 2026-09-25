package com.example.chatai.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.domain.error.ChatException
import com.example.chatai.domain.model.Chat
import com.example.chatai.domain.usecase.ObserveChatsUseCase
import com.example.chatai.domain.usecase.SyncChatsUseCase
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
    private val observeChatsUseCase: ObserveChatsUseCase,
    private val syncChatsUseCase: SyncChatsUseCase
) : ViewModel() {
    val chats: StateFlow<List<Chat>> = observeChatsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            try {
                syncChatsUseCase()
            } catch (e: ChatException) {
                _error.value = e.message()
            } catch (e: IOException) {
                _error.value = "Нет соединения с сервером: " + e.localizedMessage
            } catch (e: Exception) {
                _error.value = "Не удалось загрузить чаты: " + e.localizedMessage
            }
        }
    }
}