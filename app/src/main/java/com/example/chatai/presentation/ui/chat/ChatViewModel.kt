package com.example.chatai.presentation.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.domain.interactors.HistoryInteractor
import com.example.chatai.domain.interactors.MessageInteractor
import com.example.chatai.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val historyInteractor: HistoryInteractor,
    private val messageInteractor: MessageInteractor
) : ViewModel() {
    private val _state = MutableStateFlow<ChatState>(ChatState.Loading)
    val state: StateFlow<ChatState> = _state.asStateFlow()

    fun loadHistory(chatId: Int) {
        viewModelScope.launch {
            try {
                val history = historyInteractor.loadHistory(chatId)
                _state.value = ChatState.Success(history)

                historyInteractor.syncHistory(chatId)

                val syncedHistory = historyInteractor.loadHistory(chatId)
                _state.value = ChatState.Success(syncedHistory)

            } catch (e: Exception) {
                _state.value = ChatState.Error(e.message ?: "error")
            }
        }
    }

    fun clearHistory(chatId: Int) {
        viewModelScope.launch {
            try {
                historyInteractor.clearHistory(chatId)
                _state.value = ChatState.Success(emptyList())
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
                        messageInteractor.sendMessage(intent.chatId, intent.text)
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

    fun deleteMessage(chatId: Int, messageId: Long?) {
        if (messageId == null) {
            return
        }
        viewModelScope.launch {
            try {
                messageInteractor.deleteMessage(chatId, messageId)

                val history = historyInteractor.loadHistory(chatId)
                _state.value = ChatState.Success(history)

            } catch (e: Exception) {
                _state.value = ChatState.Error(e.message ?: "error")
            }
        }
    }
}