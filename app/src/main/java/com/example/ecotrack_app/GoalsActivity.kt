package com.example.ecotrack_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ProgressBar
import android.widget.ImageButton
import android.widget.TextView

class GoalsActivity : AppCompatActivity() {

    private lateinit var progressBars: Map<String, ProgressBar>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        // Initialize back button
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        // Initialize progress bars
        progressBars = mapOf(
            "monday" to findViewById(R.id.mondayProgress),
            "tuesday" to findViewById(R.id.tuesdayProgress),
            "wednesday" to findViewById(R.id.wednesdayProgress),
            "thursday" to findViewById(R.id.thursdayProgress),
            "friday" to findViewById(R.id.fridayProgress),
            "saturday" to findViewById(R.id.saturdayProgress),
            "sunday" to findViewById(R.id.sundayProgress)
        )

        // Set progress values (these would normally come from a database)
        setDailyProgress(mapOf(
            "monday" to 40,
            "tuesday" to 60,
            "wednesday" to 80,
            "thursday" to 30,
            "friday" to 50,
            "saturday" to 70,
            "sunday" to 90
        ))

        // Update total progress text
        val progressText: TextView = findViewById(R.id.progressText)
        progressText.text = "150/300"
    }

    private fun setDailyProgress(progressValues: Map<String, Int>) {
        progressValues.forEach { (day, progress) ->
            progressBars[day]?.progress = progress
        }
    }
}
