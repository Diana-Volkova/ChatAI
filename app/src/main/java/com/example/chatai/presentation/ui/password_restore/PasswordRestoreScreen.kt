package com.example.chatai.presentation.ui.password_restore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatai.R
import com.example.chatai.presentation.ui.extensions.getLocalizedMessage
import com.example.chatai.presentation.ui.theme.Dimens

@Composable
fun PasswordRestoreScreen(
    navController: NavController,
    viewModel: PasswordRestoreViewModel = hiltViewModel()
) {

    var email by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    val passwordResetEmailSent = stringResource(
        R.string.password_reset_email_sent
    )

    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {

                PasswordRestoreEffect.EmailSent -> {
                    snackbarHostState.showSnackbar(
                        passwordResetEmailSent
                    )
                }

                is PasswordRestoreEffect.Error -> {
                    snackbarHostState.showSnackbar(
                        effect.exception.getLocalizedMessage(context)
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(Dimens.Spacing.xLarge),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(R.string.password_restore_title),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(Dimens.Spacing.medium))

            Text(
                text = stringResource(R.string.password_restore_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(Dimens.Spacing.xLarge))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = {
                    Text(stringResource(R.string.email))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )

            Spacer(modifier = Modifier.height(Dimens.Spacing.xLarge))

            Button(
                onClick = {
                    viewModel.dispatch(
                        PasswordRestoreIntent.SendResetLink(email)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotBlank()
            ) {
                Text(
                    text = stringResource(R.string.send)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Spacing.small))

            TextButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.back_to_login)
                )
            }
        }
    }
}