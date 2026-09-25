package com.example.chatai.presentation.ui.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.chatai.R
import com.example.chatai.core.localization.LanguageManager
import com.example.chatai.presentation.navigation.Screen
import com.example.chatai.presentation.ui.theme.ThemeState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var selectedLanguage by remember {
        mutableStateOf(
            LanguageManager(context).getLanguage()
        )
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    val selectedTheme = ThemeState.mode

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val error by viewModel.error.collectAsStateWithLifecycle()

    // Обработка одноразовых эффектов
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AuthEffect.NavigateToLogin -> {
                    navController.navigate(Screen.LogInScreen) {
                        popUpTo(Screen.HomeScreen) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(error) {
        error?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            SettingsSection(
                title = stringResource(R.string.appearance)
            ) {
                ThemeSelector(
                    selectedMode = selectedTheme,
                    onModeSelected = { mode ->
                        ThemeState.mode = mode
                    }
                )
            }

            SettingsSection(
                title = stringResource(R.string.language)
            ) {
                LanguageSelector(
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = { language ->
                        selectedLanguage = language

                        LanguageManager(context)
                            .setLanguage(language)

                        activity?.recreate()
                    }
                )
            }

            SettingsSection(
                title = stringResource(R.string.account)
            ) {
                SettingsItem(
                    icon = Icons.AutoMirrored.Outlined.ExitToApp,
                    title = stringResource(R.string.logout),
                    onClick = {
                        viewModel.logout()
                    }
                )

                SettingsItem(
                    icon = Icons.Outlined.DeleteOutline,
                    title = stringResource(R.string.delete_account),
                    onClick = {
                        showDeleteDialog = true
                    },
                    tint = MaterialTheme.colorScheme.error
                )
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )
        }
    }

    if (showDeleteDialog) {
        DeleteAccountDialog(
            onDismiss = {
                showDeleteDialog = false
            },
            onConfirm = {
                showDeleteDialog = false
                viewModel.deleteAccount()
            }
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 8.dp
            )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            content()
        }
    }
}