package com.example.healthcareapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun MedicineReminderScreen(goBack: () -> Unit) {

    var medicineName by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("Select Time") }
    var message by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        Text(
            "💊 Medicine Reminder",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // MEDICINE NAME
        OutlinedTextField(
            value = medicineName,
            onValueChange = { medicineName = it },
            label = { Text("Medicine Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TIME PICKER
        Button(onClick = {

            val calendar = Calendar.getInstance()

            val timePicker = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )

            timePicker.show()

        }) {
            Text("Select Time: $selectedTime")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SET REMINDER
        Button(onClick = {

            if (medicineName.isEmpty() || selectedTime == "Select Time") {
                message = "❌ Please enter all details"
                return@Button
            }

            val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("medicine", medicineName)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            // 🔥 Convert selected time to real trigger time
            val timeParts = selectedTime.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()

            val calendar = Calendar.getInstance()
            val now = Calendar.getInstance()

            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            // If time already passed → schedule for next day
            if (calendar.timeInMillis <= now.timeInMillis) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            val triggerTime = calendar.timeInMillis

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )

            message = "✅ Reminder set for $selectedTime"

        }) {
            Text("Set Reminder")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(message)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = goBack) {
            Text("Back")
        }
    }
}