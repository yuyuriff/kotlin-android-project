package com.example.aichat.data

import android.icu.text.SimpleDateFormat
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.Locale

@Serializable
data class ChatMessage (
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
) {
    fun getFormattedTime(): String =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
}

@Serializable
data class ChatSession(
    val modelName: String,
    val messages: List<ChatMessage>,
    val context: List<Long> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)
