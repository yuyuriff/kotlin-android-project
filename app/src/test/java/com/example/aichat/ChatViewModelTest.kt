package com.example.aichat

import com.example.aichat.model.AiRepository
import com.example.aichat.model.ChatViewModel
import io.mockk.coEvery
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
    private val mockRepository = mockk<AiRepository>()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ChatViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addUserMessageTest() = runTest {
        val testMessage = "Hello"
        val aiResponse = "Hi there!"
        coEvery { mockRepository.getAiResponse(any(), any()) } returns aiResponse

        viewModel.sendMessage(testMessage, "llama3:8b")

        val messages = viewModel.messages
        assertEquals(2, messages.size)
        assertEquals(testMessage, messages[0].text)
        assertEquals(aiResponse, messages[1].text)
        assertTrue(messages[0].isUser)
        assertFalse(messages[1].isUser)
    }

    @Test
    fun clearMessagesTest() = runTest {
        viewModel.sendMessage("Test", "llama3")
        viewModel.clearMessages()
        assertTrue(viewModel.messages.isEmpty())
    }
}
