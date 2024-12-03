package com.example.ecotrack_app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import android.graphics.Color

class GoalsActivity : AppCompatActivity() {
    private lateinit var barChart: BarChart
    private lateinit var goalProgressText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        barChart = findViewById(R.id.barChart)
        goalProgressText = findViewById(R.id.goalProgressText)
        
        setupBarChart()
        loadBarData()

        val setGoalButton: Button = findViewById(R.id.setGoalButton)
        setGoalButton.setOnClickListener {
            // TODO: Implement goal setting functionality
        }
    }

    private fun setupBarChart() {
        barChart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            setPinchZoom(false)
            setScaleEnabled(false)
            
            // Customize X axis
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                valueFormatter = IndexAxisValueFormatter(getDaysOfWeek())
                textSize = 12f
            }
            
            // Customize left axis
            axisLeft.apply {
                setDrawGridLines(true)
                setDrawZeroLine(true)
                setDrawLimitLinesBehindData(true)
                axisMinimum = 0f
                textSize = 12f
            }
            
            // Customize right axis
            axisRight.isEnabled = false
            
            // Customize legend
            legend.isEnabled = false
            
            // Add animation
            animateY(1000)
        }
    }

    private fun loadBarData() {
        val entries = ArrayList<BarEntry>().apply {
            add(BarEntry(0f, 11f)) // Monday
            add(BarEntry(1f, 13f)) // Tuesday
            add(BarEntry(2f, 15f)) // Wednesday
            add(BarEntry(3f, 10f)) // Thursday
            add(BarEntry(4f, 12f)) // Friday
            add(BarEntry(5f, 14f)) // Saturday
            add(BarEntry(6f, 15f)) // Sunday
        }

        val dataSet = BarDataSet(entries, "Daily Waste").apply {
            color = Color.parseColor("#00796B") // Material Design Teal
            valueTextSize = 12f
            valueTextColor = Color.BLACK
        }

        val data = BarData(dataSet).apply {
            barWidth = 0.7f
        }

        barChart.data = data
        barChart.invalidate()
    }

    private fun getDaysOfWeek(): ArrayList<String> {
        return ArrayList<String>().apply {
            add("M")
            add("T")
            add("W")
            add("Th")
            add("F")
            add("Sat")
            add("Sun")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
