package com.example.chatai.presentation.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.chatai.R
import com.example.chatai.presentation.ui.theme.ThemeMode

@Composable
fun ThemeSelector(
    selectedMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ThemeMode.entries.forEachIndexed { index, mode ->

            ThemeOption(
                mode = mode,
                selected = mode == selectedMode,
                onClick = {
                    onModeSelected(mode)
                },
                showDivider = index < ThemeMode.entries.lastIndex
            )
        }
    }
}

@Composable
private fun ThemeOption(
    mode: ThemeMode,
    selected: Boolean,
    onClick: () -> Unit,
    showDivider: Boolean
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(
                    horizontal = 20.dp,
                    vertical = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = when (mode) {
                        ThemeMode.SYSTEM ->
                            stringResource(R.string.theme_system)

                        ThemeMode.LIGHT ->
                            stringResource(R.string.theme_light)

                        ThemeMode.DARK ->
                            stringResource(R.string.theme_dark)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = when (mode) {
                        ThemeMode.SYSTEM ->
                            stringResource(R.string.theme_system_description)

                        ThemeMode.LIGHT ->
                            stringResource(R.string.theme_light_description)

                        ThemeMode.DARK ->
                            stringResource(R.string.theme_dark_description)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.accessibility_selected),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}