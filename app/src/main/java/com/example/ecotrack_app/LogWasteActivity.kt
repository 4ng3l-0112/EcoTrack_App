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
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*

class LogWasteActivity : AppCompatActivity() {
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 123

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
            // Use coroutine for background operation
            CoroutineScope(Dispatchers.IO).launch {
                saveWasteLog(wasteType, weight, notes)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LogWasteActivity, "Waste logged: $wasteType - $weight kg", Toast.LENGTH_SHORT).show()
                    if (checkNotificationPermission()) {
                        triggerNotification(wasteType)
                    }
                    finish()
                }
            }
        }

        val cancelButton: Button = findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            finish()
        }

        createNotificationChannel()
        checkAndRequestNotificationPermission()
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private suspend fun saveWasteLog(wasteType: String, weight: Float, notes: String) {
        withContext(Dispatchers.IO) {
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
        try {
            val notificationBuilder = NotificationCompat.Builder(this, "WASTE_LOGGING_CHANNEL")
                .setSmallIcon(android.R.drawable.ic_dialog_info)  // Using system icon as fallback
                .setContentTitle("Waste Logged Successfully")
                .setContentText("You have logged $wasteType waste.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(this)
            if (checkNotificationPermission()) {
                notificationManager.notify(1, notificationBuilder.build())
            }
        } catch (e: SecurityException) {
            // Handle permission denial gracefully
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}