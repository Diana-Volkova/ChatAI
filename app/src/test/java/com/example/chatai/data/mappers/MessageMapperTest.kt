package com.example.chatai.data.mappers

import com.example.chatai.data.local.MessageEntity
import com.example.chatai.data.remote.dto.MessageDto
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.test.assertFailsWith

class MessageMapperTest {

    private val chatId = 10
    private val timestamp = 123L
    private val messageText = "Hello"
    private val alternatives = emptyList<String>()

    private fun createDto(
        sender: String = "user",
        id: Long = 42L
    ) = MessageDto(
        id = id,
        text = messageText,
        sender = sender,
        timestamp = timestamp,
        alternatives = alternatives
    )

    private fun createMessage(
        sender: Sender = Sender.USER,
        serverId: Long? = 42L
    ) = Message(
        id = 1L,
        serverId = serverId,
        chatId = chatId,
        text = messageText,
        sender = sender,
        timestamp = timestamp,
        alternatives = alternatives
    )

    private fun createEntity(
        sender: String = "user"
    ) = MessageEntity(
        id = 1L,
        serverId = 42L,
        chatId = chatId,
        text = messageText,
        sender = sender,
        timestamp = timestamp,
        alternatives = alternatives
    )

// MessageDto -> Message

    @Test
    fun `MessageDto toDomain maps user sender`() {
        val result = createDto().toDomain(chatId)

        assertEquals(42L, result.id)
        assertEquals(42L, result.serverId)
        assertEquals(chatId, result.chatId)
        assertEquals(messageText, result.text)
        assertEquals(Sender.USER, result.sender)
        assertEquals(timestamp, result.timestamp)
        assertEquals(alternatives, result.alternatives)
    }

    @Test
    fun `MessageDto toDomain maps assistant sender`() {
        val result = createDto(sender = "assistant").toDomain(chatId)

        assertEquals(Sender.ASSISTANT, result.sender)
    }

    @Test
    fun `MessageDto toDomain ignores sender case`() {
        val result = createDto(sender = "ASSISTANT").toDomain(chatId)

        assertEquals(Sender.ASSISTANT, result.sender)
    }

    @Test
    fun `MessageDto toDomain uses provided chatId`() {
        val result = createDto().toDomain(chatId = 99)

        assertEquals(99, result.chatId)
    }

    @Test
    fun `MessageDto toDomain throws for unknown sender`() {
        val dto = createDto(sender = "unknown")

        val exception = assertFailsWith<IllegalStateException> {
            dto.toDomain(chatId)
        }

        assertEquals("Unknown sender: unknown", exception.message)
    }

// Message -> MessageDto

    @Test
    fun `Message toDto maps user sender and fields`() {
        val result = createMessage().toDto()

        assertEquals(42L, result.id)
        assertEquals(messageText, result.text)
        assertEquals("user", result.sender)
        assertEquals(timestamp, result.timestamp)
        assertEquals(alternatives, result.alternatives)
    }

    @Test
    fun `Message toDto maps assistant sender`() {
        val result = createMessage(sender = Sender.ASSISTANT).toDto()

        assertEquals("assistant", result.sender)
    }

    @Test
    fun `Message toDto uses zero when serverId is null`() {
        val result = createMessage(serverId = null).toDto()

        assertEquals(0L, result.id)
    }

    @Test
    fun `MessageDto toEntity maps all fields`() {
        val result = createDto().toEntity(chatId)

        assertEquals(42L, result.serverId)
        assertEquals(chatId, result.chatId)
        assertEquals(messageText, result.text)
        assertEquals("user", result.sender)
        assertEquals(timestamp, result.timestamp)
        assertEquals(alternatives, result.alternatives)
    }

    @Test
    fun `MessageDto toEntity uses provided chatId`() {
        val result = createDto().toEntity(chatId = 99)

        assertEquals(99, result.chatId)
    }

// Message -> MessageEntity

    @Test
    fun `Message toEntity preserves serverId`() {
        val result = createMessage(serverId = 42L).toEntity()

        assertEquals(42L, result.serverId)
        assertEquals(chatId, result.chatId)
        assertEquals(messageText, result.text)
        assertEquals("user", result.sender)
        assertEquals(timestamp, result.timestamp)
        assertEquals(alternatives, result.alternatives)
    }

    @Test
    fun `Message toEntity preserves null serverId`() {
        val result = createMessage(serverId = null).toEntity()

        assertNull(result.serverId)
    }

    @Test
    fun `Message toEntity maps assistant sender`() {
        val result = createMessage(sender = Sender.ASSISTANT).toEntity()

        assertEquals("assistant", result.sender)
    }

// MessageEntity -> Message

    @Test
    fun `MessageEntity toDomain maps user sender and fields`() {
        val result = createEntity().toDomain()

        assertEquals(1L, result.id)
        assertEquals(42L, result.serverId)
        assertEquals(chatId, result.chatId)
        assertEquals(messageText, result.text)
        assertEquals(Sender.USER, result.sender)
        assertEquals(timestamp, result.timestamp)
        assertEquals(alternatives, result.alternatives)
    }

    @Test
    fun `MessageEntity toDomain maps assistant sender`() {
        val result = createEntity(sender = "assistant").toDomain()

        assertEquals(Sender.ASSISTANT, result.sender)
    }

    @Test
    fun `MessageEntity toDomain ignores sender case`() {
        val result = createEntity(sender = "ASSISTANT").toDomain()

        assertEquals(Sender.ASSISTANT, result.sender)
    }

    @Test
    fun `MessageEntity toDomain throws for unknown sender`() {
        val entity = createEntity(sender = "unknown")

        val exception = assertFailsWith<IllegalStateException> {
            entity.toDomain()
        }

        assertEquals("Unknown sender: unknown", exception.message)
    }

}