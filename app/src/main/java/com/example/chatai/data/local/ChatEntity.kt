package com.example.chatai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey
    val chatId: Int,
    val title: String,
    val model: String,
    val lastMessageAt: Long?
)