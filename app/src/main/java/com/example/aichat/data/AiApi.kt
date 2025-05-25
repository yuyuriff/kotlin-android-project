package com.example.aichat.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AiApi {
    @POST("chat")
    suspend fun getResponse(@Body request: ChatRequest): Response<ChatResponse>
}

data class ChatRequest(
    val model: String = "llama3:8b",
    val prompt: String,
    val context: List<Long> = emptyList(),
)

data class ChatResponse(
    val response: String,
    val context: List<Long>,
)

class OllamaAiApi(private val baseUrl: String): AiApi {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AiApi::class.java)
    }

    override suspend fun getResponse(request: ChatRequest): Response<ChatResponse> {
        return retrofit.getResponse(request)
    }
}
