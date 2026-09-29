package com.example.chatai.domain.interactors

import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import com.example.chatai.domain.repository.MessageRepository
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class MessageInteractorTest {

    private lateinit var repo: MessageRepository
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
    fun `sendMessage sends user message to repository`() = runTest {
        val serverMessage = Message(
            id = 42L,
            chatId = 1,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123L
        )

        coEvery {
            repo.sendMessage(1, any())
        } returns serverMessage

        interactor.sendMessage(
            chatId = 1,
            text = "Hello"
        )

        coVerify(exactly = 1) {
            repo.sendMessage(
                1,
                match {
                    it.id == 0L &&
                            it.chatId == 1 &&
                            it.text == "Hello" &&
                            it.sender == Sender.USER
                }
            )
        }
    }

    @Test
    fun `deleteMessages deletes messages from repository`() = runTest {
        val chatId = 1
        val messageIds = listOf(10L, 20L, 30L)

        coEvery {
            repo.deleteMessagesList(chatId, messageIds)
        } just Runs

        interactor.deleteMessages(
            chatId = chatId,
            messageIds = messageIds
        )

        coVerify(exactly = 1) {
            repo.deleteMessagesList(
                chatId,
                messageIds
            )
        }
    }

    @Test
    fun `sendMessage sends user message with current timestamp`() = runTest {
        val before = System.currentTimeMillis()

        val response = Message(
            id = 42L,
            chatId = 1,
            text = "Hello",
            sender = Sender.USER,
            timestamp = 123L
        )

        coEvery {
            repo.sendMessage(1, any())
        } returns response

        interactor.sendMessage(1, "Hello")

        val after = System.currentTimeMillis()

        coVerify(exactly = 1) {
            repo.sendMessage(
                1,
                match {
                    it.id == 0L &&
                            it.chatId == 1 &&
                            it.text == "Hello" &&
                            it.sender == Sender.USER &&
                            it.timestamp in before..after
                }
            )
        }
    }
}