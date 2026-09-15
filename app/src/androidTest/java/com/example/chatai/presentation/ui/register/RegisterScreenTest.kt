package com.example.chatai.presentation.ui.register

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.chatai.R
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val navController = mockk<NavController>(relaxed = true)
    private val effects = MutableSharedFlow<RegisterEffect>()
    private val viewModel = mockk<RegisterViewModel>(relaxed = true)

    @Before
    fun setup() {
        composeTestRule.setContent {
            every { viewModel.effects } returns effects

            RegisterScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    @Test
    fun registerButton_dispatchesRegisterIntent() {
        val emailLabel = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getString(R.string.email)

        val passwordLabel = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getString(R.string.password)

        composeTestRule
            .onNodeWithText(emailLabel)
            .performTextInput("test@example.com")

        composeTestRule
            .onNodeWithText(passwordLabel)
            .performTextInput("12345678")

        composeTestRule
            .onNodeWithText("Создать аккаунт")
            .performClick()

        verify {
            viewModel.dispatch(
                RegisterIntent.Register(
                    email = "test@example.com",
                    password = "12345678"
                )
            )
        }
    }

    @Test
    fun registerScreen_displaysAllElements() {
        val emailLabel = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getString(R.string.email)

        val passwordLabel = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getString(R.string.password)

        composeTestRule .onNodeWithText("Регистрация")
            .assertIsDisplayed()

        composeTestRule .onNodeWithText("Имя")
            .assertIsDisplayed()

        composeTestRule .onNodeWithText(emailLabel)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(passwordLabel)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Повторите пароль")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Создать аккаунт")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Уже есть аккаунт? Войти")
            .assertIsDisplayed()
    }

    @Test
    fun nameField_acceptsInput() {
        composeTestRule.onNodeWithText("Имя")
            .performTextInput("Alex")

        composeTestRule.onNodeWithText("Alex")
            .assertIsDisplayed()
    }

    @Test
    fun passwordField_hidesText() {
        val passwordLabel = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getString(R.string.password)

        composeTestRule
            .onNodeWithText(passwordLabel)
            .performTextInput("12345678")

        composeTestRule
            .onNodeWithText("••••••••")
            .assertIsDisplayed()
    }

    @Test
    fun confirmPasswordField_hidesPassword() {
        composeTestRule
            .onNodeWithText("Повторите пароль")
            .performTextInput("12345678")

        composeTestRule
            .onNodeWithText("••••••••")
            .assertIsDisplayed()
    }

    @Test fun loginButton_popsBackStack() {
        composeTestRule.onNodeWithText("Уже есть аккаунт? Войти")
            .performClick()
        verify { navController.popBackStack() }
    }
}