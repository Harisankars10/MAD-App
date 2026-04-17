package com.example.healthcareapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@Composable
fun AppointmentScreen(goBack: () -> Unit) {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    var doctorName by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        Text(
            text = "🏥 Book Appointment",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(modifier = Modifier.padding(16.dp)) {

                // 👨‍⚕️ Doctor Name
                OutlinedTextField(
                    value = doctorName,
                    onValueChange = { doctorName = it },
                    label = { Text("Doctor Name") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 📅 DATE FIELD (FULL CLICK WORKING)
                Box(modifier = Modifier.fillMaxWidth()) {

                    OutlinedTextField(
                        value = date,
                        onValueChange = {},
                        label = { Text("Select Date") },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {

                                val calendar = Calendar.getInstance()

                                DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
                                        date = String.format(
                                            "%02d/%02d/%04d",
                                            day, month + 1, year
                                        )
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ⏰ TIME FIELD (FULL CLICK WORKING)
                Box(modifier = Modifier.fillMaxWidth()) {

                    OutlinedTextField(
                        value = time,
                        onValueChange = {},
                        label = { Text("Select Time") },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {

                                val calendar = Calendar.getInstance()

                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->

                                        val formattedTime = String.format(
                                            "%02d:%02d %s",
                                            if (hour % 12 == 0) 12 else hour % 12,
                                            minute,
                                            if (hour < 12) "AM" else "PM"
                                        )

                                        time = formattedTime
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    false
                                ).show()
                            }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 🚀 BOOK BUTTON
                Button(
                    onClick = {

                        if (doctorName.isBlank() || date.isBlank() || time.isBlank()) {
                            message = "Please fill all fields ❌"
                            return@Button
                        }

                        if (user == null) {
                            message = "User not logged in ❌"
                            return@Button
                        }

                        isLoading = true

                        val appointment = hashMapOf(
                            "doctor" to doctorName,
                            "date" to date,
                            "time" to time,
                            "email" to user.email
                        )

                        db.collection("users")
                            .document(user.uid)
                            .collection("appointments")
                            .add(appointment)
                            .addOnSuccessListener {

                                message = "Appointment Booked ✅"
                                isLoading = false

                                val notificationHelper = NotificationHelper(context)
                                notificationHelper.showNotification(
                                    "Appointment Booked",
                                    "Doctor: $doctorName on $date at $time"
                                )

                                doctorName = ""
                                date = ""
                                time = ""
                            }
                            .addOnFailureListener {
                                message = "Failed to book appointment ❌"
                                isLoading = false
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    Text(if (isLoading) "Booking..." else "Book Appointment")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = if (message.contains("✅")) Color(0xFF2E7D32) else Color.Red
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { goBack() }) {
            Text("Back")
        }
    }
}