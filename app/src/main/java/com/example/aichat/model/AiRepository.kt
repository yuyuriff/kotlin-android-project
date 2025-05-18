package com.example.aichat.model

import com.example.aichat.data.AiApi
import com.example.aichat.data.ChatRequest
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AiRepository(private val aiApi: AiApi) {
    suspend fun getAiResponse(userMessage: String): String {
        return try {
            val response = aiApi.getResponse(ChatRequest(prompt = userMessage))

            when {
                response.isSuccessful -> {
                    response.body()?.response ?: "Empty response from the server"
                }
                response.code() == 503 -> {
                    "Server is temporarily unavailable. Please try again later."
                }
                else -> {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error."
                    "Server has thrown (${response.code()}): $errorBody"
                }
            }
        } catch (e: IOException) {
            return when (e) {
                is UnknownHostException -> "No internet connection"
                is ConnectException -> "Failed to connect to server"
                is SocketTimeoutException -> "Connection timeout"
                else -> "Network error: ${e.message ?: "Unknown IO error"}"
            }
        } catch (e: Exception) {
            "Error: ${e.localizedMessage ?: "Unknown error."}"
        }
    }
}