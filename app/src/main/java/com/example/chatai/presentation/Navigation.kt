package com.example.chatai.presentation

import com.example.chatai.presentation.home.HomeScreen
import com.example.chatai.presentation.chat.ChatScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatai.presentation.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object HomeScreen : Screen()

    @Serializable
    object ChatScreen : Screen()

    @Serializable
    object SettingsScreen : Screen()
}

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(navController, startDestination = Screen.HomeScreen) {
        composable<Screen.HomeScreen> {
            HomeScreen(navController = navController)
        }
        composable<Screen.ChatScreen> {
            ChatScreen(navController = navController)
        }
        composable<Screen.SettingsScreen> {
            SettingsScreen(navController)
        }
    }
}