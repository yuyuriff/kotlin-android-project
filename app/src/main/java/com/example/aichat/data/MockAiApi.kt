package com.example.aichat.data

import kotlinx.coroutines.delay

class MockAiApi {
    suspend fun getResponse(userMessage: String): String {
        delay(1000)
        return when (userMessage.lowercase()) {
            "привет" -> "Здравствуйте!"
            else -> "Я пока только учусь. Повторите вопрос иначе?"
        }
    }
}
