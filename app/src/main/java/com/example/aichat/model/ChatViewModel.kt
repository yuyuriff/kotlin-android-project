package com.example.aichat.model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aichat.data.ChatMessage
import com.example.aichat.data.MockAiApi
import kotlinx.coroutines.launch

class ChatViewModel(
    private val aiRepository: AiRepository = AiRepository(MockAiApi())
) : ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        _messages.add(ChatMessage(text, isUser = true))

        viewModelScope.launch {
            val aiResponse = aiRepository.getAiResponse(text)
            _messages.add(ChatMessage(aiResponse, isUser = false))
        }
    }
}
