package com.example.chatai.presentation.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.chatai.R
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import com.example.chatai.presentation.ui.theme.ChatUiTokens
import com.example.chatai.presentation.ui.theme.Dimens

@Composable
fun MessageItemMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    containerWith: Dp,
    message: Message,
    onDelete: () -> Unit,
) {
    val targetOffset = containerWith * ChatUiTokens.Message.MENU_OFFSET_FACTOR
    var showDeleteConfirm by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        offset = DpOffset(
            x = when (message.sender) {
                Sender.USER -> -targetOffset
                Sender.ASSISTANT -> targetOffset
            },
            y = 0.dp
        ),
        modifier = Modifier.widthIn(min = Dimens.Message.menuMinWidth)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.delete)
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Delete,
                    stringResource(R.string.delete)
                )
            },
            onClick = {
                showDeleteConfirm = true
                onDismiss()
            }
        )
    }

    if (showDeleteConfirm) ConfirmDialog(
        onDismiss = { showDeleteConfirm = false },
        onConfirm = onDelete,
        title = stringResource(R.string.delete_message),
        text = stringResource(R.string.delete_message_confirmation)
    )
}