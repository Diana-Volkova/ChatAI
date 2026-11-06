package com.example.chatai.presentation.ui.chat.model

import com.example.chatai.domain.model.Message
import java.time.LocalDate

sealed interface ChatListItem {

    data class MessageItem(
        val message: Message
    ) : ChatListItem

    data class DateSeparator(
        val date: LocalDate
    ) : ChatListItem
}