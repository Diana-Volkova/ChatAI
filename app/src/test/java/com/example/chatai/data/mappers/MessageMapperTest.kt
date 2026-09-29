package com.example.chatai.data.mappers

import com.example.chatai.data.local.MessageEntity
import com.example.chatai.data.remote.dto.MessageDto
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

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
    fun `MessageDto toDomain maps assistant sender`() {
        val dto = MessageDto(
            id = 42L,
            chatId = 10,
            text = "Hello",
            sender = "assistant",
            timestamp = 123L
        )

        val result = dto.toDomain()

        assertEquals(Sender.ASSISTANT, result.sender)
    }

    @Test
    fun `MessageDto toDomain is case insensitive`() {
        val dto = MessageDto(
            id = 42L,
            chatId = 10,
            text = "Hello",
            sender = "ASSISTANT",
            timestamp = 123L
        )

        val result = dto.toDomain()

        assertEquals(Sender.ASSISTANT, result.sender)
    }

    @Test
    fun `MessageDto toDomain throws exception for unknown sender`() {
        val dto = MessageDto(
            id = 42L,
            chatId = 10,
            text = "Hello",
            sender = "unknown",
            timestamp = 123L
        )

        assertFailsWith<IllegalStateException> {
            dto.toDomain()
        }
    }

    @Test
    fun `Message toDto uses serverId as id`() {
        val message = Message(
            id = 1L,
            serverId = 42L,
            chatId = 10,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123L
        )

        val result = message.toDto()

        assertEquals(42L, result.id)
        assertEquals(10, result.chatId)
        assertEquals("Hello", result.text)
        assertEquals("user", result.sender)
        assertEquals(123L, result.timestamp)
    }

    @Test
    fun `Message toDto uses zero when serverId is null`() {
        val message = Message(
            id = 1L,
            serverId = null,
            chatId = 10,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123L
        )

        val result = message.toDto()

        assertEquals(0L, result.id)
    }

    @Test
    fun `Message toDto maps assistant sender`() {
        val message = Message(
            id = 1L,
            serverId = 42L,
            chatId = 10,
            text = "Hello",
            sender = Sender.ASSISTANT,
            timestamp = 123L
        )

        val result = message.toDto()

        assertEquals("assistant", result.sender)
    }

    @Test
    fun `Message toEntity creates entity without serverId`() {
        val message = Message(
            id = 1L,
            serverId = 42L,
            chatId = 10,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123L
        )

        val result = message.toEntity()

        assertEquals(null, result.serverId)
        assertEquals(10, result.chatId)
        assertEquals("Hello", result.text)
        assertEquals("user", result.sender)
        assertEquals(123L, result.timestamp)
    }

    @Test
    fun `Message toEntity maps assistant sender`() {
        val message = Message(
            id = 1L,
            serverId = 42L,
            chatId = 10,
            text = "Hello",
            sender = Sender.ASSISTANT,
            timestamp = 123L
        )

        val result = message.toEntity()

        assertEquals("assistant", result.sender)
        assertEquals(null, result.serverId)
    }

    @Test
    fun `MessageDto toEntity preserves serverId`() {
        val dto = MessageDto(
            id = 42L,
            chatId = 10,
            text = "Hello",
            sender = "user",
            timestamp = 123L
        )

        val result = dto.toEntity()

        assertEquals(42L, result.serverId)
        assertEquals(10, result.chatId)
        assertEquals("Hello", result.text)
        assertEquals("user", result.sender)
        assertEquals(123L, result.timestamp)
    }

    @Test
    fun `MessageEntity toDomain maps user sender`() {
        val entity = MessageEntity(
            id = 1L,
            serverId = 42L,
            chatId = 10,
            text = "Hello",
            sender = "user",
            timestamp = 123L
        )

        val result = entity.toDomain()

        assertEquals(1L, result.id)
        assertEquals(42L, result.serverId)
        assertEquals(10, result.chatId)
        assertEquals("Hello", result.text)
        assertEquals(Sender.USER, result.sender)
        assertEquals(123L, result.timestamp)
    }

    @Test
    fun `MessageEntity toDomain maps assistant sender`() {
        val entity = MessageEntity(
            id = 1L,
            serverId = 42L,
            chatId = 10,
            text = "Hello",
            sender = "assistant",
            timestamp = 123L
        )

        val result = entity.toDomain()

        assertEquals(Sender.ASSISTANT, result.sender)
    }

    @Test
    fun `MessageEntity toDomain throws exception for unknown sender`() {
        val entity = MessageEntity(
            id = 1L,
            serverId = 42L,
            chatId = 10,
            text = "Hello",
            sender = "unknown",
            timestamp = 123L
        )

        assertFailsWith<IllegalStateException> {
            entity.toDomain()
        }
    }
}