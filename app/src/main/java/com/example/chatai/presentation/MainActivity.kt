package com.example.chatai.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.chatai.presentation.navigation.Navigation
import com.example.chatai.presentation.ui.theme.ChatAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatAITheme {
                val navController = rememberNavController()
                Surface {
                    Navigation(navController = navController)
                }
            }
        }
    }
}