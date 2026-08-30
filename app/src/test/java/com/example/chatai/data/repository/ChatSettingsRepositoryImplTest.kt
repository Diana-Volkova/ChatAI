package com.example.chatai.data.repository

import com.example.chatai.data.local.ChatSettingsDao
import com.example.chatai.data.local.ChatSettingsEntity
import com.example.chatai.data.mappers.toDomain
import com.example.chatai.domain.model.ChatSettings
import com.example.chatai.domain.theme.ChatThemeId
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ChatSettingsRepositoryImplTest {
    private lateinit var dao: ChatSettingsDao
    private lateinit var repository: ChatSettingsRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        dao = mockk()

        repository = ChatSettingsRepositoryImpl(
            dao = dao
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `observe returns domain settings when entity exists`() = runTest {
        val chatId = 10

        val entity = ChatSettingsEntity(
            chatId = chatId,
            theme = ChatThemeId.DEFAULT.name
        )

        every {
            dao.observe(chatId)
        } returns flowOf(entity)

        val result = repository.observe(chatId).first()

        assertEquals(
            entity.toDomain(),
            result
        )
    }

    @Test
    fun `observe returns default settings when entity is null`() = runTest {
        val chatId = 10

        every {
            dao.observe(chatId)
        } returns flowOf(null)

        val result = repository.observe(chatId).first()

        assertEquals(
            ChatSettings(
                chatId = chatId,
                theme = ChatThemeId.DEFAULT
            ),
            result
        )
    }

    @Test
    fun `setTheme upserts settings`() = runTest {
        val chatId = 10
        val theme = ChatThemeId.DEFAULT

        coEvery {
            dao.upsert(
                ChatSettingsEntity(
                    chatId = chatId,
                    theme = theme.name
                )
            )
        } just Runs

        repository.setTheme(
            chatId = chatId,
            theme = theme
        )

        coVerify(exactly = 1) {
            dao.upsert(
                ChatSettingsEntity(
                    chatId = chatId,
                    theme = theme.name
                )
            )
        }
    }
}