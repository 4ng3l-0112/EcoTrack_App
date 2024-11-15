package com.example.ecotrack_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

class LogWasteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_waste)

        val wasteTypeSpinner: Spinner = findViewById(R.id.wasteTypeSpinner)
        val subcategorySpinner: Spinner = findViewById(R.id.subcategorySpinner)
        val notesEditText: EditText = findViewById(R.id.notesEditText)

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
            // Code to save waste entry goes here
            Toast.makeText(this, "Waste logged: $wasteType", Toast.LENGTH_SHORT).show()
            finish()
        }

        val cancelButton: Button = findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            finish()
        }
    }
}
