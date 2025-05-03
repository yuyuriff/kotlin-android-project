package com.example.aichat.data

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage (
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
) {
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun getFormattedTime(): String = timeFormat.format(Date(timestamp))
}
