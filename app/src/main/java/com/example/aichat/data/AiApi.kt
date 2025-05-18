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
    val prompt: String
)

data class ChatResponse(
    val response: String
)

class OllamaAiApi(private val baseUrl: String) : AiApi {
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
