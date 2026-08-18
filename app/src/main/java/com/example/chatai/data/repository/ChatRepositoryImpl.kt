package com.example.chatai.data.repository

import com.example.chatai.data.remote.api.ChatApi
import com.example.chatai.domain.model.Sender
import com.example.chatai.domain.model.Message
import com.example.chatai.data.local.MessageDao
import com.example.chatai.data.mappers.toDto
import com.example.chatai.data.mappers.toDomain
import com.example.chatai.data.mappers.toEntity
import com.example.chatai.data.remote.dto.ChatDto
import com.example.chatai.data.remote.dto.MessageDto
import com.example.chatai.domain.error.ChatException
import com.example.chatai.domain.repository.ChatRepository

class ChatRepositoryImpl(
    private val api: ChatApi,
    private val dao: MessageDao
) : ChatRepository {
    override suspend fun loadHistory(chatId: Int): List<Message> {
        return dao.getByChatId(chatId).map { it.toDomain() }
    }

    override suspend fun loadChats(): List<ChatDto> {
        val response = api.getChats()

        if (!response.isSuccessful) {
            throw ChatException(response.code())
        }

        return response.body() ?: emptyList()
    }

    override suspend fun sendMessage(
        chatId: Int,
        message: Message
    ): Message {
        val userMessage = message.copy(chatId = chatId)

        dao.insert(userMessage.toEntity())

        val response = api.sendMsg(
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

        val body = response.body()
            ?: throw IllegalStateException("Empty response")

        val assistantMessage = Message(
            id = body.id,
            chatId = body.chatId,
            text = body.text,
            sender = if (body.sender == "user") {
                Sender.USER
            } else {
                Sender.ASSISTANT
            },
            timestamp = body.timestamp
        )

        dao.insert(assistantMessage.toEntity())

        return assistantMessage
    }

    override suspend fun getRemoteMessages(chatId: Int): List<MessageDto> {
        val response = api.getMessages(chatId)

        if (!response.isSuccessful) {
            throw ChatException(response.code())
        }

        return response.body() ?: emptyList()
    }

    override suspend fun syncMessages(chatId: Int) {
        val response = api.getMessages(chatId)

        if (!response.isSuccessful) {
            throw ChatException(response.code())
        }

        val messages = response.body()
            ?: throw ChatException(response.code())

        dao.clearChat(chatId)

        messages.forEach { message ->
            dao.insert(message.toEntity())
        }
    }

    override suspend fun clearHistory(chatId: Int) {
        val response = api.deleteMessages(chatId)

        if (!response.isSuccessful) {
            throw ChatException(response.code())
        }
        dao.clearChat(chatId)
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}