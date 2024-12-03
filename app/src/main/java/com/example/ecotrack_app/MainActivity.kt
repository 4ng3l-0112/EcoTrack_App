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

class MainActivity : AppCompatActivity() {
    private lateinit var pieChart: PieChart

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pieChart = findViewById(R.id.pieChart)
        setupPieChart()
        loadPieChartData()

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

        val viewHistoryButton: Button = findViewById(R.id.viewHistoryButton)
        viewHistoryButton.setOnClickListener {
            startActivity(Intent(this, WasteHistoryActivity::class.java))
        }
    }

    private fun setupPieChart() {
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
        }
    }

    private fun loadPieChartData() {
        val entries = ArrayList<PieEntry>().apply {
            add(PieEntry(30f, "Plastic"))
            add(PieEntry(25f, "Paper"))
            add(PieEntry(20f, "Glass"))
            add(PieEntry(15f, "Metal"))
            add(PieEntry(10f, "Other"))
        }

        val colors = ArrayList<Int>().apply {
            for (color in ColorTemplate.MATERIAL_COLORS) {
                add(color)
            }
        }

        val dataSet = PieDataSet(entries, "Waste Categories").apply {
            this.colors = colors
            valueTextSize = 12f
            valueTextColor = resources.getColor(android.R.color.black)
            valueFormatter = PercentFormatter(pieChart)
        }

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate()
        pieChart.animateY(1000)
    }
}
