package com.example.chatai.data.repository

import com.example.chatai.data.local.MessageDao
import com.example.chatai.data.mappers.toDto
import com.example.chatai.data.mappers.toEntity
import com.example.chatai.data.remote.api.ChatApi
import com.example.chatai.data.remote.dto.ChatDto
import com.example.chatai.data.remote.dto.MessageDto
import com.example.chatai.domain.error.ChatException
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertFailsWith

class ChatRepositoryImplTest {
    private lateinit var api: ChatApi
    private lateinit var dao: MessageDao
    private lateinit var repository: ChatRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        api = mockk()
        dao = mockk()

        repository = ChatRepositoryImpl(
            api = api,
            dao = dao,
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `loadChats returns chats when response is successful`() = runTest {
        val chats = listOf(
            ChatDto(
                id = 1,
                title = "First chat",
                model = "ia model"
            ),
            ChatDto(
                id = 2,
                title = "Second chat",
                model = "Yura Bulochkin"
            )
        )

        val response = Response.success(chats)

        coEvery {
            api.getChats()
        } returns response

        val result = repository.loadChats()

        assertEquals(chats, result)

        coVerify {
            api.getChats()
        }
    }

    @Test
    fun `loadChats throws ChatException when response is unsuccessful`() = runTest {
        val response = Response.error<List<ChatDto>>(
            401,
            "Unauthorized".toResponseBody()
        )

        coEvery {
            api.getChats()
        } returns response

        try {
            repository.loadChats()

            fail("Expected ChatException")
        } catch (e: ChatException) {
            assertEquals(401, e.code)
        }
    }

    @Test
    fun `loadChats returns empty list when response body is null`() = runTest {
        val response = Response.success<List<ChatDto>>(null)

        coEvery {
            api.getChats()
        } returns response

        val result = repository.loadChats()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `sendMessage saves user message and assistant message when response is successful`() = runTest {
        val chatId = 10

        val message = Message(
            id = 0L,
            serverId = null,
            chatId = 0,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123456L,
        )

        val responseBody = MessageDto(
            id = 2L,
            chatId = chatId,
            text = "Hello!",
            timestamp = 123457L,
            userMessageId = 200L,
            sender = "assistant"
        )

        coEvery {
            dao.insert(any())
        } returnsMany listOf(100L, 101L)

        coEvery {
            api.sendMsg(
                chatId = chatId,
                msg = message.toDto()
            )
        } returns Response.success(responseBody)

        coEvery {
            dao.updateServerId(
                localId = 100L,
                serverId = 200L
            )
        } just Runs

        val result = repository.sendMessage(
            chatId = chatId,
            message = message,
        )

        assertEquals(
            Message(
                id = 2L,
                serverId = 2L,
                chatId = chatId,
                text = "Hello!",
                sender = Sender.ASSISTANT,
                timestamp = 123457L,
            ),
            result
        )

        coVerify(exactly = 1) {
            dao.insert(message.copy(chatId = chatId).toEntity())
        }

        coVerify(exactly = 2) {
            dao.insert(any())
        }

        coVerify(exactly = 1) {
            dao.updateServerId(
                localId = 100L,
                serverId = 200L
            )
        }

        coVerify(exactly = 1) {
            api.sendMsg(
                chatId = chatId,
                msg = message.toDto()
            )
        }
    }

    @Test
    fun `sendMessage throws IllegalStateException when response is unsuccessful`() = runTest {
        val message = Message(
            id = 1L,
            serverId = null,
            chatId = 0,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123456L,
        )

        coEvery {
            dao.insert(any())
        } returns 100L

        coEvery {
            api.sendMsg(
                chatId = 10,
                msg = any()
            )
        } returns Response.error(
            500,
            "Internal Server Error".toResponseBody()
        )

        try {
            repository.sendMessage(
                chatId = 10,
                message = message,
            )

            fail("Expected IllegalStateException")
        } catch (e: IllegalStateException) {
            assertEquals(
                "HTTP 500: Internal Server Error",
                e.message
            )
        }
    }

    @Test
    fun `sendMessage does not update server id when userMessageId is null`() = runTest {
        val message = Message(
            id = 1L,
            serverId = null,
            chatId = 0,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123456L,
        )

        val responseBody = MessageDto(
            id = 2L,
            chatId = 10,
            text = "Hello!",
            timestamp = 123457L,
            userMessageId = null,
            sender = "assistant"
        )

        coEvery {
            dao.insert(any())
        } returns 100L

        coEvery {
            api.sendMsg(
                chatId = 1,
                msg = any()
            )
        } returns Response.success(responseBody)

        val result = repository.sendMessage(
            chatId = 1,
            message = message,
        )

        assertEquals(2L, result.id)
        assertEquals(Sender.ASSISTANT, result.sender)

        coVerify(exactly = 0) {
            dao.updateServerId(any(), any())
        }

        coVerify(exactly = 2) {
            dao.insert(any())
        }
    }

    @Test
    fun `getRemoteMessages returns messages when response is successful`() = runTest {
        val chatId = 1

        val messages = listOf(
            MessageDto(
                id = 1L,
                chatId = chatId,
                text = "Hello",
                sender = "USER",
                timestamp = 123456L,
            ),
            MessageDto(
                id = 2L,
                chatId = chatId,
                text = "Hi!",
                sender = "ASSISTANT",
                timestamp = 123457L,
            )
        )

        val response = Response.success(messages)

        coEvery {
            api.getMessages(chatId)
        } returns response

        val result = repository.getRemoteMessages(chatId)

        assertEquals(messages, result)

        coVerify(exactly = 1) {
            api.getMessages(chatId)
        }
    }

    @Test
    fun `getRemoteMessages throws ChatException when response is unsuccessful`() = runTest {
        val chatId = 1

        val response = Response.error<List<MessageDto>>(
            401,
            "Unauthorized".toResponseBody()
        )

        coEvery {
            api.getMessages(chatId)
        } returns response

        val exception = assertFailsWith<ChatException> {
            repository.getRemoteMessages(chatId)
        }

        assertEquals(401, exception.code)

        coVerify(exactly = 1) {
            api.getMessages(chatId)
        }
    }

    @Test
    fun `getRemoteMessages returns empty list when response body is null`() = runTest {
        val chatId = 1

        val response = Response.success<List<MessageDto>>(null)

        coEvery {
            api.getMessages(chatId)
        } returns response

        val result = repository.getRemoteMessages(chatId)

        assertTrue(result.isEmpty())

        coVerify(exactly = 1) {
            api.getMessages(chatId)
        }
    }

    @Test
    fun `syncMessages clears chat and inserts messages when response is successful`() = runTest {
        val chatId = 1

        val messages = listOf(
            MessageDto(
                id = 1L,
                chatId = chatId,
                text = "Hello",
                sender = "USER",
                timestamp = 123456L,
            ),
            MessageDto(
                id = 2L,
                chatId = chatId,
                text = "Hi!",
                sender = "ASSISTANT",
                timestamp = 123457L,
            )
        )

        val response = Response.success(messages)

        coEvery {
            api.getMessages(chatId)
        } returns response

        coEvery {
            dao.clearChat(chatId)
        } just Runs

        coEvery {
            dao.insert(any())
        } returns 1L

        repository.syncMessages(chatId)

        coVerify(exactly = 1) {
            api.getMessages(chatId)
        }

        coVerify(exactly = 1) {
            dao.clearChat(chatId)
        }

        coVerify(exactly = 2) {
            dao.insert(any())
        }
    }

    @Test
    fun `syncMessages throws ChatException when response is unsuccessful`() = runTest {
        val chatId = 1

        val response = Response.error<List<MessageDto>>(
            500,
            "Internal Server Error".toResponseBody()
        )

        coEvery {
            api.getMessages(chatId)
        } returns response

        val exception = assertFailsWith<ChatException> {
            repository.syncMessages(chatId)
        }

        assertEquals(500, exception.code)

        coVerify(exactly = 1) {
            api.getMessages(chatId)
        }

        coVerify(exactly = 0) {
            dao.clearChat(any())
        }

        coVerify(exactly = 0) {
            dao.insert(any())
        }
    }

    @Test
    fun `syncMessages throws ChatException when response body is null`() = runTest {
        val chatId = 1

        val response = Response.success<List<MessageDto>>(null)

        coEvery {
            api.getMessages(chatId)
        } returns response

        val exception = assertFailsWith<ChatException> {
            repository.syncMessages(chatId)
        }

        assertEquals(200, exception.code)

        coVerify(exactly = 1) {
            api.getMessages(chatId)
        }

        coVerify(exactly = 0) {
            dao.clearChat(any())
        }

        coVerify(exactly = 0) {
            dao.insert(any())
        }
    }

    @Test
    fun `syncMessages clears chat and does not insert anything when messages are empty`() = runTest {
        val chatId = 1

        val response = Response.success(emptyList<MessageDto>())

        coEvery {
            api.getMessages(chatId)
        } returns response

        coEvery {
            dao.clearChat(chatId)
        } just Runs

        repository.syncMessages(chatId)

        coVerify(exactly = 1) {
            api.getMessages(chatId)
        }

        coVerify(exactly = 1) {
            dao.clearChat(chatId)
        }

        coVerify(exactly = 0) {
            dao.insert(any())
        }
    }

    @Test
    fun `clearHistory clears chat when response is successful`() = runTest {
        val chatId = 10

        val response = Response.success<Unit>(Unit)

        coEvery {
            api.deleteMessages(chatId)
        } returns response

        coEvery {
            dao.clearChat(chatId)
        } just Runs

        repository.clearHistory(chatId)

        coVerify(exactly = 1) {
            api.deleteMessages(chatId)
        }

        coVerify(exactly = 1) {
            dao.clearChat(chatId)
        }
    }

    @Test
    fun `clearHistory throws ChatException when response is unsuccessful`() = runTest {
        val chatId = 10

        val response = Response.error<Unit>(
            500,
            "Internal Server Error".toResponseBody()
        )

        coEvery {
            api.deleteMessages(chatId)
        } returns response

        try {
            repository.clearHistory(chatId)

            fail("Expected ChatException")
        } catch (e: ChatException) {
            assertEquals(500, e.code)
        }

        coVerify(exactly = 1) {
            api.deleteMessages(chatId)
        }

        coVerify(exactly = 0) {
            dao.clearChat(any())
        }
    }

    @Test
    fun `deleteMessagesList deletes messages when response is successful`() = runTest {
        val chatId = 10
        val messageIds = listOf(101L, 102L, 103L)

        val response = Response.success<Unit>(Unit)

        coEvery {
            api.deleteMessagesList(
                chatId = chatId,
                messageIds = messageIds
            )
        } returns response

        coEvery {
            dao.deleteByServerIds(messageIds)
        } just Runs

        repository.deleteMessagesList(
            chatId = chatId,
            messageIds = messageIds
        )

        coVerify(exactly = 1) {
            api.deleteMessagesList(
                chatId = chatId,
                messageIds = messageIds
            )
        }

        coVerify(exactly = 1) {
            dao.deleteByServerIds(messageIds)
        }
    }

    @Test
    fun `deleteMessagesList throws ChatException when response is unsuccessful`() = runTest {
        val chatId = 10
        val messageIds = listOf(101L, 102L)

        val response = Response.error<Unit>(
            404,
            "Not Found".toResponseBody()
        )

        coEvery {
            api.deleteMessagesList(
                chatId = chatId,
                messageIds = messageIds
            )
        } returns response

        try {
            repository.deleteMessagesList(
                chatId = chatId,
                messageIds = messageIds
            )

            fail("Expected ChatException")
        } catch (e: ChatException) {
            assertEquals(404, e.code)
        }

        coVerify(exactly = 1) {
            api.deleteMessagesList(
                chatId = chatId,
                messageIds = messageIds
            )
        }

        coVerify(exactly = 0) {
            dao.deleteByServerIds(any())
        }
    }

    @Test
    fun `clearAll clears all messages`() = runTest {
        coEvery {
            dao.clearAll()
        } just Runs

        repository.clearAll()

        coVerify(exactly = 1) {
            dao.clearAll()
        }
    }
}