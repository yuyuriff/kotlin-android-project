package com.example.ollamaserver.model

data class ClientRequest(
    val model: String,
    val prompt: String,
    val context: List<Long>,
)

data class ServerChatResponse(
    val response: String,
    val context: List<Long>
)

data class OllamaGenerateRequest(
    val model: String,
    val prompt: String,
    val context: List<Long>,
    val stream: Boolean = false,
)

data class OllamaGenerateResponse(
    val model: String,
    val created_at: String,
    val response: String,
    val done: Boolean,
    val done_reason: String? = null,
    val context: List<Long>,
    val total_duration: Long? = null,
    val load_duration: Long? = null,
    val prompt_eval_count: Int? = null,
    val prompt_eval_duration: Long? = null,
    val eval_count: Int? = null,
    val eval_duration: Long? = null
)