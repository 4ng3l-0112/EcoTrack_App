package com.example.ecotrack_app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.widget.Toast
import android.widget.ImageButton
import android.widget.EditText

class GoalsActivity : AppCompatActivity() {
    private lateinit var progressText: TextView
    private var currentGoal: Int = 300 // Default goal value
    private lateinit var progressBars: Array<ProgressBar>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        try {
            // Enable back button
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            // Initialize views
            progressText = findViewById(R.id.progressText)
            val backButton: ImageButton = findViewById(R.id.backButton)
            val setGoalButton: Button = findViewById(R.id.setGoalButton)

            // Initialize progress bars
            progressBars = arrayOf(
                findViewById(R.id.mondayProgress),
                findViewById(R.id.tuesdayProgress),
                findViewById(R.id.wednesdayProgress),
                findViewById(R.id.thursdayProgress),
                findViewById(R.id.fridayProgress),
                findViewById(R.id.saturdayProgress),
                findViewById(R.id.sundayProgress)
            )

            // Set initial progress text
            updateProgressText()

            // Setup back button
            backButton.setOnClickListener {
                onBackPressed()
            }

            // Setup button click
            setGoalButton.setOnClickListener {
                try {
                    showSetGoalDialog()
                } catch (e: Exception) {
                    Log.e("GoalsActivity", "Error in button click: ${e.message}", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Log.e("GoalsActivity", "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error initializing activity: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateProgressText() {
        // Calculate total progress (sum of all progress values)
        val currentProgress = progressBars.sumOf { it.progress }
        progressText.text = "$currentProgress/$currentGoal"
    }

    private fun showSetGoalDialog() {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            hint = "Enter goal in kg"
        }

        AlertDialog.Builder(this)
            .setTitle("Set New Goal")
            .setView(input)
            .setPositiveButton("Set") { _, _ ->
                try {
                    val newGoal = input.text.toString().toIntOrNull()
                    if (newGoal != null && newGoal > 0) {
                        currentGoal = newGoal
                        updateProgressText()
                        Toast.makeText(this, "Goal updated to ${currentGoal}kg", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Please enter a valid number greater than 0", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("GoalsActivity", "Error setting goal: ${e.message}", e)
                    Toast.makeText(this, "Error setting goal: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
