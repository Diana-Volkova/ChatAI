package com.example.chatai.presentation.ui.settings

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.chatai.R
import com.example.chatai.core.localization.LanguageManager

@Composable
fun LanguageSelector() {

    val context = LocalContext.current
    val activity = context as? Activity

    var currentLanguage by remember {
        mutableStateOf(
            LanguageManager(context).getLanguage()
        )
    }

    Column {
        Text(
            text = stringResource(R.string.language)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    currentLanguage = LanguageManager.EN

                    LanguageManager(context)
                        .setLanguage(LanguageManager.EN)

                    activity?.recreate()
                }
                .padding(16.dp)
        ) {
            Text("English")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    currentLanguage = LanguageManager.RU

                    LanguageManager(context)
                        .setLanguage(LanguageManager.RU)

                    activity?.recreate()
                }
                .padding(16.dp)
        ) {
            Text("Русский")
        }
    }
}