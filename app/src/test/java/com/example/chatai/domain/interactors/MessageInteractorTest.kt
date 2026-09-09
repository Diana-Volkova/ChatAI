package com.example.chatai.domain.interactors

import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import com.example.chatai.domain.repository.ChatRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
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
    fun `sendMessage sends user message to repository`() = runTest {
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

        interactor.sendMessage(1, "Hello")

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

}