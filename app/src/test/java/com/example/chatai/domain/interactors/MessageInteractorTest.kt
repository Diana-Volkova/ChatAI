package com.example.chatai.domain.interactors

import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import com.example.chatai.domain.repository.ChatRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MessageInteractorTest {
    private lateinit var repo: ChatRepository
    private lateinit var interactor: MessageInteractor

    @Before
    fun setup() {
        repo = mockk()
        interactor = MessageInteractor(repo)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `sendMessage emits user message and sent message`() = runTest {
        val serverMessage = Message(
            id = 42,
            chatId = 1,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123L
        )

        coEvery {
            repo.sendMessage(1, any())
        } returns serverMessage

        val result = interactor
            .sendMessage(1, "Hello")
            .toList()

        assertEquals(2, result.size)

        assertEquals("Hello", result[0].text)
        assertEquals(Sender.USER, result[0].sender)
        assertEquals(1, result[0].chatId)

        assertEquals(serverMessage, result[1])

        coVerify(exactly = 1) {
            repo.sendMessage(
                1,
                match {
                    it.text == "Hello" &&
                            it.sender == Sender.USER &&
                            it.chatId == 1
                }
            )
        }
    }

}