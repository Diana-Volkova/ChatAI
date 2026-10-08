package com.example.chatai.data.repository

import com.example.chatai.data.local.ChatDao
import com.example.chatai.data.mappers.toEntity
import com.example.chatai.data.remote.api.ChatApi
import com.example.chatai.data.remote.dto.ChatDto
import com.example.chatai.domain.error.ChatException
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
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertFailsWith

class ChatRepositoryImplTest {
    private lateinit var api: ChatApi
    private lateinit var dao: ChatDao
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
    fun `syncChats refreshes chats when response is successful`() = runTest {
        val chats = listOf(
            ChatDto(
                id = 1,
                title = "First chat",
                model = "ia model",
                lastMessageAt = 123L
            ),
            ChatDto(
                id = 2,
                title = "Second chat",
                model = "Yura Bulochkin",
                lastMessageAt = 124L
            ),
        )

        coEvery {
            api.getChats()
        } returns Response.success(chats)

        coEvery {
            dao.refreshChats(any())
        } just Runs

        repository.syncChats()

        coVerify(exactly = 1) {
            api.getChats()
        }

        coVerify(exactly = 1) {
            dao.refreshChats(
                chats.map { it.toEntity() }
            )
        }
    }
    @Test
    fun `syncChats throws ChatException when response is unsuccessful`() = runTest {
        val response = Response.error<List<ChatDto>>(
            500,
            "Server error".toResponseBody()
        )

        coEvery {
            api.getChats()
        } returns response

        assertFailsWith<ChatException> {
            repository.syncChats()
        }

        coVerify(exactly = 1) {
            api.getChats()
        }

        coVerify(exactly = 0) {
            dao.insertAll(any())
        }
    }

    @Test
    fun `syncChats throws ChatException when response body is null`() = runTest {
        val response = Response.success<List<ChatDto>>(null)

        coEvery {
            api.getChats()
        } returns response

        assertFailsWith<ChatException> {
            repository.syncChats()
        }

        coVerify(exactly = 1) {
            api.getChats()
        }

        coVerify(exactly = 0) {
            dao.insertAll(any())
        }
    }
}