package com.example.chatai.data.repository

import android.util.Log
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
        Log.d("CHAT_SYNC", "loadHistory: $chatId")
        val messages = dao.getByChatId(chatId)

        Log.d("CHAT_SYNC", "local messages: $messages")

        return messages.map {
            Log.d("CHAT_SYNC", "mapping: $it")
            it.toDomain()
        }
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

        val localId = dao.insert(userMessage.toEntity())

        val dto = message.toDto()
        Log.d("CHAT_API", "send dto = $dto")

        val response = api.sendMsg(
            chatId = chatId,
            msg = dto
        )

        if (!response.isSuccessful) {
            val error = response.errorBody()?.string()
            Log.e("CHAT_API", "HTTP ${response.code()}: $error")

            throw IllegalStateException(
                "HTTP ${response.code()}: ${
                    response.errorBody()?.string()
                }"
            )
        }

        val body = response.body()
            ?: throw IllegalStateException("Empty response")

        body.userMessageId?.let { serverId ->
            dao.updateServerId(
                localId = localId,
                serverId = serverId
            )
        }

        val assistantMessage = Message(
            id = body.id,
            serverId = body.id,
            chatId = body.chatId,
            text = body.text,
            sender = Sender.ASSISTANT,
            timestamp = body.timestamp
        )

        dao.insert(assistantMessage.toEntity())

        return assistantMessage
    }

    override suspend fun getRemoteMessages(chatId: Int): List<MessageDto> {
        val response = api.getMessages(chatId)

        if (!response.isSuccessful) {
            Log.e(
                "CHAT_SYNC",
                "HTTP ${response.code()}: ${response.errorBody()}"
            )
            throw ChatException(response.code())
        }

        return response.body() ?: emptyList()
    }

    override suspend fun syncMessages(chatId: Int) {
        val response = api.getMessages(chatId)

        Log.d("CHAT_SYNC", "GET messages: ${response.code()}")

        if (!response.isSuccessful) {
            val error = response.errorBody()?.string()

            Log.e("CHAT_SYNC", "GET messages error: $error")

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

    override suspend fun deleteMessagesList(
        chatId: Int,
        messageIds: List<Long>
    ) {
        val response = api.deleteMessagesList(
            chatId = chatId,
            messageIds = messageIds
        )

        if (!response.isSuccessful) {
            val error = response.errorBody()?.string()

            Log.e(
                "CHAT_DELETE",
                "HTTP ${response.code()}: $error"
            )

            throw ChatException(response.code())
        }

        dao.deleteByServerIds(messageIds)
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}