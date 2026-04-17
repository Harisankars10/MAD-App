package com.example.healthcareapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatbotScreen(goBack: () -> Unit) {

    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "🤖 AI Chatbot",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 💬 Messages list
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(messages) { msg ->
                Text(
                    text = if (msg.isUser)
                        "You: ${msg.text}"
                    else
                        "Bot: ${msg.text}",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ✏️ Input row
        Row {

            TextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask something...") }
            )

            IconButton(
                onClick = {

                    if (message.isNotBlank()) {

                        val userMsg = ChatMessage(message, true)
                        val botMsg = ChatMessage(getBotReply(message), false)

                        messages = messages + userMsg + botMsg
                        message = ""
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send"
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = goBack) {
            Text("Back")
        }
    }
}