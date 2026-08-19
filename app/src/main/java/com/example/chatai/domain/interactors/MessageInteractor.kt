package com.example.chatai.domain.interactors

import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import com.example.chatai.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessageInteractor @Inject constructor(
    private val repo: ChatRepository
) {
    fun sendMessage(
        chatId: Int,
        text: String
    ): Flow<Message> = flow {
        val userMessage = Message(
            id = 0,
            chatId = chatId,
            text = text,
            sender = Sender.USER,
            timestamp = System.currentTimeMillis()
        )

        emit(userMessage)

        emit(repo.sendMessage(chatId, userMessage))
    }
}