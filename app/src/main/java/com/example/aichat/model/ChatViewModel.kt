package com.example.aichat.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aichat.data.ChatMessage
import com.example.aichat.data.OllamaAiApi
import kotlinx.coroutines.launch

class ChatViewModel(
    private val aiRepository: AiRepository = AiRepository(
        OllamaAiApi("https://adapted-satyr-strong.ngrok-free.app/api/ollama/")
    )
) : ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun sendGreeting(text: String) {
        _messages.add(ChatMessage(text, isUser = false))
    }

    fun sendMessage(
        text: String,
        modelInfo: ModelInfo,
    ) {
        if (text.isBlank() || _isLoading.value) return

        _messages.add(ChatMessage(text, isUser = true))
        _isLoading.value = true

        val fullPrompt = """
            ${modelInfo.systemPrompt}
            User: $text
        """.trimIndent()

        viewModelScope.launch {
            try {
                val aiResponse = aiRepository.getAiResponse(fullPrompt, modelInfo.apiName)
                _messages.add(ChatMessage(aiResponse, isUser = false))
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        _messages.clear()
        aiRepository.clearContext()
    }
}
