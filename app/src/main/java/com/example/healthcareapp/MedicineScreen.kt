package com.example.healthcareapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MedicineScreen(goBack: () -> Unit) {

    var medicine by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        Text(
            "💊 Medicine Reminder",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = medicine,
            onValueChange = { medicine = it },
            label = { Text("Medicine Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("Time (e.g. 08:00 AM)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            // UI only screen (no alarm here)
        }) {
            Text("Save Reminder")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = goBack) {
            Text("Back")
        }
    }
}