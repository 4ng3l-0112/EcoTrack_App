package com.example.ecotrack_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import android.os.Build
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CommunityChallengesActivity : AppCompatActivity() {
    private lateinit var finishedChallengesAdapter: FinishedChallengesAdapter
    private val finishedChallenges = mutableListOf<FinishedChallenge>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_challenges)

        // Enable back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val challengesTextView: TextView = findViewById(R.id.challengesTextView)
        val joinButton: Button = findViewById(R.id.joinChallengeButton)
        val completeButton: Button = findViewById(R.id.completeChallengeButton)

        // Setup RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.finishedChallengesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        finishedChallengesAdapter = FinishedChallengesAdapter(finishedChallenges)
        recyclerView.adapter = finishedChallengesAdapter

        challengesTextView.text = "Current Challenge:\n\nPlastic-Free Week\nReduce your plastic usage for one week and track your progress!\n\nParticipants: 150\nDays Remaining: 5"

        createNotificationChannel()

        joinButton.setOnClickListener {
            Toast.makeText(this, "You've joined the Plastic-Free Week challenge!", Toast.LENGTH_SHORT).show()
            sendChallengeNotification()
        }

        completeButton.setOnClickListener {
            Toast.makeText(this, "Congratulations! You've completed the challenge!", Toast.LENGTH_SHORT).show()
            sendCompletionNotification()
            addFinishedChallenge("Plastic-Free Week")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Community Challenges"
            val descriptionText = "Notifications for community challenges"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHALLENGE_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendChallengeNotification() {
        val builder = NotificationCompat.Builder(this, "CHALLENGE_CHANNEL")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Challenge Joined!")
            .setContentText("You've joined the Plastic-Free Week challenge. Good luck!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, builder.build())
    }

    private fun sendCompletionNotification() {
        val builder = NotificationCompat.Builder(this, "CHALLENGE_CHANNEL")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Challenge Completed! ")
            .setContentText("Congratulations! You've successfully completed the Plastic-Free Week challenge!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(3, builder.build())
    }

    private fun addFinishedChallenge(challengeName: String) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        
        finishedChallenges.add(0, FinishedChallenge(challengeName, currentDate))
        finishedChallengesAdapter.updateChallenges(finishedChallenges)
    }
}