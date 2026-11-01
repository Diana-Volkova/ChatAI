package com.example.chatai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chatai.domain.theme.ChatFontId
import com.example.chatai.domain.theme.ChatThemeId

@Entity(tableName = "chat_settings")
data class ChatSettingsEntity(
    @PrimaryKey
    val chatId: Int,
    val theme: String = ChatThemeId.DEFAULT.name,
    val font: String = ChatFontId.DEFAULT.name
)