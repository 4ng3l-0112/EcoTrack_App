package com.example.ecotrack_app


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class CommunityChallengesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_challenges)

        val challengesTextView: TextView = findViewById(R.id.challengesTextView)

// Mock data display for challenges
        challengesTextView.text = "Join the 'Plastic-Free Week' challenge to reduce plastic usage."
    }
}