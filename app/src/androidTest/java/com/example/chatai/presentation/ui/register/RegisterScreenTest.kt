package com.example.chatai.presentation.ui.register

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.chatai.presentation.ui.TestStrings
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
        composeTestRule
            .onNodeWithText(TestStrings.email)
            .performTextInput("test@example.com")

        composeTestRule
            .onNodeWithText(TestStrings.password)
            .performTextInput("12345678")

        composeTestRule
            .onNodeWithText(TestStrings.createAccount)
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
        composeTestRule .onNodeWithText(TestStrings.registration)
            .assertIsDisplayed()

        composeTestRule .onNodeWithText(TestStrings.userName)
            .assertIsDisplayed()

        composeTestRule .onNodeWithText(TestStrings.email)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(TestStrings.password)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(TestStrings.repeatPassword)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(TestStrings.createAccount)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(TestStrings.alreadyHaveAccount)
            .assertIsDisplayed()
    }

    @Test
    fun nameField_acceptsInput() {
        composeTestRule.onNodeWithText(TestStrings.userName)
            .performTextInput("Alex")

        composeTestRule.onNodeWithText("Alex")
            .assertIsDisplayed()
    }

    @Test
    fun passwordField_hidesText() {
        composeTestRule
            .onNodeWithText(TestStrings.password)
            .performTextInput("12345678")

        composeTestRule
            .onNodeWithText("••••••••")
            .assertIsDisplayed()
    }

    @Test
    fun confirmPasswordField_hidesPassword() {
        composeTestRule
            .onNodeWithText(TestStrings.repeatPassword)
            .performTextInput("12345678")

        composeTestRule
            .onNodeWithText("••••••••")
            .assertIsDisplayed()
    }

    @Test fun loginButton_popsBackStack() {
        composeTestRule.onNodeWithText(TestStrings.alreadyHaveAccount)
            .performClick()
        verify { navController.popBackStack() }
    }
}