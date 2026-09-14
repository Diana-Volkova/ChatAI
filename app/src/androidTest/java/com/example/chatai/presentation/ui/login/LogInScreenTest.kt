package com.example.chatai.presentation.ui.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.example.chatai.R
import com.example.chatai.presentation.navigation.Screen
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class LogInScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mockk<NavController>(relaxed = true)
    private val viewModel = mockk<LogInViewModel>(relaxed = true)

    private val effects = MutableSharedFlow<LogInEffect>()

    @Test
    fun login_button_sends_entered_credentials_to_viewModel() {
        every { viewModel.effects } returns effects.asSharedFlow()

        composeTestRule.setContent {
            LogInScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        val passwordLabel = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getString(R.string.password)

        val emailLabel = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getString(R.string.email)

        val logInLabel = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getString(R.string.login)

        composeTestRule
            .onNode(
                hasSetTextAction() and hasText(emailLabel)
            )
            .performTextInput("test@test.com")

        composeTestRule
            .onNode(
                hasSetTextAction() and hasText(passwordLabel)
            )
            .performTextInput("12345678")

        composeTestRule
            .onNodeWithText(logInLabel)
            .performClick()

        verify {
            viewModel.dispatch(
                LogInIntent.LogIn(
                    email = "test@test.com",
                    password = "12345678"
                )
            )
        }
    }

    @Test
    fun screen_displays_login_elements() {
        every { viewModel.effects } returns effects.asSharedFlow()

        composeTestRule.setContent {
            LogInScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val titleLabel = context.getString(R.string.login_title)
        val emailLabel = context.getString(R.string.email)
        val passwordLabel = context.getString(R.string.password)
        val logInLabel = context.getString(R.string.login)
        val createAccountLabel = context.getString(R.string.create_account)
        val forgotPasswordLabel = context.getString(R.string.forgot_password)

        composeTestRule
            .onNodeWithText(titleLabel)
            .assertIsDisplayed()

        composeTestRule
            .onNode(hasSetTextAction() and hasText(emailLabel))
            .assertIsDisplayed()

        composeTestRule
            .onNode(hasSetTextAction() and hasText(passwordLabel))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(logInLabel)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(createAccountLabel)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(forgotPasswordLabel)
            .assertIsDisplayed()
    }
    @Test
    fun register_button_navigates_to_register_screen() {
        every { viewModel.effects } returns effects.asSharedFlow()

        composeTestRule.setContent {
            LogInScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val createAccountLabel = context.getString(R.string.create_account)

        composeTestRule
            .onNodeWithText(createAccountLabel)
            .performClick()

        verify {
            navController.navigate(Screen.RegisterScreen)
        }
    }

    @Test
    fun successful_login_navigates_to_home() = runTest {
        every { viewModel.effects } returns effects.asSharedFlow()

        composeTestRule.setContent {
            LogInScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        effects.emit(LogInEffect.NavigateToHome)
        composeTestRule.awaitIdle()

        verify {
            navController.navigate(
                eq(Screen.HomeScreen),
                any<NavOptionsBuilder.() -> Unit>()
            )
        }
    }

    @Test
    fun login_error_displays_snackbar() = runTest {
        every { viewModel.effects } returns effects.asSharedFlow()

        composeTestRule.setContent {
            LogInScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        val errorMessage = "Неверный логин или пароль"

        effects.emit(LogInEffect.Error(errorMessage))
        composeTestRule.awaitIdle()

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }
}