package com.example.healthcareapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// ✅ Data Class
data class Appointment(
    val id: String = "",
    val doctor: String = "",
    val date: String = "",
    val time: String = "",
    val email: String = ""
)

@Composable
fun ViewAppointmentsScreen(goBack: () -> Unit) {

    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    var appointmentList by remember { mutableStateOf(listOf<Appointment>()) }
    var isLoading by remember { mutableStateOf(true) }

    // 🔥 Fetch Data (UPDATED PATH)
    LaunchedEffect(Unit) {
        db.collection("users")
            .document(userId!!)
            .collection("appointments")
            .get()
            .addOnSuccessListener { result ->

                val list = result.map {
                    Appointment(
                        id = it.id,
                        doctor = it.getString("doctor") ?: "",
                        date = it.getString("date") ?: "",
                        time = it.getString("time") ?: "",
                        email = it.getString("email") ?: ""
                    )
                }

                appointmentList = list
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            "📋 Your Appointments",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {

            if (appointmentList.isEmpty()) {
                Text("No Appointments Found ❌")
            } else {

                LazyColumn {
                    items(appointmentList) { appointment ->

                        AppointmentCard(
                            appointment = appointment,
                            onDelete = {

                                db.collection("users")
                                    .document(userId!!)
                                    .collection("appointments")
                                    .document(appointment.id)
                                    .delete()
                                    .addOnSuccessListener {

                                        appointmentList =
                                            appointmentList.filter {
                                                it.id != appointment.id
                                            }
                                    }
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { goBack() }) {
            Text("Back")
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: Appointment,
    onDelete: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text("👨‍⚕️ Doctor: ${appointment.doctor}")
            Text("📅 Date: ${appointment.date}")
            Text("⏰ Time: ${appointment.time}")
            Text("📧 Email: ${appointment.email}")

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        }
    }

    // ✅ Confirmation Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },

            confirmButton = {
                Button(onClick = {
                    onDelete()
                    showDialog = false
                }) {
                    Text("Yes")
                }
            },

            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("No")
                }
            },

            text = {
                Text("Delete this appointment?")
            }
        )
    }
}