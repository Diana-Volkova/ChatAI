package com.example.chatai.presentation.ui.chat

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.example.chatai.presentation.ui.components.Error
import com.example.chatai.presentation.ui.components.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    chatId: Int,
) {
    val viewModel = hiltViewModel<ChatViewModel>()
    val chatState by viewModel.state.collectAsState()

    val selectedMessages = remember { mutableStateSetOf<Message>() }

    LaunchedEffect(chatId) {
        viewModel.loadHistory(chatId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (selectedMessages.isEmpty()) {
                            "Chat"
                        } else {
                            "Выбрано: ${selectedMessages.size}"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (selectedMessages.isNotEmpty()) {
                                selectedMessages.clear()
                            } else {
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    if (selectedMessages.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                val messageIds = selectedMessages
                                    .mapNotNull { it.serverId }

                                viewModel.deleteMessages(
                                    chatId = chatId,
                                    messageIds = messageIds
                                )

                                selectedMessages.clear()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete messages"
                            )
                        }
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        when (val state = chatState) {
            is ChatState.Loading -> {
                LoadingScreen(modifier = Modifier.padding(paddingValues))
            }

            is ChatState.Success -> {
                MessagesScreen(
                    messages = state.messages,
                    paddingValues = paddingValues,
                    onSendMessage = { text ->
                        viewModel.dispatch(
                            ChatIntent.SendMessage(chatId, text)
                        )
                    },
                    selectedMessages = selectedMessages,
                    onDeleteMessages = { messageIds ->
                        Log.d(
                            "CHAT_DELETE",
                            "selected=${selectedMessages.size}, serverIds=$messageIds"
                        )
                        viewModel.deleteMessages(
                            chatId = chatId,
                            messageIds = messageIds
                        )
                    }
                )
            }

            is ChatState.Error -> {
                Error(
                    message = state.message,
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }
}

