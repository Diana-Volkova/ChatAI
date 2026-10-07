package com.example.chatai.core.theme

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ThemeManagerTest {

    private lateinit var context: Context
    private lateinit var themeManager: ThemeManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()

        context
            .getSharedPreferences(
                "theme_preferences",
                Context.MODE_PRIVATE
            )
            .edit()
            .clear()
            .commit()

        themeManager = ThemeManager(context)
    }

    @Test
    fun system_theme_is_used_by_default() {
        assertEquals(
            ThemeManager.SYSTEM,
            themeManager.getTheme()
        )
    }

    @Test
    fun theme_is_saved_when_changed_from_system_to_dark() {
        themeManager.setTheme(ThemeManager.DARK)

        assertEquals(
            ThemeManager.DARK,
            themeManager.getTheme()
        )
    }

    @Test
    fun theme_is_changed_from_dark_back_to_system() {
        themeManager.setTheme(ThemeManager.DARK)
        themeManager.setTheme(ThemeManager.SYSTEM)

        assertEquals(
            ThemeManager.SYSTEM,
            themeManager.getTheme()
        )
    }

    @Test
    fun dark_theme_is_persisted_and_restored() {
        assertEquals(
            ThemeManager.SYSTEM,
            themeManager.getTheme()
        )

        themeManager.setTheme(ThemeManager.DARK)

        val newThemeManager = ThemeManager(context)

        assertEquals(
            ThemeManager.DARK,
            newThemeManager.getTheme()
        )
    }
}