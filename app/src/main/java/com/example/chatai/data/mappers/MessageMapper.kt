package com.example.chatai.data.mappers

import com.example.chatai.domain.model.Message
import com.example.chatai.data.remote.dto.MessageDto
import com.example.chatai.domain.model.Sender
import com.example.chatai.data.local.MessageEntity

fun MessageDto.toDomain(chatId: Int): Message {
    return Message(
        id = 0,
        chatId = chatId,
        text = text,
        sender = if (sender == "user") Sender.USER else Sender.ASSISTANT,
        timestamp = timestamp
    )
}

fun Message.toDto(): MessageDto {
    return MessageDto(
        text = text,
        sender = sender.name.lowercase(),
        timestamp = timestamp
    )
}

fun Message.toEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        chatId = chatId,
        text = text,
        sender = sender.name,
        timestamp = timestamp
    )
}

fun MessageEntity.toDomain(): Message {
    return Message(
        id = id,
        chatId = chatId,
        text = text,
        sender = Sender.valueOf(sender),
        timestamp = timestamp
    )
}