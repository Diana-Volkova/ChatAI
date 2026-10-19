
package com.example.chatai.presentation.navigation

import android.content.Context
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.chatai.presentation.ui.chat.ChatScreen
import com.example.chatai.presentation.ui.home.HomeScreen
import com.example.chatai.presentation.ui.login.LogInScreen
import com.example.chatai.presentation.ui.password_restore.PasswordRestoreScreen
import com.example.chatai.presentation.ui.register.RegisterScreen
import com.example.chatai.presentation.ui.settings.SettingsScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun Navigation(navController: NavHostController) {
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

    val backStackEntry by navController.currentBackStackEntryAsState()
    val isChat = backStackEntry?.destination?.hasRoute<Screen.ChatScreen>() == true

    var isReturning by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(isChat) {
        if (isChat && !isReturning) {
            dragOffset = 0f
        }
    }

    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    val scope = rememberCoroutineScope()
    val showHomeUnderlay = isChat || isReturning ||
            navController.currentBackStackEntry
                ?.destination
                ?.hasRoute<Screen.HomeScreen>() == true

    val homeEntry = remember(navController, showHomeUnderlay) {
        if (showHomeUnderlay) {
            runCatching {
                navController.getBackStackEntry<Screen.HomeScreen>()
            }.getOrNull()
        } else {
            null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { containerSize = it }
            .pointerInput(isChat, containerSize) {
                if (!isChat) return@pointerInput

                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset = (dragOffset + dragAmount).coerceIn(
                            0f,
                            containerSize.width.toFloat()
                        )
                    },
                    onDragEnd = {
                        scope.launch {
                            val width = containerSize.width.toFloat()

                            if (
                                width > 0f &&
                                dragOffset > width * 0.30f
                            ) {
                                animate(
                                    initialValue = dragOffset,
                                    targetValue = width,
                                    animationSpec = tween(180)
                                ) { value, _ ->
                                    dragOffset = value
                                }

                                isReturning = true
                                val popped = navController.popBackStack()

                                if (popped) {
                                    navController.currentBackStackEntryFlow.first {
                                        it.destination.hasRoute<Screen.HomeScreen>()
                                    }

                                    withFrameNanos { }
                                    isReturning = false
                                } else {
                                    animate(
                                        initialValue = dragOffset,
                                        targetValue = 0f,
                                        animationSpec = tween(220)
                                    ) { value, _ ->
                                        dragOffset = value
                                    }

                                    isReturning = false
                                }
                            } else {
                                animate(
                                    initialValue = dragOffset,
                                    targetValue = 0f,
                                    animationSpec = tween(220)
                                ) { value, _ ->
                                    dragOffset = value
                                }
                            }
                        }
                    },
                    onDragCancel = {
                        scope.launch {
                            animate(
                                initialValue = dragOffset,
                                targetValue = 0f,
                                animationSpec = tween(220)
                            ) { value, _ ->
                                dragOffset = value
                            }
                        }
                    }
                )
            }
    ) {
        if (homeEntry != null) {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides homeEntry
            ) {
                HomeScreen(navController = navController)
            }
        }

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = dragOffset
                }
                .testTag("navigation"),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable<Screen.HomeScreen> {
                HomeScreen(navController = navController)
            }

            composable<Screen.ChatScreen> { entry ->
                val args = entry.toRoute<Screen.ChatScreen>()

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

            composable<Screen.PasswordRestoreScreen> {
                PasswordRestoreScreen(navController)
            }
        }
    }
}
