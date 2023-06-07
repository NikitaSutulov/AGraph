package com.nickytoolchick.agraph.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.data.Constants
import com.nickytoolchick.agraph.data.DatasetOptions
import com.nickytoolchick.agraph.databinding.ActivityChartRenderBinding
import com.nickytoolchick.agraph.render.ChartRenderer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChartRenderActivity : AppCompatActivity() {

    var chartOptions = ChartOptions()
    var datasetOptions = DatasetOptions()
    lateinit var mainActivityIntent: Intent
    private lateinit var binding: ActivityChartRenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartRenderBinding.inflate(layoutInflater)

        mainActivityIntent = intent
        loadOptions()
        initChartRenderer()
        setContentView(binding.root)
    }

    private fun initChartRenderer() {
        binding.chartRenderer.setChartOptions(chartOptions)
        binding.chartRenderer.setDatasetOptions(datasetOptions)
    }

    private fun loadOptions() {
        chartOptions = Json.decodeFromString(mainActivityIntent.getStringExtra(Constants.STABLE_CHART_OPTIONS)!!)
        datasetOptions = Json.decodeFromString(mainActivityIntent.getStringExtra(Constants.STABLE_DATASET_OPTIONS)!!)
    }
}