package com.example.aichat.data

import kotlinx.coroutines.delay

interface AiApi {
    suspend fun getResponse(userMessage: String): String
}

class MockAiApi : AiApi {
    override suspend fun getResponse(userMessage: String): String {
        delay(1000)
        return when (userMessage.lowercase()) {
            "привет" -> "Здравствуйте!"
            else -> "Я пока только учусь. Повторите вопрос иначе?"
        }
    }
}
