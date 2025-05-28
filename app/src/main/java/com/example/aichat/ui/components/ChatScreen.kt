package com.example.aichat.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

import com.example.aichat.model.ChatViewModel
import com.example.aichat.model.ModelInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel(),
    modelInfo: ModelInfo,
    onBack: () -> Unit
) {
    var userInput by rememberSaveable { mutableStateOf("") }
    val messages = viewModel.messages

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    val isLoading = viewModel.isLoading

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.lastIndex)
            }
        }
    }

    var hasSentGreeting by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(modelInfo.apiName) {
        if (!hasSentGreeting) {
            viewModel.sendGreeting(modelInfo.greeting)
            hasSentGreeting = true
        }
    }

    val pinnedScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            ChatTopBar(
                modelInfo = modelInfo,
                viewModel = viewModel,
                onBack = onBack,
                scrollBehavior = pinnedScrollBehavior,
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus()
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                MessagesList(
                    modifier = Modifier.weight(1f),
                    listState = listState,
                    messages = messages,
                )

                MessageInputField(
                    text = userInput,
                    onTextChange = { userInput = it },
                    onSend = {
                        if (userInput.isNotBlank()) {
                            viewModel.sendMessage(userInput, modelInfo)
                            userInput = ""
                        }
                    },
                    isLoading = isLoading.value,
                )
            }
        }
    }
}
