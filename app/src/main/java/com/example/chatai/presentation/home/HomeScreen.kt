@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatai.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatai.presentation.Screen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val viewModel: HomeViewModel = hiltViewModel()

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screen.SettingsScreen) }
                    ) {
                        Icon(Icons.Outlined.Settings, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Home(
                viewModel = viewModel,
                paddingValues,
                onChatClick = { chatId ->
                    navController.navigate(
                        Screen.ChatScreen(chatId)
                    )
                }, onLogout = {
                    viewModel.logout()
                })
        }
    }
}

@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    onChatClick: (Int) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        val chats by viewModel.chats.collectAsState()
        Column {
            Text("Мои чаты")
            LazyColumn {
                items(chats) { chat ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onChatClick(chat.id)
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = chat.title)
                            Text(text = chat.model)
                        }
                    }
                }
            }
        }

        Button(
            onClick = { onLogout() }
        ) {
            Text("Logout")
        }
    }
}