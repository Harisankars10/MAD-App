package com.example.healthcareapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {

    object Home : Screen("home", "Home", Icons.Default.Home)
    object Appointment : Screen("appointment", "Appointments", Icons.Default.Event)
    object Records : Screen("records", "Records", Icons.Default.List)
    object Reminder : Screen("reminder", "Reminder", Icons.Default.Notifications)
    object Chatbot : Screen("chatbot", "Chatbot", Icons.Default.Chat)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}