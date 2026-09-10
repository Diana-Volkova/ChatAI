package com.example.chatai.core.localization

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class LocaleContextWrapperTest {

    private val context = RuntimeEnvironment.getApplication()

    @Test
    fun `system language returns original context`() {
        val result = LocaleContextWrapper.wrap(
            context,
            LanguageManager.SYSTEM
        )

        assertSame(context, result)
    }

    @Test
    fun `language changes locale`() {
        val result = LocaleContextWrapper.wrap(
            context,
            "ru"
        )

        assertEquals(
            Locale.forLanguageTag("ru"),
            result.resources.configuration.locales[0]
        )
    }

    @Test
    fun `language with region changes locale`() {
        val result = LocaleContextWrapper.wrap(
            context,
            "en-US"
        )

        assertEquals(
            Locale.forLanguageTag("en-US"),
            result.resources.configuration.locales[0]
        )
    }
}