package com.example.aichat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.aichat.model.ModelInfo
import com.example.aichat.ui.components.ChatScreen
import com.example.aichat.ui.components.ModelSelectionScreen
import com.example.aichat.ui.theme.AiChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiChatTheme {
                var selectedModel by rememberSaveable { mutableStateOf<ModelInfo?>(null) }

                if (selectedModel == null) {
                    ModelSelectionScreen { model ->
                        selectedModel = model
                    }
                } else {
                    ChatScreen(
                        modelInfo = selectedModel!!,
                        onBack = { selectedModel = null }
                    )
                }
            }
        }
    }
}
