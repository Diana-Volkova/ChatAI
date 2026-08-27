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
        val message = Message(
            id = 1L,
            serverId = null,
            chatId = 0,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123456L,
        )

        val assistantResponse = MessageDto(
            id = 2L,
            chatId = 10,
            text = "Hello! How can I help?",
            timestamp = 123457L,
            userMessageId = 200L,
            sender = "assistant"
        )

        coEvery {
            dao.insert(message.copy(chatId = 10).toEntity())
        } returns 100L

        coEvery {
            dao.insert(any())
        } returns 101L

        coEvery {
            api.sendMsg(
                chatId = 10,
                msg = message.toDto()
            )
        } returns Response.success(assistantResponse)

        coEvery {
            dao.updateServerId(
                localId = 100L,
                serverId = 200L
            )
        } just Runs

        val result = repository.sendMessage(
            chatId = 10,
            message = message,
        )

        assertEquals(
            Message(
                id = 2L,
                serverId = 2L,
                chatId = 10,
                text = "Hello! How can I help?",
                sender = Sender.ASSISTANT,
                timestamp = 123457L,
            ),
            result
        )

        coVerify(exactly = 1) {
            dao.insert(message.copy(chatId = 10).toEntity())
        }

        coVerify(exactly = 1) {
            api.sendMsg(
                chatId = 10,
                msg = message.toDto()
            )
        }

        coVerify(exactly = 1) {
            dao.updateServerId(
                localId = 100L,
                serverId = 200L
            )
        }

        coVerify(exactly = 2) {
            dao.insert(any())
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
}