package com.example.aichat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aichat.data.ChatMessage

@Composable
fun MessagesList(
    modifier: Modifier,
    listState: LazyListState,
    messages: List<ChatMessage>,
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.Bottom,
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        items(messages) { message ->
            MessageBubble(message)
        }
    }
}