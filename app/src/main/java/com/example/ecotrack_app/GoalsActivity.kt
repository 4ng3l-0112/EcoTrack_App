package com.example.ecotrack_app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.widget.Toast
import android.widget.EditText
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.util.ArrayList
import kotlin.collections.sumOf

class GoalsActivity : AppCompatActivity() {
    private lateinit var goalProgressText: TextView
    private lateinit var barChart: BarChart
    private var currentGoal: Int = 100 // Default goal value in kg

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals_temp)

        try {
            // Enable back button
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            // Initialize views
            goalProgressText = findViewById(R.id.goalProgressText)
            barChart = findViewById(R.id.barChart)
            val setGoalButton: Button = findViewById(R.id.setGoalButton)

            // Setup initial chart
            setupBarChart()

            // Update progress text
            updateProgressText()

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

    private fun setupBarChart() {
        try {
            val entries = mutableListOf<BarEntry>()
            entries.add(BarEntry(0f, 30f)) // Monday
            entries.add(BarEntry(1f, 35f)) // Tuesday
            entries.add(BarEntry(2f, 40f)) // Wednesday
            entries.add(BarEntry(3f, 25f)) // Thursday
            entries.add(BarEntry(4f, 45f)) // Friday
            entries.add(BarEntry(5f, 20f)) // Saturday
            entries.add(BarEntry(6f, 30f)) // Sunday

            val dataSet = BarDataSet(entries, "Daily Waste (kg)")
            dataSet.color = Color.GREEN
            
            val data = BarData(dataSet)
            barChart.data = data
            
            // Customize the chart
            barChart.description.isEnabled = false
            barChart.legend.isEnabled = false
            barChart.setFitBars(true)
            barChart.animateY(1000)
            
            // Update the chart
            barChart.invalidate()
        } catch (e: Exception) {
            Log.e("GoalsActivity", "Error setting up bar chart: ${e.message}", e)
            Toast.makeText(this, "Error setting up chart: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProgressText() {
        try {
            // Calculate total progress (sum of all bar values)
            val currentProgress = barChart.data?.getDataSetByIndex(0)?.values?.sumOf { it.y.toDouble() }?.toInt() ?: 0
            goalProgressText.text = "Current Progress: ${currentProgress}kg / ${currentGoal}kg"
        } catch (e: Exception) {
            Log.e("GoalsActivity", "Error updating progress text: ${e.message}", e)
            Toast.makeText(this, "Error updating progress: ${e.message}", Toast.LENGTH_SHORT).show()
        }
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
        finish()
        return true
    }
}
