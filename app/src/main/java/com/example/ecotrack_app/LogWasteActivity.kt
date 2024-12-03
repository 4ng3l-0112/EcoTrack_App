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
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class LogWasteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_waste)

        // Enable back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val wasteTypeSpinner: Spinner = findViewById(R.id.wasteTypeSpinner)
        val notesEditText: EditText = findViewById(R.id.notesEditText)
        val weightEditText: EditText = findViewById(R.id.weightEditText)

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
            val weightStr = weightEditText.text.toString()

            if (weightStr.isEmpty()) {
                Toast.makeText(this, "Please enter weight", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weight = weightStr.toFloat()
            saveWasteLog(wasteType, weight, notes)
            Toast.makeText(this, "Waste logged: $wasteType - $weight kg", Toast.LENGTH_SHORT).show()
            triggerNotification(wasteType)
            finish()
        }

        val cancelButton: Button = findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            finish()
        }

        createNotificationChannel()
    }

    private fun saveWasteLog(wasteType: String, weight: Float, notes: String) {
        val sharedPref = getSharedPreferences("waste_logs", Context.MODE_PRIVATE)
        val existingLogsStr = sharedPref.getString("logs", "[]")
        val logsArray = JSONArray(existingLogsStr)

        val newLog = JSONObject().apply {
            put("type", wasteType)
            put("weight", weight)
            put("notes", notes)
            put("date", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
        }

        logsArray.put(newLog)
        
        with(sharedPref.edit()) {
            putString("logs", logsArray.toString())
            apply()
        }

        // Update total waste for goals
        val currentTotal = sharedPref.getFloat("total_waste", 0f)
        with(sharedPref.edit()) {
            putFloat("total_waste", currentTotal + weight)
            apply()
        }
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
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Waste Logged Successfully")
            .setContentText("You have logged $wasteType waste.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, notificationBuilder.build())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}