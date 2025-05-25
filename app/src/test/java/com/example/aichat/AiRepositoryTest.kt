package com.example.aichat

import com.example.aichat.data.AiApi
import com.example.aichat.data.ChatResponse
import com.example.aichat.model.AiRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import retrofit2.Response
import java.net.UnknownHostException

class AiRepositoryTest {
    private lateinit var repository: AiRepository
    private val mockApi = mockk<AiApi>()

    @Before
    fun setup() {
        repository = AiRepository(mockApi)
    }

    @Test
    fun successResponseTest() = runTest {
        val testPrompt = "Hello"
        val expectedResponse = ChatResponse("Hi", emptyList())
        coEvery { mockApi.getResponse(any()) } returns Response.success(expectedResponse)

        val result = repository.getAiResponse(testPrompt, "llama3:8b")

        assertEquals("Hi", result)
    }

    @Test
    fun networkErrorTest() = runTest {
        coEvery { mockApi.getResponse(any()) } throws UnknownHostException()

        val result = repository.getAiResponse("Test", "llama3:8b")

        assertTrue(result.contains("No internet connection"))
    }
}