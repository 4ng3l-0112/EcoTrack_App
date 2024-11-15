package com.example.ecotrack_app


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView

class TipsResourcesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips_resources)

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        val articleTextView: TextView = findViewById(R.id.articleTextView)

        // Mock data display for the articles
        articleTextView.text = "Reducing Plastic Waste: Tips on minimizing plastic usage."
    }
}
