package com.example.aichat

import com.example.aichat.model.AiRepository
import com.example.aichat.model.ChatHistoryRepository
import com.example.aichat.model.ChatViewModel
import com.example.aichat.model.ModelInfo
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {
    private lateinit var viewModel: ChatViewModel
    private val mockAiRepository = mockk<AiRepository>()
    private val mockHistoryRepository = mockk<ChatHistoryRepository>()
    private val testDispatcher = StandardTestDispatcher()
    private val modelInfo = ModelInfo(
        displayName = "Alice",
        apiName = "llama3:8b",
        systemPrompt = """
            You are Alice, a well-read librarian who loves literature. While you keep a friendly conversation you can:
            - Recommend books based on user's mood
            - Discuss philosophical ideas accessibly
            - Share interesting quotes when relevant
            - Adapt to user's reading level
            - Write shorter messages resembling human chat
            - Occasionally use emoji
            - Example: "That reminds me of a passage from 'The Midnight Library'..."
        """.trimIndent(),
        greeting = "Hello! Do you want to join my book club?",
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ChatViewModel(mockAiRepository, mockHistoryRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addUserMessageTest() = runTest {
        val testMessage = "Hello"
        val aiResponse = "Hi there!"

        coEvery { mockAiRepository.getAiResponse(any(), any()) } returns aiResponse

        viewModel.sendMessage(testMessage, modelInfo)

        testDispatcher.scheduler.advanceUntilIdle()

        val messages = viewModel.messages
        assertEquals(2, messages.size)
        assertEquals(testMessage, messages[0].text)
        assertEquals(aiResponse, messages[1].text)
        assertTrue(messages[0].isUser)
        assertFalse(messages[1].isUser)
    }

    @Test
    fun clearMessagesTest() = runTest {
        coEvery { mockAiRepository.clearContext() } just Runs
        coEvery { mockAiRepository.getAiResponse(any(), any()) } returns "ok"

        viewModel.sendMessage("Test", modelInfo)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.clearCurrentSession()
        assertTrue(viewModel.messages.isEmpty())
    }
}
