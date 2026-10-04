package com.example.chatai.presentation.ui.password_restore

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
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
class PasswordRestoreScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mockk<NavController>(relaxed = true)
    private val viewModel = mockk<PasswordRestoreViewModel>(relaxed = true)

    private val effects =
        MutableSharedFlow<PasswordRestoreEffect>()

    @Before
    fun setup() {
        every { viewModel.effects } returns effects

        composeTestRule.setContent {
            PasswordRestoreScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    @Test
    fun sendButton_isDisabled_whenEmailIsEmpty() {
        composeTestRule
            .onNodeWithText(TestStrings.send)
            .assertIsNotEnabled()
    }

    @Test
    fun sendButton_isEnabled_whenEmailIsEntered() {
        composeTestRule
            .onNodeWithText(TestStrings.email)
            .performTextInput("test@test.com")

        composeTestRule
            .onNodeWithText(TestStrings.send)
            .assertIsEnabled()
    }

    @Test
    fun sendButton_dispatchesSendResetLink() {
        composeTestRule
            .onNodeWithText(TestStrings.email)
            .performTextInput("test@test.com")

        composeTestRule
            .onNodeWithText(TestStrings.send)
            .performClick()

        composeTestRule.runOnIdle {
            verify {
                viewModel.dispatch(
                    PasswordRestoreIntent.SendResetLink(
                        "test@test.com"
                    )
                )
            }
        }
    }

    @Test
    fun backToLogin_callsPopBackStack() {
        composeTestRule
            .onNodeWithText(TestStrings.backToLogin)
            .performClick()

        verify {
            navController.popBackStack()
        }
    }
}