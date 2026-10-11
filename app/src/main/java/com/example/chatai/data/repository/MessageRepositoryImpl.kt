package com.example.chatai.data.repository

import android.util.Log
import com.example.chatai.data.local.ChatDao
import com.example.chatai.data.local.MessageDao
import com.example.chatai.data.mappers.toDomain
import com.example.chatai.data.mappers.toDto
import com.example.chatai.data.mappers.toEntity
import com.example.chatai.data.mappers.toChatEntity
import com.example.chatai.data.remote.api.ChatApi
import com.example.chatai.domain.error.ChatException
import com.example.chatai.domain.model.ChatDetails
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import com.example.chatai.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class MessageRepositoryImpl(
    private val api: ChatApi,
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) : MessageRepository {

    override suspend fun sendMessage(
        chatId: Int,
        message: Message
    ): Message {
        val userMessage = message.copy(chatId = chatId)

        val localId = messageDao.insert(userMessage.toEntity())

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
            messageDao.updateServerId(
                localId = localId,
                serverId = serverId
            )
        }

        val assistantMessage = Message(
            id = body.id,
            serverId = body.id,
            chatId = chatId,
            text = body.text,
            sender = Sender.ASSISTANT,
            timestamp = body.timestamp
        )

        messageDao.insert(assistantMessage.toEntity())

        return assistantMessage
    }

    override suspend fun generateAlternative(
        chatId: Int,
        message: Message
    ): Message {
        val serverId = message.serverId
            ?: throw IllegalStateException(
                "Cannot generate alternative for unsynced message"
            )

        val response = api.generateAlternative(
            chatId = chatId,
            messageId = serverId
        )

        if (!response.isSuccessful) {
            val error = response.errorBody()?.string()
            Log.e(
                "CHAT_API",
                "Alternative generation failed: HTTP ${response.code()}: $error"
            )
            throw ChatException(response.code())
        }

        val body = response.body()
            ?: throw IllegalStateException("Empty alternative response")

        val updatedMessage = body.toDomain(chatId)

        val existing = messageDao.getByServerId(
            chatId = chatId,
            serverId = serverId
        ) ?: throw IllegalStateException(
            "Message not found in local database: $serverId"
        )

        messageDao.update(
            existing.copy(
                text = updatedMessage.text,
                alternatives = updatedMessage.alternatives,
                timestamp = updatedMessage.timestamp
            )
        )

        return updatedMessage.copy(
            id = existing.id,
            serverId = serverId
        )
    }

    override fun observeHistory(chatId: Int): Flow<ChatDetails> {
        return chatDao.observeChatDetails(chatId)
            .filterNotNull()
            .map { details ->
                ChatDetails(
                    chat = details.chat.toDomain(),
                    messages = details.messages.map { it.toDomain() }
                )
            }
    }

    override suspend fun syncChat(chatId: Int) {
        val response = api.getChat(chatId)

        if (!response.isSuccessful) {
            throw ChatException(response.code())
        }

        val chatDetails = response.body()
            ?: throw ChatException(response.code())

        chatDao.insert(
            chatDetails.toChatEntity()
        )

        messageDao.replaceChatMessages(
            chatId = chatId,
            messages = chatDetails.messages.map {
                it.toEntity(chatId)
            }
        )
    }

    override suspend fun clearHistory(chatId: Int) {
        val response = api.deleteMessages(chatId)

        if (!response.isSuccessful) {
            throw ChatException(response.code())
        }
        messageDao.clearChat(chatId)
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

        messageDao.deleteByServerIds(messageIds)
    }

    override suspend fun clearAll() {
        messageDao.clearAll()
    }
}