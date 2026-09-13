package com.example.chatai.presentation.ui.login

import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.chatai.R
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
}