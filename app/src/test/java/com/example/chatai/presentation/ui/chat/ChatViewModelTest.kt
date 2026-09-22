package com.example.chatai.presentation.ui.chat

import com.example.chatai.MainDispatcherRule
import com.example.chatai.domain.interactors.HistoryInteractor
import com.example.chatai.domain.interactors.MessageInteractor
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import com.example.chatai.domain.repository.ChatSettingsRepository
import io.mockk.Runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val historyInteractor = mockk<HistoryInteractor>()
    private val messageInteractor = mockk<MessageInteractor>()
    private val chatSettingsRepository = mockk<ChatSettingsRepository>()

    private lateinit var viewModel: ChatViewModel

    @Before
    fun before() {
        viewModel = ChatViewModel(
            historyInteractor = historyInteractor,
            messageInteractor = messageInteractor,
            chatSettingsRepository = chatSettingsRepository
        )
    }

    @After
    fun after() {
        unmockkAll()
    }

    @Test
    fun `loadHistory emits Success when history is loaded`() = runTest {
        val chatId = 1

        val messages = listOf(
            Message(
                id = 1L,
                chatId = chatId,
                text = "Hello",
                sender = Sender.USER,
                timestamp = 1L
            ),
            Message(
                id = 2L,
                chatId = chatId,
                text = "Hi! How can I help you?",
                sender = Sender.ASSISTANT,
                timestamp = 2L
            )
        )

        coEvery {
            historyInteractor.syncHistory(chatId)
        } just Runs

        every {
            historyInteractor.observeHistory(chatId)
        } returns flowOf(messages)

        viewModel.dispatch(ChatIntent.LoadHistory(chatId))

        advanceUntilIdle()

        assertEquals(
            ChatState.Success(messages),
            viewModel.state.value
        )

        coVerify(exactly = 1) {
            historyInteractor.syncHistory(chatId)
        }
    }

    @Test
    fun `loadHistory emits Error when history loading fails`() = runTest {
        val chatId = 1
        val exception = RuntimeException("Failed to load history")

        coEvery {
            historyInteractor.syncHistory(chatId)
        } just Runs

        every {
            historyInteractor.observeHistory(chatId)
        } returns flow {
            throw exception
        }

        viewModel.dispatch(ChatIntent.LoadHistory(chatId))

        advanceUntilIdle()

        assertEquals(
            ChatState.Error("Failed to load history"),
            viewModel.state.value
        )
    }



    @Test
    fun `sendMessage calls messageInteractor`() = runTest {
        val chatId = 1
        val text = "Test message"

        coEvery {
            messageInteractor.sendMessage(chatId, text)
        } just Runs

        viewModel.dispatch(ChatIntent.SendMessage(chatId, text))
        advanceUntilIdle()

        coVerify(exactly = 1) {
            messageInteractor.sendMessage(chatId, text)
        }
    }

    @Test
    fun `sendMessage emits Error when exception occurs`() = runTest {
        val chatId = 1
        val text = "Test message"
        val errorMessage = "Network error"

        coEvery {
            messageInteractor.sendMessage(chatId, text)
        } throws RuntimeException(errorMessage)

        viewModel.dispatch(ChatIntent.SendMessage(chatId, text))
        advanceUntilIdle()

        assertEquals(
            ChatState.Error(errorMessage),
            viewModel.state.value
        )
    }

    @Test
    fun `clearHistory clears messages and emits Success with empty list`() = runTest {
        val chatId = 1

        coEvery {
            historyInteractor.clearHistory(chatId)
        } just Runs

        viewModel.dispatch(ChatIntent.ClearHistory(chatId))
        advanceUntilIdle()

        assertEquals(
            ChatState.Success(emptyList()),
            viewModel.state.value
        )

        coVerify(exactly = 1) {
            historyInteractor.clearHistory(chatId)
        }
    }

    @Test
    fun `searchMessages finds matching message indices correctly`() = runTest {
        val chatId = 1
        val messages = listOf(
            Message(id = 1L, chatId = chatId, text = "Kotlin is awesome", sender = Sender.USER, timestamp = 1L),
            Message(id = 2L, chatId = chatId, text = "Java is fine too", sender = Sender.ASSISTANT, timestamp = 2L),
            Message(id = 3L, chatId = chatId, text = "Learning Kotlin coroutines", sender = Sender.USER, timestamp = 3L)
        )

        coEvery { historyInteractor.syncHistory(chatId) } just Runs
        every { historyInteractor.observeHistory(chatId) } returns flowOf(messages)

        viewModel.dispatch(ChatIntent.LoadHistory(chatId))
        advanceUntilIdle()

        viewModel.dispatch(ChatIntent.SearchMessages("kotlin"))
        advanceUntilIdle()

        assertEquals("kotlin", viewModel.searchQuery.value)
        assertEquals(listOf(0, 2), viewModel.searchResults.value)
    }
}