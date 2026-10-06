package com.example.chatai.core.theme

import android.content.Context
import androidx.core.content.edit

class ThemeManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "theme_preferences"
        private const val KEY_THEME = "theme"

        const val SYSTEM = "system"
        const val LIGHT = "light"
        const val DARK = "dark"
    }

    private val prefs =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getTheme(): String {
        return prefs.getString(KEY_THEME, SYSTEM) ?: SYSTEM
    }

    fun setTheme(theme: String) {
        prefs.edit {
            putString(KEY_THEME, theme)
        }
    }
}