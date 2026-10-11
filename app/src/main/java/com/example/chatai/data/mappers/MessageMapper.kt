package com.example.chatai.data.mappers

import com.example.chatai.domain.model.Message
import com.example.chatai.data.remote.dto.MessageDto
import com.example.chatai.domain.model.Sender
import com.example.chatai.data.local.MessageEntity

fun MessageDto.toDomain(chatId: Int): Message {
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
        timestamp = timestamp,
        alternatives = alternatives
    )
}

fun Message.toDto(): MessageDto {
    return MessageDto(
        id = serverId ?: 0,
        text = text,
        sender = when (sender) {
            Sender.USER -> "user"
            Sender.ASSISTANT -> "assistant"
        },
        timestamp = timestamp,
        alternatives = alternatives
    )
}

fun MessageDto.toEntity(chatId: Int): MessageEntity {
    return MessageEntity(
        serverId = id,
        chatId = chatId,
        text = text,
        sender = sender,
        timestamp = timestamp,
        alternatives = alternatives
    )
}

fun Message.toEntity(): MessageEntity {
    return MessageEntity(
        serverId = serverId,
        chatId = chatId,
        text = text,
        sender = when (sender) {
            Sender.USER -> "user"
            Sender.ASSISTANT -> "assistant"
        },
        timestamp = timestamp,
        alternatives = alternatives
    )
}

fun MessageEntity.toDomain(): Message {
    return Message(
        id = id,
        serverId = serverId,
        chatId = chatId,
        text = text,
        sender = when (sender.lowercase()) {
            "user" -> Sender.USER
            "assistant" -> Sender.ASSISTANT
            else -> throw IllegalStateException(
                "Unknown sender: $sender"
            )
        },
        timestamp = timestamp,
        alternatives = alternatives
    )
}