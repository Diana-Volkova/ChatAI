package com.example.chatai.core.localization

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.core.content.edit

class LanguageManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "language_preferences"
        private const val KEY_LANGUAGE = "language"

        const val SYSTEM = "system"
        const val RU = "ru"
        const val EN = "en"
    }

    private val prefs =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getLanguage(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager =
                context.getSystemService(LocaleManager::class.java)

            localeManager.applicationLocales
                .get(0)
                ?.language
                ?: SYSTEM
        } else {
            prefs.getString(KEY_LANGUAGE, SYSTEM) ?: SYSTEM
        }
    }

    fun setLanguage(language: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager =
                context.getSystemService(LocaleManager::class.java)

            localeManager.applicationLocales =
                if (language == SYSTEM) {
                    LocaleList.getEmptyLocaleList()
                } else {
                    LocaleList.forLanguageTags(language)
                }
        } else {
            prefs.edit {
                putString(KEY_LANGUAGE, language)
            }
        }
    }
}