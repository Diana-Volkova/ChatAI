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
        sender = when (sender.lowercase()) {
            "user" -> Sender.USER
            "assistant" -> Sender.ASSISTANT
            else -> throw IllegalStateException(
                "Unknown sender: $sender"
            )
        },
        timestamp = timestamp
    )
}

fun Message.toDto(): MessageDto {
    return MessageDto(
        id = serverId ?: 0,
        chatId = chatId,
        text = text,
        sender = when (sender) {
            Sender.USER -> "user"
            Sender.ASSISTANT -> "assistant"
        },
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
        sender = when (sender) {
            Sender.USER -> "user"
            Sender.ASSISTANT -> "assistant"
        },
        timestamp = timestamp
    )
}

fun MessageEntity.toDomain(): Message {
    return Message(
        id = id,
        serverId = serverId,
        chatId = chatId,
        text = text,
        sender = when (sender) {
            "user" -> Sender.USER
            "assistant" -> Sender.ASSISTANT
            else -> throw IllegalStateException(
                "Unknown sender: $sender"
            )
        },
        timestamp = timestamp
    )
}