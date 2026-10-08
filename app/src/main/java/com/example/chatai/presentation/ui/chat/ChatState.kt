package com.example.chatai.presentation.ui.chat

import com.example.chatai.domain.model.ChatDetails

sealed interface ChatState {

    data object Loading : ChatState

    data class Success(
        val data: ChatDetails
    ) : ChatState

    data class Error(
        val message: String
    ) : ChatState
}