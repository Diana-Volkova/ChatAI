package com.example.chatai.core.localization

import android.app.LocaleManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.LocaleList
import androidx.core.content.edit
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.util.ReflectionHelpers

@RunWith(RobolectricTestRunner::class)
class LanguageManagerTest {

    private lateinit var context: Context
    private lateinit var prefs: SharedPreferences
    private lateinit var localeManager: LocaleManager

    private lateinit var languageManager: LanguageManager

    @Before
    fun setUp() {
        context = mockk()
        prefs = mockk(relaxed = true)
        localeManager = mockk(relaxed = true)

        every {
            context.getSharedPreferences(
                "language_preferences",
                Context.MODE_PRIVATE
            )
        } returns prefs

        every {
            context.getSystemService(LocaleManager::class.java)
        } returns localeManager

        languageManager = LanguageManager(context)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getLanguage returns saved language on pre Tiramisu`() {
        setSdkVersion(Build.VERSION_CODES.S)

        every {
            prefs.getString("language", LanguageManager.SYSTEM)
        } returns LanguageManager.RU

        assertThat(languageManager.getLanguage())
            .isEqualTo(LanguageManager.RU)
    }

    @Test
    fun `getLanguage returns system by default on pre Tiramisu`() {
        setSdkVersion(Build.VERSION_CODES.S)

        every {
            prefs.getString("language", LanguageManager.SYSTEM)
        } returns LanguageManager.SYSTEM

        assertThat(languageManager.getLanguage())
            .isEqualTo(LanguageManager.SYSTEM)
    }

    @Test
    fun `getLanguage returns system when preference is null`() {
        setSdkVersion(Build.VERSION_CODES.S)

        every {
            prefs.getString("language", LanguageManager.SYSTEM)
        } returns null

        assertThat(languageManager.getLanguage())
            .isEqualTo(LanguageManager.SYSTEM)
    }

    @Test
    fun `setLanguage saves language on pre Tiramisu`() {
        setSdkVersion(Build.VERSION_CODES.S)

        languageManager.setLanguage(LanguageManager.RU)

        verify {
            prefs.edit {
                putString("language", LanguageManager.RU)
            }
        }
    }

    @Test
    fun `setLanguage sets russian locale on Tiramisu`() {
        setSdkVersion(Build.VERSION_CODES.TIRAMISU)

        languageManager.setLanguage(LanguageManager.RU)

        verify {
            localeManager.applicationLocales =
                LocaleList.forLanguageTags(LanguageManager.RU)
        }
    }

    @Test
    fun `setLanguage sets english locale on Tiramisu`() {
        setSdkVersion(Build.VERSION_CODES.TIRAMISU)

        languageManager.setLanguage(LanguageManager.EN)

        verify {
            localeManager.applicationLocales =
                LocaleList.forLanguageTags(LanguageManager.EN)
        }
    }

    @Test
    fun `setLanguage clears locale when system is selected`() {
        setSdkVersion(Build.VERSION_CODES.TIRAMISU)

        languageManager.setLanguage(LanguageManager.SYSTEM)

        verify {
            localeManager.applicationLocales =
                LocaleList.getEmptyLocaleList()
        }
    }

    private fun setSdkVersion(version: Int) {
        ReflectionHelpers.setStaticField(
            Build.VERSION::class.java,
            "SDK_INT",
            version
        )
    }
}