package com.example.ecotrack_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import java.text.SimpleDateFormat
import java.util.*
import org.json.JSONArray
import android.graphics.BitmapFactory
import android.util.Base64
import kotlinx.coroutines.*

class WasteHistoryActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WasteHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waste_history)

        // Enable back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.wasteHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        launch {
            val entries = loadWasteEntries()
            adapter = WasteHistoryAdapter(entries)
            recyclerView.adapter = adapter
        }
    }

    private suspend fun loadWasteEntries(): List<WasteEntry> = withContext(Dispatchers.IO) {
        val sharedPref = getSharedPreferences("waste_logs", MODE_PRIVATE)
        val logsStr = sharedPref.getString("logs", "[]") ?: "[]"
        val logsArray = JSONArray(logsStr)
        
        val entries = mutableListOf<WasteEntry>()
        for (i in 0 until logsArray.length()) {
            val log = logsArray.getJSONObject(i)
            entries.add(
                WasteEntry(
                    type = log.getString("type"),
                    amount = log.getDouble("amount"),
                    date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        .parse(log.getString("date")) ?: Date(),
                    photoBase64 = log.optString("photo", null)
                )
            )
        }
        entries.reversed() // Show newest entries first
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel() // Cancel all coroutines
    }
}

data class WasteEntry(
    val type: String,
    val amount: Double,
    val date: Date,
    val photoBase64: String? = null
)

class WasteHistoryAdapter(private var wasteEntries: List<WasteEntry>) :
    RecyclerView.Adapter<WasteHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wasteTypeTextView: TextView = view.findViewById(R.id.wasteTypeTextView)
        val wasteAmountTextView: TextView = view.findViewById(R.id.wasteAmountTextView)
        val wasteDateTextView: TextView = view.findViewById(R.id.wasteDateTextView)
        val wastePhotoThumbnail: ImageView = view.findViewById(R.id.wastePhotoThumbnail)
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

        // Load photo if available
        entry.photoBase64?.let { base64String ->
            try {
                val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                holder.wastePhotoThumbnail.setImageBitmap(bitmap)
                holder.wastePhotoThumbnail.visibility = View.VISIBLE
            } catch (e: Exception) {
                holder.wastePhotoThumbnail.visibility = View.GONE
            }
        } ?: run {
            holder.wastePhotoThumbnail.visibility = View.GONE
        }
    }

    override fun getItemCount() = wasteEntries.size

    fun updateEntries(newEntries: List<WasteEntry>) {
        wasteEntries = newEntries
        notifyDataSetChanged()
    }
}
