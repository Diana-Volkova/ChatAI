package com.example.chatai.presentation.ui.chat

import com.example.chatai.domain.model.Message

sealed interface ChatState {

    data object Loading : ChatState

    data class Success(
        val messages: List<Message> = emptyList()
    ) : ChatState

    data class Error(
        val message: String
    ) : ChatState
}