package com.example.chatai

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun mainActivity_displaysSplashScreen() {
        composeTestRule
            .onNodeWithTag("splash_screen")
            .assertIsDisplayed()
    }

    @Test
    fun mainActivity_displaysNavigation_afterSplash() {
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule
                .onAllNodesWithTag("navigation")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag("navigation")
            .assertIsDisplayed()
    }

    @Test
    fun activity_usesSavedLanguage() {
        composeTestRule.activityRule.scenario.onActivity { activity ->
            val locale = activity.resources.configuration.locales[0]

            assertEquals(
                "ru",
                locale.language
            )
        }
    }
}