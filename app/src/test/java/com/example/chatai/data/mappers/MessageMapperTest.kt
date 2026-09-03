package com.example.chatai.data.mappers

import com.example.chatai.data.remote.dto.MessageDto
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import org.junit.Assert.*
import org.junit.Test

class MessageMapperTest {
    @Test
    fun `MessageDto toDomain maps user sender`() {
        val dto = MessageDto(
            id = 42L,
            chatId = 10,
            text = "Hello",
            sender = "user",
            timestamp = 123L
        )

        val result = dto.toDomain()

        assertEquals(42L, result.id)
        assertEquals(42L, result.serverId)
        assertEquals(10, result.chatId)
        assertEquals("Hello", result.text)
        assertEquals(Sender.USER, result.sender)
        assertEquals(123L, result.timestamp)
    }

    @Test
    fun `MessageDto toDomain maps non-user sender to assistant`() {
        val dto = MessageDto(
            id = 42,
            chatId = 10,
            text = "Hello",
            sender = "assistant",
            timestamp = 123L
        )

        assertEquals(Sender.ASSISTANT, dto.toDomain().sender)
    }

    @Test
    fun `Message toDto uses serverId as id`() {
        val message = Message(
            id = 1,
            serverId = 42,
            chatId = 10,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123L
        )

        assertEquals(42, message.toDto().id)
    }

    @Test
    fun `Message toDto uses zero when serverId is null`() {
        val message = Message(
            id = 1,
            serverId = null,
            chatId = 10,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123L
        )

        assertEquals(0, message.toDto().id)
    }

    @Test
    fun `Message toEntity creates entity without serverId`() {
        val message = Message(
            id = 1,
            serverId = 42,
            chatId = 10,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123L
        )

        val entity = message.toEntity()

        assertEquals(null, entity.serverId)
        assertEquals(10, entity.chatId)
        assertEquals("Hello", entity.text)
        assertEquals("USER", entity.sender)
        assertEquals(123L, entity.timestamp)
    }

}