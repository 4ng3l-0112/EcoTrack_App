package com.example.ecotrack_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.ProgressBar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val totalWasteTextView: TextView = findViewById(R.id.totalWasteTextView)
        totalWasteTextView.text = "Total Waste Tracked: 150 kg"

        val quickLogButton: Button = findViewById(R.id.quickLogButton)
        quickLogButton.setOnClickListener {
            startActivity(Intent(this, LogWasteActivity::class.java))
        }

        val goalsButton: Button = findViewById(R.id.goalsButton)
        goalsButton.setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
        }

        val tipsResourcesButton: Button = findViewById(R.id.tipsResourcesButton)
        tipsResourcesButton.setOnClickListener {
            startActivity(Intent(this, TipsResourcesActivity::class.java))
        }

        val communityChallengesButton: Button = findViewById(R.id.communityChallengesButton)
        communityChallengesButton.setOnClickListener {
            startActivity(Intent(this, CommunityChallengesActivity::class.java))
        }
    }
}