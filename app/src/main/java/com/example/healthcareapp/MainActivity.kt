package com.example.healthcareapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.healthcareapp.ui.theme.HealthCareAppTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HealthCareAppTheme {

                val auth = FirebaseAuth.getInstance()

                // 🔥 ONLY LOGIN FLOW CONTROL HERE
                var isLoggedIn by remember {
                    mutableStateOf(auth.currentUser != null)
                }

                if (isLoggedIn) {

                    // ✅ AFTER LOGIN → SHOW BOTTOM NAVIGATION
                    MainScreen()

                } else {

                    var showRegister by remember { mutableStateOf(false) }

                    if (showRegister) {

                        RegisterScreen(
                            goToLogin = { showRegister = false }
                        )

                    } else {

                        LoginScreen(
                            onLoginSuccess = { isLoggedIn = true },
                            goToRegister = { showRegister = true }
                        )
                    }
                }
            }
        }
    }
}