package com.example.chatai.data.mappers

import com.example.chatai.data.local.ChatSettingsEntity
import com.example.chatai.domain.model.ChatSettings
import com.example.chatai.domain.theme.ChatFontId
import com.example.chatai.domain.theme.ChatThemeId

fun ChatSettingsEntity.toDomain(): ChatSettings {
    return ChatSettings(
        chatId = chatId,
        theme = runCatching {
            ChatThemeId.valueOf(theme)
        }.getOrDefault(ChatThemeId.DEFAULT),
        font = runCatching {
            ChatFontId.valueOf(font)
        }.getOrDefault(ChatFontId.DEFAULT)
    )
}

fun ChatSettings.toEntity(): ChatSettingsEntity {
    return ChatSettingsEntity(
        chatId = chatId,
        theme = theme.name
    )
}

fun String.toChatThemeId(): ChatThemeId {
    return ChatThemeId.entries.firstOrNull { it.name == this }
        ?: ChatThemeId.DEFAULT
}

fun String.toChatFontId() : ChatFontId {
    return ChatFontId.entries.firstOrNull {it.name == this}
        ?: ChatFontId.DEFAULT
}