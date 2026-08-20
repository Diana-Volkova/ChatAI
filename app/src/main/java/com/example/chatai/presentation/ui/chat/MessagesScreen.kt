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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.example.chatai.domain.model.Message
import com.example.chatai.presentation.ui.components.MessageItem

@Composable
fun MessagesScreen(
    messages: List<Message>,
    paddingValues: PaddingValues,
    onSendMessage: (String) -> Unit,
    selectedMessages: SnapshotStateSet<Message>,
    onDeleteMessages: (List<Long>) -> Unit,
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
            selectedMessages = selectedMessages,
            onDeleteMessages = onDeleteMessages,
        )

        MessageInput(
            onSendMessage = onSendMessage,
        )
    }
}

@Composable
fun Messages(
    messages: List<Message>,
    selectedMessages: SnapshotStateSet<Message>,
    onDeleteMessages: (List<Long>) -> Unit,
    modifier: Modifier = Modifier
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
            MessageItem(
                message = message,
                selected = message in selectedMessages,
                selecting = selectedMessages.isNotEmpty(),
                onDelete = {
                    message.serverId?.let { serverId ->
                        onDeleteMessages(listOf(serverId))
                    }
                },

                onSelect = {
                    when (message in selectedMessages) {
                        true -> selectedMessages.remove(message)
                        false -> selectedMessages.add(message)
                    }
                },
            )
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