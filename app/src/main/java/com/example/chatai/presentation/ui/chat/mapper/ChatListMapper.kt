package com.example.chatai.presentation.ui.chat.mapper

import com.example.chatai.domain.model.Message
import com.example.chatai.presentation.ui.chat.model.ChatListItem
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object ChatListMapper {

    fun map(
        messages: List<Message>,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): List<ChatListItem> {

        val result = mutableListOf<ChatListItem>()
        var previousDate: LocalDate? = null

        messages
            .sortedWith(
                compareBy<Message> { it.timestamp }
                    .thenBy { it.id }
            )
            .forEach { message ->

                val date = Instant
                    .ofEpochMilli(message.timestamp)
                    .atZone(zoneId)
                    .toLocalDate()

                if (date != previousDate) {
                    result.add(
                        ChatListItem.DateSeparator(date)
                    )
                    previousDate = date
                }

                result.add(
                    ChatListItem.MessageItem(message)
                )
            }

        return result
    }
}