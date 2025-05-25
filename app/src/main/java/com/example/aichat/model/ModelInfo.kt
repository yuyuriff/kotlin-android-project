package com.example.aichat.model

data class ModelInfo(
    val displayName: String,
    val apiName: String
)

val availableModels = listOf(
    ModelInfo("Llama 3", "llama3:8b"),
    ModelInfo("Mistral", "mistral"),
)
