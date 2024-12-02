package com.example.ecotrack_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.os.Build

class LogWasteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_waste)

        val wasteTypeSpinner: Spinner = findViewById(R.id.wasteTypeSpinner)
        val notesEditText: EditText = findViewById(R.id.notesEditText)

        ArrayAdapter.createFromResource(
            this, R.array.waste_type_array, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            wasteTypeSpinner.adapter = adapter
        }

        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            val wasteType = wasteTypeSpinner.selectedItem.toString()
            val notes = notesEditText.text.toString()
            // Code to save waste entry goes here
            Toast.makeText(this, "Waste logged: $wasteType", Toast.LENGTH_SHORT).show()
            triggerNotification(wasteType) // Call to trigger the notification
            finish()
        }

        val cancelButton: Button = findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            finish()
        }

        createNotificationChannel() // Create notification channel
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Waste Logging Channel"
            val descriptionText = "Channel for waste logging notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("WASTE_LOGGING_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun triggerNotification(wasteType: String) {
        val notificationBuilder = NotificationCompat.Builder(this, "WASTE_LOGGING_CHANNEL")
            .setSmallIcon(R.drawable.notification_icon) // Replace with your notification icon
            .setContentTitle("Waste Logged")
            .setContentText("You have logged $wasteType.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Dismiss the notification when clicked

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, notificationBuilder.build())
    }
}