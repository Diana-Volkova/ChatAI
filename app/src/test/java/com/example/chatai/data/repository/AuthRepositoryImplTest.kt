package com.example.chatai.data.repository

import com.example.chatai.data.local.SessionManager
import com.example.chatai.data.remote.api.ChatApi
import com.example.chatai.data.remote.dto.LoginRequest
import com.example.chatai.data.remote.dto.RefreshRequest
import com.example.chatai.data.remote.dto.RegisterRequest
import com.example.chatai.data.remote.dto.TokenResponse
import com.example.chatai.domain.error.AuthException
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthRepositoryImplTest {

    private lateinit var api: ChatApi
    private lateinit var sessionManager: SessionManager
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        api = mockk()
        sessionManager = mockk()
        repository = AuthRepositoryImpl(
            api = api,
            sessionManager = sessionManager,
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login saves tokens when response is successful`() = runTest {
        val responseBody = TokenResponse(
            access_token = "access-token",
            refresh_token = "refresh-token",
            token_type = "token-type"
        )

        val response = Response.success(responseBody)

        coEvery {
            api.login(
                LoginRequest(
                    email = "test@mail.com",
                    password = "12345678",
                )
            )
        } returns response

        coEvery {
            sessionManager.saveTokens(
                accessToken = any(),
                refreshToken = any(),
            )
        } just Runs

        repository.login(
            email = "test@mail.com",
            password = "12345678",
        )

        coVerify {
            sessionManager.saveTokens(
                accessToken = "access-token",
                refreshToken = "refresh-token",
            )
        }
    }

    @Test
    fun `login throws AuthException when response is unsuccessful`() = runTest {
        val response = Response.error< TokenResponse>(
            401,
            "Unauthorized".toResponseBody()
        )

        coEvery {
            api.login(any())
        } returns response

        try {
            repository.login(
                email = "test@mail.com",
                password = "wrong-password",
            )

            fail("Expected AuthException")
        } catch (e: AuthException) {
            assertEquals(401, e.code)
        }
    }

    @Test
    fun `login throws IllegalStateException when response body is null`() = runTest {
        val response = Response.success<TokenResponse>(null)

        coEvery {
            api.login(any())
        } returns response

        try {
            repository.login(
                email = "test@mail.com",
                password = "123456",
            )

            fail("Expected IllegalStateException")
        } catch (e: IllegalStateException) {
            assertEquals("Empty response", e.message)
        }
    }

    @Test
    fun `register completes when response is successful`() = runTest {
        val response = Response.success<Unit>(Unit)

        coEvery {
            api.register(
                RegisterRequest(
                    email = "test@mail.com",
                    password = "12345678",
                )
            )
        } returns response

        repository.register(
            email = "test@mail.com",
            password = "12345678",
        )

        coVerify {
            api.register(
                RegisterRequest(
                    email = "test@mail.com",
                    password = "12345678",
                )
            )
        }
    }

    @Test
    fun `register throws AuthException when response is unsuccessful`() = runTest {
        val response = Response.error<Unit>(
            400,
            "Bad Request".toResponseBody()
        )

        coEvery {
            api.register(any())
        } returns response

        try {
            repository.register(
                email = "test@mail.com",
                password = "12345678",
            )

            fail("Expected AuthException")
        } catch (e: AuthException) {
            assertEquals(400, e.code)
        }
    }

    @Test
    fun `logout completes when response is successful`() = runTest {
        val response = Response.success<Unit>(Unit)

        coEvery {
            api.logout(
                RefreshRequest("refresh-token")
            )
        } returns response

        repository.logout("refresh-token")

        coVerify {
            api.logout(
                RefreshRequest("refresh-token")
            )
        }
    }

    @Test
    fun `logout throws AuthException when response is unsuccessful`() = runTest {
        val response = Response.error<Unit>(
            401,
            "Unauthorized".toResponseBody()
        )

        coEvery {
            api.logout(any())
        } returns response

        try {
            repository.logout("refresh-token")

            fail("Expected AuthException")
        } catch (e: AuthException) {
            assertEquals(401, e.code)
        }
    }

    @Test
    fun `deleteAccount completes when response is successful`() = runTest {
        val response = Response.success<Unit>(Unit)

        coEvery {
            api.deleteAccount()
        } returns response

        repository.deleteAccount()

        coVerify {
            api.deleteAccount()
        }
    }

    @Test
    fun `deleteAccount throws AuthException when response is unsuccessful`() = runTest {
        val response = Response.error<Unit>(
            401,
            "Unauthorized".toResponseBody()
        )

        coEvery {
            api.deleteAccount()
        } returns response

        try {
            repository.deleteAccount()

            fail("Expected AuthException")
        } catch (e: AuthException) {
            assertEquals(401, e.code)
        }
    }
}