package com.example.chatai.data.mappers

import com.example.chatai.data.local.ChatEntity
import com.example.chatai.data.remote.dto.ChatDetailsDto
import com.example.chatai.domain.model.Chat
import com.example.chatai.domain.model.ChatDetails

fun ChatDetailsDto.toDomain(): ChatDetails {
    return ChatDetails(
        chat = Chat(
            id = id,
            title = title,
            model = model,
            lastMessageAt = lastMessageAt
        ),
        messages = messages.map { it.toDomain(id) }
    )
}

fun ChatDetailsDto.toChatEntity(): ChatEntity {
    return ChatEntity(
        chatId = id,
        title = title,
        model = model,
        lastMessageAt = lastMessageAt
    )
}