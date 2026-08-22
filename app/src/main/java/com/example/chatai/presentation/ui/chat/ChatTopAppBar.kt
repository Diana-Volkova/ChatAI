package com.example.chatai.presentation.ui.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.theme.ChatThemeId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
    selectedMessages: Set<Message>,
    chatId: Int,
    navController: NavController,
    onClearSelection: () -> Unit,
    onIntent: (ChatIntent) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showThemeSelection by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = if (selectedMessages.isEmpty()) {
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
                        onClearSelection()
                    } else {
                        navController.popBackStack()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Назад"
                )
            }
        },
        actions = {
            if (selectedMessages.isNotEmpty()) {
                IconButton(
                    onClick = {
                        val messageIds = selectedMessages
                            .mapNotNull { it.serverId }

                        onIntent(
                            ChatIntent.DeleteMessages(
                                chatId = chatId,
                                messageIds = messageIds
                            )
                        )

                        onClearSelection()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Удалить сообщения"
                    )
                }
            } else {
                Box {
                    IconButton(
                        onClick = {
                            showMenu = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Дополнительные действия"
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = {
                            showMenu = false
                            showThemeSelection = false
                        }
                    ) {
                        if (showThemeSelection) {
                            DropdownMenuItem(
                                text = {
                                    Text("← Тема")
                                },
                                onClick = {
                                    showThemeSelection = false
                                }
                            )

                            ChatThemeId.entries.forEach { theme ->
                                DropdownMenuItem(
                                    text = {
                                        Text(theme.label)
                                    },
                                    onClick = {
                                        onIntent(
                                            ChatIntent.SetTheme(
                                                chatId = chatId,
                                                chatThemeId = theme
                                            )
                                        )

                                        showMenu = false
                                        showThemeSelection = false
                                    }
                                )
                            }
                        } else {
                            DropdownMenuItem(
                                text = {
                                    Text("Тема")
                                },
                                onClick = {
                                    showThemeSelection = true
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("Очистить историю")
                                },
                                onClick = {
                                    showMenu = false

                                    onIntent(
                                        ChatIntent.ClearHistory(
                                            chatId = chatId
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}