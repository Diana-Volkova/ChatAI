package com.example.chatai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_settings")
data class ChatSettingsEntity(
    @PrimaryKey
    val chatId: Int,
    val theme: String
)