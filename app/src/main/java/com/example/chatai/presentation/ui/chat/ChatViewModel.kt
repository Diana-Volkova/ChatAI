package com.example.chatai.presentation.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.repository.ChatRepository
import com.example.chatai.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repo: ChatRepository,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<ChatState>(ChatState.Loading)
    val state: StateFlow<ChatState> = _state.asStateFlow()

    fun loadHistory(chatId: Int) {
        viewModelScope.launch {
            try {
                val history = repo.loadHistory(chatId)
                _state.value = ChatState.Success(history)
            } catch (e: Exception) {
                _state.value = ChatState.Error(e.message ?: "error")
            }
        }
    }

    fun dispatch(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.SendMessage -> {
                viewModelScope.launch {
                    try {
                        sendMessageUseCase(intent.chatId, intent.text)
                            .collect(::addMessage)
                    } catch (e: Exception) {
                        _state.value = ChatState.Error(e.message ?: "error")
                    }
                }
            }
        }
    }

    private fun addMessage(message: Message) {
        _state.update { state ->
            when (state) {
                is ChatState.Success -> state.copy(messages = state.messages + message)
                else -> state
            }
        }
    }
}