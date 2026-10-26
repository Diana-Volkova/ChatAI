package com.example.chatai.presentation.ui.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chatai.R
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.theme.ChatThemeId
import com.example.chatai.presentation.ui.components.ExceptionContent
import com.example.chatai.presentation.ui.components.LoadingScreen
import com.example.chatai.presentation.ui.sync.SyncStatus
import com.example.chatai.presentation.ui.components.SyncWarning
import com.example.chatai.presentation.ui.theme.ChatTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel(),
    chatId: Int,
) {
    val chatState by viewModel.state.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    val chatDetails = (chatState as? ChatState.Success)?.data
    val messages = chatDetails?.messages.orEmpty()
    val chatName = chatDetails?.chat?.title.orEmpty()

    val selectedMessages = remember { mutableStateSetOf<Message>() }
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(chatId) {
        viewModel.dispatch(ChatIntent.LoadHistory(chatId))
        viewModel.dispatch(ChatIntent.ObserveSettings(chatId))
    }

    ChatTheme(theme = settings?.theme ?: ChatThemeId.DEFAULT) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                ChatTopAppBar(
                    selectedMessages = selectedMessages,
                    chatId = chatId,
                    searchQuery = searchQuery,
                    searchResults = searchResults,
                    navController = navController,
                    messages = messages,
                    chatName = chatName,
                    onClearSelection = { selectedMessages.clear() },
                    onSearchQueryChange = { viewModel.dispatch(ChatIntent.SearchMessages(it)) },
                    onIntent = viewModel::dispatch,
                    onSearchResultClick = { index ->
                        coroutineScope.launch { lazyListState.animateScrollToItem(index) }
                    }
                )
            }
        ) { paddingValues ->
            when (val state = chatState) {
                is ChatState.Loading -> {
                    LoadingScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .testTag("loading_screen")
                    )
                }

                is ChatState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        if (syncStatus is SyncStatus.OfflineCached) {
                            SyncWarning(message = stringResource(R.string.chat_offline_cached))
                        }

                        MessagesScreen(
                            modifier = Modifier.weight(1f),
                            messages = messages,
                            paddingValues = PaddingValues(),
                            listState = lazyListState,
                            searchQuery = searchQuery,
                            selectedMessages = selectedMessages,
                            onSendMessage = { text ->
                                viewModel.dispatch(ChatIntent.SendMessage(chatId, text))
                            },
                            onGenerateAnotherAnswer = { message ->
                                viewModel.dispatch(ChatIntent.GenerateAlternative(chatId, message))
                            },
                            onDeleteMessages = { messageIds ->
                                viewModel.dispatch(ChatIntent.DeleteMessages(chatId, messageIds))
                            }
                        )
                    }
                }

                is ChatState.Error -> {
                    ExceptionContent(
                        message = state.message,
                        modifier = Modifier
                            .padding(paddingValues)
                            .testTag("error_message")
                    )
                }
            }
        }
    }
}