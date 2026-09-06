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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
    private val api: ChatApi,
    private val dao: MessageDao
) : ChatRepository {

    override fun observeHistory(chatId: Int): Flow<List<Message>> {
        return dao.observeMessages(chatId)
            .map { messages ->
                messages.map { it.toDomain() }
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
                "HTTP ${response.code()}: $error"
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

        if (!response.isSuccessful) {
            throw ChatException(response.code())
        }

        val messages = response.body()
            ?: throw ChatException(response.code())

        dao.replaceChatMessages(
            chatId = chatId,
            messages = messages.map { it.toEntity() }
        )
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