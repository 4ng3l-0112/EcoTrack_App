package com.example.ecotrack_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

data class WasteEntry(
    val type: String,
    val amount: Double,
    val date: Date
)

class WasteHistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WasteHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waste_history)

        // Enable back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.wasteHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // TODO: Replace this with actual data from your database
        val sampleData = listOf(
            WasteEntry("Plastic", 0.5, Date()),
            WasteEntry("Paper", 1.0, Date()),
            WasteEntry("Glass", 0.75, Date())
        )
        
        adapter = WasteHistoryAdapter(sampleData)
        recyclerView.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

class WasteHistoryAdapter(private val wasteEntries: List<WasteEntry>) : 
    RecyclerView.Adapter<WasteHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wasteTypeTextView: TextView = view.findViewById(R.id.wasteTypeTextView)
        val wasteAmountTextView: TextView = view.findViewById(R.id.wasteAmountTextView)
        val wasteDateTextView: TextView = view.findViewById(R.id.wasteDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_waste_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = wasteEntries[position]
        holder.wasteTypeTextView.text = entry.type
        holder.wasteAmountTextView.text = "${entry.amount} kg"
        
        val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        holder.wasteDateTextView.text = dateFormat.format(entry.date)
    }

    override fun getItemCount() = wasteEntries.size
}
