package com.example.chatai.presentation

import android.content.Context
import com.example.chatai.presentation.home.HomeScreen
import com.example.chatai.presentation.chat.ChatScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.chatai.presentation.register.RegisterScreen
import com.example.chatai.presentation.settings.SettingsScreen
import com.example.chatai.presentation.login.LogInScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object HomeScreen : Screen()

    @Serializable
    data class ChatScreen(
        val chatId: Int
    ) : Screen()

    @Serializable
    object LogInScreen : Screen()

    @Serializable
    object RegisterScreen : Screen()

    @Serializable
    object SettingsScreen : Screen()
}

@Composable
fun Navigation(
    navController: NavHostController
) {
    val context = LocalContext.current

    val preferences = remember {
        context.getSharedPreferences(
            "app_preferences",
            Context.MODE_PRIVATE
        )
    }

    val startDestination = remember {
        if (preferences.getString("access_token", null) != null) {
            Screen.HomeScreen
        } else {
            Screen.LogInScreen
        }
    }

    NavHost(navController, startDestination = startDestination) {
        composable<Screen.HomeScreen> {
            HomeScreen(navController = navController)
        }
        composable<Screen.ChatScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ChatScreen>()
            ChatScreen(
                navController = navController,
                chatId = args.chatId
            )
        }
        composable<Screen.SettingsScreen> {
            SettingsScreen(navController)
        }
        composable<Screen.LogInScreen> {
            LogInScreen(navController)
        }
        composable<Screen.RegisterScreen> {
            RegisterScreen(navController)
        }
    }
}