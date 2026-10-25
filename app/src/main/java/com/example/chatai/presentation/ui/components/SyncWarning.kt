package com.example.chatai.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.chatai.presentation.ui.theme.Dimens

@Composable
fun SyncWarning(
    message: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.Spacing.medium,
                vertical = Dimens.Spacing.small
            ),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = Dimens.Elevation.low
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = Dimens.Spacing.medium,
                vertical = Dimens.Spacing.itemSpacing
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = null
            )

            Spacer(Modifier.width(Dimens.Spacing.small))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
