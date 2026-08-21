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

    fun dispatch(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.LoadHistory -> {
                loadHistory(intent.chatId)
            }
            is ChatIntent.SendMessage -> {
                sendMessage(intent.chatId, intent.text)
            }

            is ChatIntent.DeleteMessages -> {
                deleteMessages(intent.chatId, intent.messageIds)
            }

            is ChatIntent.ClearHistory -> {
                clearHistory(intent.chatId)
            }
        }
    }

    private fun sendMessage(chatId: Int, text: String) {
        viewModelScope.launch {
            try {
                messageInteractor.sendMessage(chatId, text)
                    .collect(::addMessage)
            } catch (e: Exception) {
                _state.value = ChatState.Error(e.message ?: "error")
            }
        }
    }

    private fun loadHistory(chatId: Int) {
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

    private fun addMessage(message: Message) {
        _state.update { state ->
            when (state) {
                is ChatState.Success -> state.copy(messages = state.messages + message)
                else -> state
            }
        }
    }

    private fun deleteMessages(chatId: Int, messageIds: List<Long>) {
        viewModelScope.launch {
            try {
                messageInteractor.deleteMessages(chatId, messageIds)

                val history = historyInteractor.loadHistory(chatId)
                _state.value = ChatState.Success(history)

            } catch (e: Exception) {
                _state.value = ChatState.Error(e.message ?: "error")
            }
        }
    }

    private fun clearHistory(chatId: Int) {
        viewModelScope.launch {
            try {
                historyInteractor.clearHistory(chatId)
                _state.value = ChatState.Success(emptyList())
            } catch (e: Exception) {
                _state.value = ChatState.Error(e.message ?: "error")
            }
        }
    }
}