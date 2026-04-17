package com.example.healthcareapp

fun getBotReply(message: String): String {
    val msg = message.lowercase()

    return when {
        msg.contains("hello") -> "Hello! 👋 How can I help you?"
        msg.contains("hi") -> "Hi there! 😊"
        msg.contains("appointment") -> "You can book an appointment from dashboard."
        msg.contains("medicine") -> "Take your medicines on time 💊"
        msg.contains("bye") -> "Goodbye! Take care 👋"
        else -> "I am still learning 🤖"
    }
}