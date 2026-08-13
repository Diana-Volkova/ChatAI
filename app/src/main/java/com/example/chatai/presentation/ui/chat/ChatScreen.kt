package com.example.chatai.presentation.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatai.data.Message
import com.example.chatai.data.Sender
import com.example.chatai.presentation.ui.components.Error
import com.example.chatai.presentation.ui.components.LoadingScreen
import formatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    chatId: Int,
) {
    val viewModel = hiltViewModel<ChatViewModel>()
    val chatState by viewModel.state.collectAsState()

    LaunchedEffect(chatId) {
        viewModel.loadHistory(chatId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Chat")
                },
                navigationIcon = {
                    IconButton(
                        onClick = navController::popBackStack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
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

@Composable
fun MessagesScreen(
    messages: List<Message>,
    paddingValues: PaddingValues,
    onSendMessage: (String) -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = paddingValues.calculateStartPadding(layoutDirection),
                end = paddingValues.calculateEndPadding(layoutDirection),
            )
    ) {
        Messages(
            messages = messages,
            modifier = Modifier.weight(1f),
        )

        MessageInput(
            onSendMessage = onSendMessage,
        )
    }
}

@Composable
fun Messages(
    messages: List<Message>,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.scrollToItem(messages.lastIndex)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
    ) {
        items(messages) { message ->
            MessageItem(message)
        }
    }
}
@Composable
fun MessageInput(
    onSendMessage: (String) -> Unit,
) {
    val fieldState = remember {
        TextFieldState()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .imePadding()
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            state = fieldState,
            placeholder = {
                Text("Message...")
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
        )

        IconButton(
            onClick = {
                onSendMessage(fieldState.text.toString())
                fieldState.clearText()
            },
            enabled = fieldState.text.isNotBlank(),
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
            ),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun MessageItem(
    message: Message,
) {
    val density = LocalDensity.current

    var textLayout by remember {
        mutableStateOf<TextLayoutResult?>(null)
    }

    var containerSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    var timeSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    val isInline =
        textLayout != null &&
                timeSize != IntSize.Zero &&
                containerSize != IntSize.Zero &&
                textLayout!!.size.width < containerSize.width / 2

    val isFreeSpace =
        textLayout != null &&
                timeSize != IntSize.Zero &&
                containerSize != IntSize.Zero &&
                textLayout!!.lineCount > 1 &&
                run {
                    val layout = textLayout!!
                    val lastX = layout.getLineRight(layout.lineCount - 1)
                    val freeSpace = layout.size.width - lastX

                    freeSpace > timeSize.width
                }

    val arrangement: Arrangement.Vertical =
        if (isInline || isFreeSpace) {
            Arrangement.spacedBy(
                density.run {
                    -timeSize.height.toDp()
                }
            )
        } else {
            Arrangement.Top
        }

    val textPadding =
        if (isInline) {
            density.run {
                timeSize.width.toDp()
            } + 4.dp
        } else {
            0.dp
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                containerSize = it
            },
        horizontalArrangement = when (message.sender) {
            Sender.USER -> Arrangement.End
            Sender.ASSISTANT -> Arrangement.Start
        },
    ) {
        Surface(
            color = when (message.sender) {
                Sender.USER -> MaterialTheme.colorScheme.primary
                Sender.ASSISTANT -> MaterialTheme.colorScheme.surfaceVariant
            },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 4.dp,
            ),
        ) {
            Column(
                verticalArrangement = arrangement,
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 8.dp,
                ),
            ) {
                Text(
                    text = message.text,
                    onTextLayout = {
                        textLayout = it
                    },
                    modifier = Modifier.padding(
                        end = textPadding,
                    ),
                )

                Text(
                    text = formatTime(message.timestamp),
                    modifier = Modifier
                        .onSizeChanged {
                            timeSize = it
                        }
                        .align(Alignment.End)
                        .alpha(0.7f),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}