package com.example.chatai.data.repository

import com.example.chatai.data.ChatApi
import com.example.chatai.data.Sender
import com.example.chatai.data.Message
import com.example.chatai.data.local.MessageDao
import com.example.chatai.data.mappers.toDto
import com.example.chatai.data.mappers.toDomain
import com.example.chatai.data.mappers.toEntity

class ChatRepository(
    private val api: ChatApi,
    private val dao: MessageDao
) {
    suspend fun loadHistory(): List<Message> {
        return dao.getAll().map { it.toDomain() }
    }

    suspend fun sendMessage(
        chatId: Int,
        message: Message
    ): Message {
        dao.insert(
            message.toEntity()
        )

        val response =
            api.sendMsg(
                chatId = chatId,
                message.toDto()
            )

        if (!response.isSuccessful) {

            throw IllegalStateException(
                "HTTP ${response.code()}: ${
                    response.errorBody()?.string()
                }"
            )
        }

        val body =
            response.body()
                ?: throw IllegalStateException(
                    "Empty response"
                )

        val assistantMessage = Message(
            id = 0,
            text = body.text,
            sender = Sender.ASSISTANT,
            timestamp = body.timestamp
        )

        dao.insert(
            assistantMessage.toEntity()
        )
        return assistantMessage
    }

    suspend fun clearHistory() {
        dao.clear()
    }
}