package com.example.healthcareapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    // 🔥 Observe login state
    var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

    // 🔐 If logged out → show login
    if (!isLoggedIn) {
        LoginScreen(
            onLoginSuccess = { isLoggedIn = true },
            goToRegister = {}
        )
        return
    }

    // ✅ ALL NAV ITEMS
    val items = listOf(
        Screen.Home,
        Screen.Appointment,
        Screen.Records,
        Screen.Reminder,
        Screen.Chatbot,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {

            NavigationBar {

                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                items.forEach { screen ->

                    NavigationBarItem(
                        selected = currentRoute == screen.route,

                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo("home")
                                launchSingleTop = true
                            }
                        },

                        icon = {
                            Icon(screen.icon, contentDescription = screen.title)
                        },

                        label = {
                            Text(screen.title)
                        }
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {

            // 🏠 HOME (Dashboard)
            composable("home") {
                DashboardScreen(
                    onLogout = {
                        FirebaseAuth.getInstance().signOut()
                        isLoggedIn = false // ✅ FIX
                    },
                    goToAppointment = { navController.navigate("appointment") },
                    goToViewAppointments = { navController.navigate("records") },
                    goToReminder = { navController.navigate("reminder") },
                    goToChatbot = { navController.navigate("chatbot") }
                )
            }

            // 📅 APPOINTMENT
            composable("appointment") {
                AppointmentScreen(
                    goBack = { navController.popBackStack() }
                )
            }

            // 📋 RECORDS
            composable("records") {
                ViewAppointmentsScreen(
                    goBack = { navController.popBackStack() }
                )
            }

            // 🔔 REMINDER
            composable("reminder") {
                MedicineReminderScreen(
                    goBack = { navController.popBackStack() }
                )
            }

            // 🤖 CHATBOT
            composable("chatbot") {
                ChatbotScreen(
                    goBack = { navController.popBackStack() }
                )
            }

            // 👤 PROFILE
            composable("profile") {
                ProfileScreen()
            }
        }
    }
}