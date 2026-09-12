package com.example.chatai.presentation.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.chatai.core.localization.LanguageManager

@Composable
fun LanguageSelector(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LanguageOption(
            language = LanguageManager.EN,
            title = stringResource(
                R.string.language_english
            ),
            selected = selectedLanguage == LanguageManager.EN,
            onClick = {
                onLanguageSelected(LanguageManager.EN)
            },
            showDivider = true
        )

        LanguageOption(
            language = LanguageManager.RU,
            title = stringResource(
                R.string.language_russian
            ),
            selected = selectedLanguage == LanguageManager.RU,
            onClick = {
                onLanguageSelected(LanguageManager.RU)
            },
            showDivider = false
        )
    }
}

@Composable
private fun LanguageOption(
    language: String,
    title: String,
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
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(
                        R.string.accessibility_selected
                    ),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(
                    horizontal = 20.dp
                ),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}