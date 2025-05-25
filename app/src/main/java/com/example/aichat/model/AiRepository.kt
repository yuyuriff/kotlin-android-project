package com.example.aichat.model

import com.example.aichat.data.AiApi
import com.example.aichat.data.ChatRequest
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AiRepository(private val aiApi: AiApi) {
    private var currentContext: List<Long> = emptyList()

    suspend fun getAiResponse(
        userMessage: String,
        model: String,
    ): String {
        return try {
            val response = aiApi.getResponse(
                ChatRequest(
                    model = model,
                    prompt = userMessage,
                    context = currentContext,
                )
            )

            when {
                response.isSuccessful -> {
                    currentContext = response.body()?.context ?: emptyList()
                    response.body()?.response ?: "Empty response from the server"
                }
                response.code() == HttpURLConnection.HTTP_UNAVAILABLE -> {
                    "Server is temporarily unavailable. Please try again later."
                }
                else -> {
                    "Server has thrown error ${response.code()}"
                }
            }
        } catch (e: IOException) {
            return when (e) {
                is UnknownHostException -> "No internet connection"
                is ConnectException -> "Failed to connect to server"
                is SocketTimeoutException -> "Connection timeout"
                else -> "Network error: ${e.message ?: "Unknown IO error"}"
            }
        }
    }

    fun clearContext() {
        currentContext = emptyList()
    }
}
