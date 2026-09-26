package com.example.chatai.presentation.ui.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatai.R
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.theme.ChatThemeId
import com.example.chatai.presentation.ui.components.Error
import com.example.chatai.presentation.ui.components.LoadingScreen
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
    val settings by viewModel.settings.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    val messages = (chatState as? ChatState.Success)?.messages.orEmpty()

    val selectedMessages = remember { mutableStateSetOf<Message>() }

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(chatId) {
        viewModel.dispatch(
            ChatIntent.LoadHistory(chatId)
        )

        viewModel.dispatch(
            ChatIntent.ObserveSettings(chatId)
        )
    }

    ChatTheme(
        theme = settings?.theme ?: ChatThemeId.DEFAULT
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                ChatTopAppBar(
                    selectedMessages = selectedMessages,
                    chatId = chatId,
                    searchQuery = searchQuery,
                    searchResults = searchResults,
                    navController = navController,
                    onClearSelection = {
                        selectedMessages.clear()
                    },
                    onSearchQueryChange = { query ->
                        viewModel.dispatch(
                            ChatIntent.SearchMessages(query)
                        )
                    },
                    onIntent = viewModel::dispatch,
                    onSearchResultClick = { index ->
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(index)
                        }
                    },
                    messages = messages
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->

            when (val state = chatState) {
                ChatState.Loading -> {
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
                        state.warning?.let { _ ->
                            SyncWarning(message = stringResource(R.string.chat_offline_cached))
                        }

                        MessagesScreen(
                            messages = state.messages,
                            paddingValues = PaddingValues(),
                            listState = lazyListState,
                            searchQuery = searchQuery,
                            onSendMessage = { text ->
                                viewModel.dispatch(
                                    ChatIntent.SendMessage(
                                        chatId = chatId,
                                        text = text
                                    )
                                )
                            },
                            selectedMessages = selectedMessages,
                            onDeleteMessages = { messageIds ->
                                viewModel.dispatch(
                                    ChatIntent.DeleteMessages(
                                        chatId = chatId,
                                        messageIds = messageIds
                                    )
                                )
                            }
                        )
                    }
                }

                is ChatState.Error -> {
                    Error(
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


@Composable
private fun SyncWarning(
    message: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 10.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = null
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

