package com.example.chatai.presentation.ui.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
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
import androidx.compose.foundation.lazy.items
import formatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
    selectedMessages: Set<Message>,
    chatId: Int,
    searchQuery: String,
    searchResults: List<Int>,
    navController: NavController,
    onSearchQueryChange: (String) -> Unit,
    onClearSelection: () -> Unit,
    onIntent: (ChatIntent) -> Unit,
    onSearchResultClick: (Int) -> Unit,
    messages: List<Message>
) {
    var showMenu by remember { mutableStateOf(false) }
    var showThemeSelection by remember { mutableStateOf(false) }
    var isSearching by rememberSaveable { mutableStateOf(false) }
    var searchExpanded by rememberSaveable { mutableStateOf(false) }


    fun ChatThemeId.icon(): ImageVector {
        return when (this) {
            ChatThemeId.DEFAULT -> ChatThemeDefaultIcon
            ChatThemeId.MIDNIGHT -> ChatThemeMidnightIcon
            ChatThemeId.FOREST -> ChatThemeForestIcon
            ChatThemeId.SUNSET -> ChatThemeSunsetIcon
        }
    }

    if (isSearching) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onSearch = {
                        searchExpanded = false
                    },
                    expanded = searchExpanded,
                    onExpandedChange = {
                        searchExpanded = it
                    },
                    placeholder = {
                        Text(
                            text = "Поиск по истории",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                isSearching = false
                                searchExpanded = false
                                onSearchQueryChange("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Назад"
                            )
                        }
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = searchQuery.isNotEmpty()
                        ) {
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
                    }
                )
            },
            expanded = searchExpanded,
            onExpandedChange = {
                searchExpanded = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            if (searchQuery.isNotBlank()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        items = searchResults,
                        key = { it }
                    ) { index ->
                        val message = messages.getOrNull(index)
                        if (message != null) {
                            SearchResultItem(
                                message = message,
                                query = searchQuery,
                                onClick = {
                                    isSearching = false
                                    searchExpanded = false
                                    onSearchQueryChange("")
                                    onSearchResultClick(index)
                                }
                            )
                        }
                    }
                }
            }
        }

        return
    }

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
                    when {
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
                                    searchExpanded = true
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

@Composable
private fun SearchResultItem(
    message: Message,
    query: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = 20.dp,
                vertical = 14.dp
            )
    ) {
        Text(
            text = message.text,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = formatTime(message.timestamp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

