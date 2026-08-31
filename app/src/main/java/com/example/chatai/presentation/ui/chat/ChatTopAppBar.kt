package com.example.chatai.presentation.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.theme.ChatThemeId
import com.example.chatai.presentation.ui.theme.ChatThemeBackIcon
import com.example.chatai.presentation.ui.theme.ChatThemeDefaultIcon
import com.example.chatai.presentation.ui.theme.ChatThemeDeleteIcon
import com.example.chatai.presentation.ui.theme.ChatThemeForestIcon
import com.example.chatai.presentation.ui.theme.ChatThemeMidnightIcon
import com.example.chatai.presentation.ui.theme.ChatThemePaletteIcon
import com.example.chatai.presentation.ui.theme.ChatThemeSearchIcon
import com.example.chatai.presentation.ui.theme.ChatThemeSunsetIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
    selectedMessages: Set<Message>,
    chatId: Int,
    searchQuery: String,
    navController: NavController,
    onSearchQueryChange: (String) -> Unit,
    onClearSelection: () -> Unit,
    onIntent: (ChatIntent) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showThemeSelection by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }

    fun ChatThemeId.icon(): ImageVector {
        return when (this) {
            ChatThemeId.DEFAULT -> ChatThemeDefaultIcon
            ChatThemeId.MIDNIGHT -> ChatThemeMidnightIcon
            ChatThemeId.FOREST -> ChatThemeForestIcon
            ChatThemeId.SUNSET -> ChatThemeSunsetIcon
        }
    }

    TopAppBar(
        title = {
            if (isSearching) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Поиск по истории",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            innerTextField()
                        }
                    )

                }
            } else {
                Text(
                    text = if (selectedMessages.isEmpty()) {
                        "Chat"
                    } else {
                        "Выбрано: ${selectedMessages.size}"
                    }
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    when {
                        isSearching -> {
                            isSearching = false
                            onSearchQueryChange("")
                        }

                        selectedMessages.isNotEmpty() -> {
                            onClearSelection()
                        }

                        else -> {
                            navController.popBackStack()
                        }
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
            if (isSearching) {
                if (searchQuery.isNotBlank()) {
                    IconButton(
                        onClick = {
                            onSearchQueryChange("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Очистить поиск"
                        )
                    }
                }
            } else if (selectedMessages.isNotEmpty()) {
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
                        },
                        shape = RoundedCornerShape(16.dp),
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp,
                        shadowElevation = 4.dp
                    ) {
                        if (showThemeSelection) {
                            DropdownMenuItem(
                                text = {
                                    Text("Тема")
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = ChatThemeBackIcon,
                                        contentDescription = null
                                    )
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
                                    leadingIcon = {
                                        Icon(
                                            imageVector = theme.icon(),
                                            contentDescription = null
                                        )
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
                                leadingIcon = {
                                    Icon(
                                        imageVector = ChatThemePaletteIcon,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    showThemeSelection = true
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text("Поиск по истории")
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = ChatThemeSearchIcon,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    isSearching = true
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("Очистить историю")
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = ChatThemeDeleteIcon,
                                        contentDescription = null
                                    )
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