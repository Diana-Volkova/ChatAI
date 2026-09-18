package com.example.chatai.presentation.ui.chat

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.chatai.domain.model.ChatSettings
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ChatScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mockk<NavController>(relaxed = true)
    private val viewModel = mockk<ChatViewModel>(relaxed = true)

    private val state = MutableStateFlow<ChatState>(
        ChatState.Success(emptyList())
    )
    private val settings = MutableStateFlow<ChatSettings?>(null)
    private val searchQuery = MutableStateFlow("")
    private val searchResults = MutableStateFlow<List<Int>>(emptyList())

    @Before
    fun setup() {
        every { viewModel.state } returns state
        every { viewModel.settings } returns settings
        every { viewModel.searchQuery } returns searchQuery
        every { viewModel.searchResults } returns searchResults

        composeTestRule.setContent {
            ChatScreen(
                navController = navController,
                viewModel = viewModel,
                chatId = 1
            )
        }
    }

    @Test
    fun loadingState_displaysLoadingScreen() {
        state.value = ChatState.Loading

        composeTestRule
            .onNodeWithTag("loading_screen")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_displaysErrorMessage() {
        state.value = ChatState.Error(
            message = "Something went wrong"
        )

        composeTestRule
            .onNodeWithTag("error_message")
            .assertIsDisplayed()
    }

    @Test
    fun screen_loadsHistoryAndObservesSettings() {
        composeTestRule.runOnIdle {
            verify(exactly = 1) {
                viewModel.dispatch(ChatIntent.LoadHistory(1))
            }

            verify(exactly = 1) {
                viewModel.dispatch(ChatIntent.ObserveSettings(1))
            }
        }
    }
}