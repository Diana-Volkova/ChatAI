package com.example.chatai.data.mappers

import com.example.chatai.data.local.ChatEntity
import com.example.chatai.data.remote.dto.ChatDto
import com.example.chatai.domain.model.Chat

fun ChatDto.toDomain(): Chat {
    return Chat(
        id = id,
        title = title,
        model = model,
        lastMessageAt = lastMessageAt
    )
}

fun Chat.toDto(): ChatDto {
    return ChatDto(
        id = id,
        title = title,
        model = model,
        lastMessageAt = lastMessageAt
    )
}

fun ChatDto.toEntity(): ChatEntity {
    return ChatEntity(
        chatId = id,
        title = title,
        model = model,
        lastMessageAt = lastMessageAt
    )
}

fun Chat.toEntity(): ChatEntity {
    return ChatEntity(
        chatId = id,
        title = title,
        model = model,
        lastMessageAt = lastMessageAt
    )
}

fun ChatEntity.toDomain(): Chat {
    return Chat(
        id = chatId,
        title = title,
        model = model,
        lastMessageAt = lastMessageAt
    )
}