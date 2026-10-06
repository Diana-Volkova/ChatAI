package com.example.chatai

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.chatai.core.localization.LanguageManager
import com.example.chatai.core.localization.LocaleContextWrapper
import com.example.chatai.core.theme.ThemeManager
import com.example.chatai.presentation.navigation.Navigation
import com.example.chatai.presentation.ui.splash.SplashScreen
import com.example.chatai.presentation.ui.theme.ChatAITheme
import com.example.chatai.presentation.ui.theme.ThemeMode
import com.example.chatai.presentation.ui.theme.ThemeState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val languageManager = LanguageManager(newBase)
        val language = languageManager.getLanguage()

        super.attachBaseContext(
            LocaleContextWrapper.wrap(newBase, language)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )

        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
            insets
        }

        val themeManager = ThemeManager(this)

        ThemeState.mode = when (themeManager.getTheme()) {
            ThemeManager.LIGHT -> ThemeMode.LIGHT
            ThemeManager.DARK -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
        setContent {
            ChatAITheme {
                var showSplash by remember {
                    mutableStateOf(true)
                }

                if (showSplash) {
                    SplashScreen(
                        onFinished = {
                            showSplash = false
                        }
                    )
                } else {
                    val navController = rememberNavController()
                    Surface {
                        Navigation(navController = navController)
                    }
                }
            }
        }
    }
}