package com.example.ecotrack_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.*
import android.view.View

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private var _pieChart: PieChart? = null
    private val pieChart get() = _pieChart!!
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _pieChart = findViewById(R.id.pieChart)
        
        // Initialize views and setup chart in background
        launch(Dispatchers.Default) {
            setupPieChart()
            loadPieChartData()
        }

        setupButtons()
        setupWasteText()
    }

    private fun setupWasteText() {
        val totalWasteTextView: TextView = findViewById(R.id.totalWasteTextView)
        totalWasteTextView.text = "Total Waste Tracked: 150 kg"
    }

    private fun setupButtons() {
        val buttons = mapOf(
            R.id.quickLogButton to LogWasteActivity::class.java,
            R.id.goalsButton to GoalsActivity::class.java,
            R.id.tipsResourcesButton to TipsResourcesActivity::class.java,
            R.id.communityChallengesButton to CommunityChallengesActivity::class.java,
            R.id.viewHistoryButton to WasteHistoryActivity::class.java
        )

        buttons.forEach { (buttonId, activityClass) ->
            findViewById<Button>(buttonId)?.setOnClickListener {
                startActivity(Intent(this, activityClass))
            }
        }
    }

    private suspend fun setupPieChart() = withContext(Dispatchers.Main) {
        pieChart.apply {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(12f)
            setEntryLabelColor(resources.getColor(android.R.color.black))
            centerText = "Waste Distribution"
            setCenterTextSize(20f)
            description.isEnabled = false
            legend.isEnabled = true
            legend.textSize = 12f
            setDrawEntryLabels(false) // Disable entry labels for better performance
        }
    }

    private suspend fun loadPieChartData() {
        // Prepare data in background thread
        val entries = withContext(Dispatchers.Default) {
            ArrayList<PieEntry>().apply {
                add(PieEntry(30f, "Plastic"))
                add(PieEntry(25f, "Paper"))
                add(PieEntry(20f, "Glass"))
                add(PieEntry(15f, "Metal"))
                add(PieEntry(10f, "Other"))
            }
        }

        val colors = ArrayList<Int>().apply {
            addAll(ColorTemplate.MATERIAL_COLORS)
        }

        // Update UI on main thread
        withContext(Dispatchers.Main) {
            val dataSet = PieDataSet(entries, "Waste Categories").apply {
                this.colors = colors
                valueTextSize = 12f
                valueTextColor = resources.getColor(android.R.color.black)
                valueFormatter = PercentFormatter(pieChart)
            }

            pieChart.apply {
                data = PieData(dataSet)
                invalidate()
                animateY(500) // Reduced animation time
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel() // Cancel all coroutines when the activity is destroyed
        _pieChart = null // Clear reference to prevent memory leaks
    }
}
