package com.example.chatai.data.mappers

import com.example.chatai.domain.model.Message
import com.example.chatai.data.remote.dto.MessageDto
import com.example.chatai.domain.model.Sender
import com.example.chatai.data.local.MessageEntity

fun MessageDto.toDomain(): Message {
    return Message(
        id = id,
        serverId = id,
        chatId = chatId,
        text = text,
        sender = if (sender == "user") Sender.USER else Sender.ASSISTANT,
        timestamp = timestamp
    )
}

fun Message.toDto(): MessageDto {
    return MessageDto(
        id = serverId ?: 0,
        chatId = chatId,
        text = text,
        sender = sender.name.lowercase(),
        timestamp = timestamp
    )
}

fun MessageDto.toEntity(): MessageEntity {
    return MessageEntity(
        serverId = id,
        chatId = chatId,
        text = text,
        sender = sender,
        timestamp = timestamp
    )
}

fun Message.toEntity(): MessageEntity {
    return MessageEntity(
        serverId = null,
        chatId = chatId,
        text = text,
        sender = sender.name,
        timestamp = timestamp
    )
}

fun MessageEntity.toDomain(): Message {
    return Message(
        id = serverId ?: id,
        chatId = chatId,
        text = text,
        sender = if (sender == "user") Sender.USER else Sender.ASSISTANT,
        timestamp = timestamp
    )
}