package com.example.aichat.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aichat.data.ChatMessage
import com.example.aichat.data.ChatSession
import com.example.aichat.data.ModelInfo
import com.example.aichat.data.OllamaAiApi
import kotlinx.coroutines.launch

class ChatViewModel(
    private val aiRepository: AiRepository = AiRepository(
        OllamaAiApi("https://adapted-satyr-strong.ngrok-free.app/api/ollama/")
    ),
    private val chatHistory: ChatHistoryRepository
) : ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var currentModelName: String? = null

    private val _sessionLoaded = mutableStateOf(false)
    val sessionLoaded: State<Boolean> = _sessionLoaded

    fun initializeSession(modelInfo: ModelInfo) {
        viewModelScope.launch {
            val session = loadSession(modelInfo.apiName)

            if (session == null) {
                startNewSession(modelInfo)
            }

            _sessionLoaded.value = true
        }
    }

    private fun startNewSession(model: ModelInfo) {
        currentModelName = model.apiName
        _messages.clear()
        sendGreeting(model.greeting)
    }

    private suspend fun loadSession(modelName: String): ChatSession? {
        return chatHistory.loadSession(modelName)?.also { session ->
                currentModelName = session.modelName
                _messages.clear()
                _messages.addAll(session.messages)
            }
    }

    fun deleteAndRestartSession(modelInfo: ModelInfo) {
        viewModelScope.launch {
            currentModelName?.let { modelName ->
                chatHistory.deleteSession(modelName)
            }
            _messages.clear();
            startNewSession(modelInfo)
        }
    }

    fun saveCurrentSession() {
        viewModelScope.launch {
            currentModelName?.let { modelName ->
                val session = ChatSession(
                    modelName = modelName,
                    messages = _messages.toList(),
                    context = aiRepository.currentContext,
                )
                chatHistory.saveSession(session)
            }
        }
    }

    private fun sendGreeting(text: String) {
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

    fun clearCurrentSession() {
        _messages.clear()
        aiRepository.clearContext()
        _sessionLoaded.value = false
    }
}
