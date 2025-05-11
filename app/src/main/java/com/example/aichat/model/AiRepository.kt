package com.example.aichat.model

import com.example.aichat.data.AiApi

class AiRepository(private val aiApi: AiApi) {
    suspend fun getAiResponse(userMessage: String): String {
        return aiApi.getResponse(userMessage)
    }
}