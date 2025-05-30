package com.example.aichat.model

import android.content.Context
import com.example.aichat.data.ChatSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.ListSerializer
import java.io.File
import java.io.IOException

class ChatHistoryRepository(private val context: Context) {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private val historyFile: File by lazy {
        File(context.filesDir, "chat_history.json").apply {
            if (!exists()) createNewFile()
        }
    }

    suspend fun saveSession(session: ChatSession) = withContext(Dispatchers.IO) {
        val updatedSession = session.copy(timestamp = System.currentTimeMillis())
        val sessions = loadAllSessions()
            .filter { it.modelName != session.modelName }
            .plus(updatedSession)
            .sortedByDescending { it.timestamp }

        val serializer = ListSerializer(ChatSession.serializer())
        historyFile.writeText(json.encodeToString<List<ChatSession>>(serializer, sessions))
    }

    suspend fun loadAllSessions(): List<ChatSession> = withContext(Dispatchers.IO) {
        try {
            if (historyFile.length() > 0L) {
                json.decodeFromString<List<ChatSession>>(historyFile.readText())
            } else {
                emptyList()
            }
        } catch (_: IOException) {
            emptyList()
        } catch (_: SerializationException) {
            emptyList()
        }
    }

    suspend fun loadSession(modelName: String): ChatSession? = withContext(Dispatchers.IO) {
        loadAllSessions().firstOrNull { it.modelName == modelName }
    }

    suspend fun deleteSession(modelName: String) = withContext(Dispatchers.IO) {
        val updated = loadAllSessions().filter { it.modelName != modelName }

        val serializer = ListSerializer(ChatSession.serializer())
        historyFile.writeText(json.encodeToString(serializer, updated))
    }

    suspend fun clearHistory() = withContext(Dispatchers.IO) {
        historyFile.delete()
    }
}
