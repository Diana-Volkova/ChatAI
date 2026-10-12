package com.example.chatai.presentation.ui.chat

import com.example.chatai.MainDispatcherRule
import com.example.chatai.domain.interactors.HistoryInteractor
import com.example.chatai.domain.interactors.MessageInteractor
import com.example.chatai.domain.model.Chat
import com.example.chatai.domain.model.ChatDetails
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import com.example.chatai.domain.repository.ChatSettingsRepository
import com.example.chatai.presentation.ui.sync.SyncStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.Runs
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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

    private val chatId = 1

    private fun createMessage(
        id: Long,
        text: String,
        sender: Sender = Sender.USER
    ) = Message(
        id = id,
        serverId = id,
        chatId = chatId,
        text = text,
        sender = sender,
        timestamp = id
    )

    @Before
    fun before() {
        viewModel = ChatViewModel(
            historyInteractor = historyInteractor,
            messageInteractor = messageInteractor,
            chatSettingsRepository = chatSettingsRepository
        )
    }

    @Test
    fun `loadHistory emits Success when history is loaded`() = runTest {
        val messages = listOf(
            createMessage(1L, "Hello"),
            createMessage(2L, "Hi!", Sender.ASSISTANT)
        )

        val chat = Chat(
            id = chatId,
            title = "Test chat",
            model = "test-model",
            lastMessageAt = 2L
        )

        val chatDetails = ChatDetails(
            chat = chat,
            messages = messages
        )

        coEvery {
            historyInteractor.syncHistory(chatId)
        } just Runs

        every {
            historyInteractor.observeHistory(chatId)
        } returns flowOf(chatDetails)

        viewModel.dispatch(ChatIntent.LoadHistory(chatId))
        advanceUntilIdle()

        assertEquals(ChatState.Success(chatDetails), viewModel.state.value)
        assertEquals(SyncStatus.Synced, viewModel.syncStatus.value)

        coVerify(exactly = 1) {
            historyInteractor.syncHistory(chatId)
        }
    }

    @Test
    fun `loadHistory emits Error when observing history fails`() = runTest {
        val errorMessage = "Failed to load history"

        every {
            historyInteractor.observeHistory(chatId)
        } returns flow {
            throw RuntimeException(errorMessage)
        }

        viewModel.dispatch(ChatIntent.LoadHistory(chatId))
        advanceUntilIdle()

        assertEquals(
            ChatState.Error(errorMessage),
            viewModel.state.value
        )
    }

    @Test
    fun `loadHistory uses cached history when sync fails`() = runTest {
        val messages = listOf(createMessage(1L, "Cached message"))

        val chat = Chat(
            id = chatId,
            title = "Test chat",
            model = "test-model",
            lastMessageAt = 1L
        )

        val chatDetails = ChatDetails(
            chat = chat,
            messages = messages
        )


        coEvery {
            historyInteractor.syncHistory(chatId)
        } throws RuntimeException("Network error")

        every {
            historyInteractor.observeHistory(chatId)
        } returns flowOf(chatDetails)

        viewModel.dispatch(ChatIntent.LoadHistory(chatId))
        advanceUntilIdle()

        assertEquals(ChatState.Success(chatDetails), viewModel.state.value)
        assertEquals(SyncStatus.OfflineCached, viewModel.syncStatus.value)
    }

    @Test
    fun `sendMessage calls messageInteractor`() = runTest {
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
    fun `generateAlternative calls messageInteractor`() = runTest {
        val message = createMessage(
            1L,
            "Original response",
            Sender.ASSISTANT
        )

        coEvery {
            messageInteractor.generateAlternative(chatId, message)
        } returns message

        viewModel.dispatch(
            ChatIntent.GenerateAlternative(chatId, message)
        )

        advanceUntilIdle()

        coVerify(exactly = 1) {
            messageInteractor.generateAlternative(chatId, message)
        }
    }

    @Test
    fun `generateAlternative emits Error when exception occurs`() = runTest {
        val message = createMessage(1L, "Original response", Sender.ASSISTANT)
        val errorMessage = "Failed to generate alternative"

        coEvery {
            messageInteractor.generateAlternative(chatId, message)
        } throws RuntimeException(errorMessage)

        viewModel.dispatch(
            ChatIntent.GenerateAlternative(chatId, message)
        )
        advanceUntilIdle()

        assertEquals(
            ChatState.Error(errorMessage),
            viewModel.state.value
        )
    }

    @Test
    fun `deleteMessages calls messageInteractor`() = runTest {
        val messageIds = listOf(1L, 2L)

        coEvery {
            messageInteractor.deleteMessages(chatId, messageIds)
        } just Runs

        viewModel.dispatch(
            ChatIntent.DeleteMessages(chatId, messageIds)
        )
        advanceUntilIdle()

        coVerify(exactly = 1) {
            messageInteractor.deleteMessages(chatId, messageIds)
        }
    }

    @Test
    fun `deleteMessages emits Error when exception occurs`() = runTest {
        val messageIds = listOf(1L, 2L)

        coEvery {
            messageInteractor.deleteMessages(chatId, messageIds)
        } throws RuntimeException("Delete failed")

        viewModel.dispatch(
            ChatIntent.DeleteMessages(chatId, messageIds)
        )
        advanceUntilIdle()

        assertEquals(
            ChatState.Error("Delete failed"),
            viewModel.state.value
        )
    }

    @Test
    fun `clearHistory clears messages after history is loaded`() = runTest {
        val messages = listOf(createMessage(1L, "Hello"))

        val chat = Chat(
            id = chatId,
            title = "Test chat",
            model = "test-model",
            lastMessageAt = 1L
        )

        val chatDetails = ChatDetails(
            chat = chat,
            messages = messages
        )

        coEvery {
            historyInteractor.syncHistory(chatId)
        } just Runs

        every {
            historyInteractor.observeHistory(chatId)
        } returns flowOf(chatDetails)

        coEvery {
            historyInteractor.clearHistory(chatId)
        } just Runs

        viewModel.dispatch(ChatIntent.LoadHistory(chatId))
        advanceUntilIdle()

        viewModel.dispatch(ChatIntent.ClearHistory(chatId))
        advanceUntilIdle()

        assertEquals(
            ChatState.Success(chatDetails.copy(messages = emptyList())),
            viewModel.state.value
        )

        coVerify(exactly = 1) {
            historyInteractor.clearHistory(chatId)
        }
    }


    @Test
    fun `clearHistory emits Error when exception occurs`() = runTest {
        coEvery {
            historyInteractor.clearHistory(chatId)
        } throws RuntimeException("Clear failed")

        viewModel.dispatch(ChatIntent.ClearHistory(chatId))
        advanceUntilIdle()

        assertEquals(
            ChatState.Error("Clear failed"),
            viewModel.state.value
        )
    }

    @Test
    fun `searchMessages finds matching message indices`() = runTest {
        val messages = listOf(
            createMessage(1L, "Kotlin is awesome"),
            createMessage(2L, "Java is fine", Sender.ASSISTANT),
            createMessage(3L, "Learning Kotlin coroutines")
        )

        val chat = Chat(
            id = chatId,
            title = "Test chat",
            model = "test-model",
            lastMessageAt = 3L
        )

        val chatDetails = ChatDetails(
            chat = chat,
            messages = messages
        )

        coEvery {
            historyInteractor.syncHistory(chatId)
        } just Runs

        every {
            historyInteractor.observeHistory(chatId)
        } returns flowOf(chatDetails)

        viewModel.dispatch(ChatIntent.LoadHistory(chatId))
        advanceUntilIdle()

        viewModel.dispatch(ChatIntent.SearchMessages("kotlin"))

        assertEquals("kotlin", viewModel.searchQuery.value)
        assertEquals(listOf(0, 2), viewModel.searchResults.value)
    }

    @Test
    fun `searchMessages ignores letter case`() = runTest {
        val messages = listOf(
            createMessage(1L, "KOTLIN"),
            createMessage(2L, "Java")
        )

        val chat = Chat(
            id = chatId,
            title = "Test chat",
            model = "test-model",
            lastMessageAt = 2L
        )

        val chatDetails = ChatDetails(
            chat = chat,
            messages = messages
        )

        coEvery {
            historyInteractor.syncHistory(chatId)
        } just Runs

        every {
            historyInteractor.observeHistory(chatId)
        } returns flowOf(chatDetails)

        viewModel.dispatch(ChatIntent.LoadHistory(chatId))
        advanceUntilIdle()

        viewModel.dispatch(ChatIntent.SearchMessages("kotlin"))

        assertEquals(listOf(0), viewModel.searchResults.value)
    }

    @Test
    fun `searchMessages clears results for blank query`() = runTest {
        val messages = listOf(
            createMessage(1L, "Kotlin"),
            createMessage(2L, "Java")
        )

        val chat = Chat(
            id = chatId,
            title = "Test chat",
            model = "test-model",
            lastMessageAt = 2L
        )

        val chatDetails = ChatDetails(
            chat = chat,
            messages = messages
        )

        coEvery {
            historyInteractor.syncHistory(chatId)
        } just Runs

        every {
            historyInteractor.observeHistory(chatId)
        } returns flowOf(chatDetails)

        viewModel.dispatch(ChatIntent.LoadHistory(chatId))
        advanceUntilIdle()

        viewModel.dispatch(ChatIntent.SearchMessages("Kotlin"))
        viewModel.dispatch(ChatIntent.SearchMessages("   "))

        assertEquals("   ", viewModel.searchQuery.value)
        assertEquals(emptyList(), viewModel.searchResults.value)
    }

    @Test
    fun `searchMessages returns empty results when state is not Success`() {
        viewModel.dispatch(ChatIntent.SearchMessages("Kotlin"))

        assertEquals("Kotlin", viewModel.searchQuery.value)
        assertEquals(emptyList(), viewModel.searchResults.value)
    }
}
