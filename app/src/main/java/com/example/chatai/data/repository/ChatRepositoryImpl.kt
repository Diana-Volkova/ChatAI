package com.example.chatai.data.repository

import com.example.chatai.data.local.ChatDao
import com.example.chatai.data.remote.api.ChatApi
import com.example.chatai.data.mappers.toDomain
import com.example.chatai.data.mappers.toEntity
import com.example.chatai.domain.error.ChatException
import com.example.chatai.domain.model.Chat
import com.example.chatai.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
    private val api: ChatApi,
    private val dao: ChatDao
) : ChatRepository {

    override fun observeChats(): Flow<List<Chat>> {
        return dao.observeChats()
            .map { chats ->
                chats.map { it.toDomain() }
            }
    }
    override suspend fun syncChats() {
        val response = api.getChats()

        if (!response.isSuccessful) {
            throw ChatException(response.code())
        }

        val chats = response.body()
            ?: throw ChatException(response.code())

        dao.insertAll(
            chats.map { it.toEntity() }
        )
    }
}