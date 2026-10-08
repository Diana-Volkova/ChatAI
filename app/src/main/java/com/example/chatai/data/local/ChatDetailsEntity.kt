package com.example.chatai.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class ChatDetailsEntity(
    @Embedded
    val chat: ChatEntity,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId"
    )
    val messages: List<MessageEntity>
)