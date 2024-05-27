package com.nickytoolchick.agraph.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.data.Constants
import com.nickytoolchick.agraph.data.DatasetOptions
import com.nickytoolchick.agraph.databinding.ActivityChartRenderBinding
import com.nickytoolchick.agraph.fileio.FileWriter
import com.nickytoolchick.agraph.render.ChartRenderer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChartRenderActivity : AppCompatActivity() {

    private var chartOptions = ChartOptions()
    private var datasetOptions = DatasetOptions()
    private lateinit var mainActivityIntent: Intent
    private lateinit var binding: ActivityChartRenderBinding
    private val fileWriter = FileWriter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartRenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainActivityIntent = intent
        loadOptions()
        initChartRenderer()
        setupButtonListeners()
    }

    private fun initChartRenderer() {
        binding.chartRenderer.apply {
            setChartOptions(chartOptions)
            setDatasetOptions(datasetOptions)
        }
    }

    private fun loadOptions() {
        chartOptions = Json.decodeFromString(mainActivityIntent.getStringExtra(Constants.STABLE_CHART_OPTIONS)!!)
        datasetOptions = Json.decodeFromString(mainActivityIntent.getStringExtra(Constants.STABLE_DATASET_OPTIONS)!!)
    }

    private fun setupButtonListeners() {
        binding.exportButton.setOnClickListener {
            fileWriter.exportChartAsPng(binding.chartRenderer)
        }
        binding.shareButton.setOnClickListener {
            fileWriter.shareChart(this, binding.chartRenderer)
        }
    }
}
