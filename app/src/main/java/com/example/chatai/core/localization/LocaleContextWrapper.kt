package com.example.chatai.core.localization

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleContextWrapper {

    fun wrap(context: Context, language: String): Context {
        if (language == LanguageManager.SYSTEM) {
            return context
        }

        val locale = Locale.forLanguageTag(language)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        return context.createConfigurationContext(configuration)
    }
}