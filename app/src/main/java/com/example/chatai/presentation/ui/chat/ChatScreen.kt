package com.example.chatai.presentation.ui.chat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.theme.ChatThemeId
import com.example.chatai.presentation.ui.components.Error
import com.example.chatai.presentation.ui.components.LoadingScreen
import com.example.chatai.presentation.ui.theme.ChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    chatId: Int,
) {
    val viewModel = hiltViewModel<ChatViewModel>()

    val chatState by viewModel.state.collectAsState()
    val settings by viewModel.settings.collectAsState()

    val selectedMessages = remember { mutableStateSetOf<Message>() }

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
                    navController = navController,
                    onClearSelection = {
                        selectedMessages.clear()
                    },
                    onIntent = viewModel::dispatch
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->

            when (val state = chatState) {
                ChatState.Loading -> {
                    LoadingScreen(
                        modifier = Modifier.padding(paddingValues)
                    )
                }

                is ChatState.Success -> {
                    MessagesScreen(
                        messages = state.messages,
                        paddingValues = paddingValues,
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

                is ChatState.Error -> {
                    Error(
                        message = state.message,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

