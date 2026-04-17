package com.example.healthcareapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    goToAppointment: () -> Unit,
    goToViewAppointments: () -> Unit,
    goToReminder: () -> Unit,
    goToChatbot: () -> Unit
) {

    val user = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // 🔷 HEADER (Gradient)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1976D2),
                            Color(0xFF42A5F5)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = user?.email ?: "User",
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        // 🔻 MAIN CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-30).dp) // Pull cards upward
                .padding(16.dp)
        ) {

            // 🧩 GRID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DashboardCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Event,
                    title = "Book Appointment",
                    color = Color(0xFF4CAF50),
                    onClick = goToAppointment
                )

                DashboardCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.List,
                    title = "View Records",
                    color = Color(0xFF2196F3),
                    onClick = goToViewAppointments
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DashboardCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Notifications,
                    title = "Reminders",
                    color = Color(0xFFFF9800),
                    onClick = goToReminder
                )

                DashboardCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Chat,
                    title = "AI Chatbot",
                    color = Color(0xFF9C27B0),
                    onClick = goToChatbot
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔴 LOGOUT BUTTON
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout")
            }
        }
    }
}

@Composable
fun DashboardCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    color: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .padding(8.dp)
            .height(140.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clickable { onClick() },

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(55.dp)
                    .background(
                        color.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}