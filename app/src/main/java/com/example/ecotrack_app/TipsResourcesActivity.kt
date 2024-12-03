package com.example.ecotrack_app


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView

class TipsResourcesActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips_resources)

        // Enable back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        val articleTextView: TextView = findViewById(R.id.articleTextView)

        // Mock data display for the articles
        articleTextView.text = "Reducing Plastic Waste: Tips on minimizing plastic usage."
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
