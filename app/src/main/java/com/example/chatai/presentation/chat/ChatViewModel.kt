package com.example.chatai.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.data.Message
import com.example.chatai.data.Sender
import com.example.chatai.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repo: ChatRepository
) : ViewModel() {
    private val _state = MutableStateFlow<ChatState>(ChatState.Loading)
    val state: StateFlow<ChatState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val history = repo.loadHistory()
            _state.value = ChatState.Success(history)
        }
    }

    fun dispatch(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.SendMessage -> {
                sendMessage(intent.text)
            }
        }
    }

    fun sendMessage(text: String) {
        val userMessage = Message(
            id = 0,
            text = text,
            sender = Sender.USER,
            timestamp = System.currentTimeMillis()
        )

        addMessage(userMessage)

        viewModelScope.launch {
            try {
                val response = repo.sendMessage(userMessage)
                addMessage(response)
            } catch (e: Exception) {
                _state.value = ChatState.Error(e.message ?: "error")
            }
        }
    }

    private fun addMessage(message: Message) {
        _state.update { state ->
            if (state is ChatState.Success) {
                state.copy(messages = state.messages + message)
            } else state
        }
    }
}