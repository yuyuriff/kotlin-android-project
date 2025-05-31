package com.example.aichat.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.aichat.model.ChatViewModel
import com.example.aichat.data.ModelInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    modelInfo: ModelInfo,
    viewModel: ChatViewModel,
    onBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    CenterAlignedTopAppBar(
        title = { Text(modelInfo.displayName) },
        navigationIcon = {
            IconButton(onClick = {
                onBack()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        },
        actions = {
            IconButton(
                onClick = {
                    viewModel.deleteAndRestartSession(modelInfo)
                }
            ) {
                Icon(Icons.Default.Delete, "Delete session", tint = Color.Red)
            }
        },
        modifier = Modifier.height(50.dp),
        scrollBehavior = scrollBehavior,
    )
}
