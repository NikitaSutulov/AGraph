package com.nickytoolchick.agraph

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.data.DatasetOptions
import com.nickytoolchick.agraph.data.Constants
import com.nickytoolchick.agraph.databinding.ActivityMainBinding
import com.nickytoolchick.agraph.fileio.FileReader
import com.nickytoolchick.agraph.fileio.FileWriter
import com.nickytoolchick.agraph.ui.ChartOptionsActivity
import com.nickytoolchick.agraph.ui.ChartRenderActivity
import com.nickytoolchick.agraph.ui.DatasetOptionsActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {

    private var chartOptions = ChartOptions()
    private var datasetOptions = DatasetOptions()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setButtonsOnClickListeners()
    }

    private fun setButtonsOnClickListeners() {
        binding.configureChartButton.setOnClickListener {
            val charOptionsIntent = Intent(this, ChartOptionsActivity::class.java)
            charOptionsIntent.putExtra(Constants.STABLE_CHART_OPTIONS, Json.encodeToString(chartOptions))
            chartOptionsResultLauncher.launch(charOptionsIntent)
        }
        binding.configureDatasetButton.setOnClickListener {
            val datasetOptionsIntent = Intent(this, DatasetOptionsActivity::class.java)
            datasetOptionsIntent.putExtra(Constants.STABLE_DATASET_OPTIONS, Json.encodeToString(datasetOptions))
            datasetOptionsResultLauncher.launch(datasetOptionsIntent)
        }
        binding.renderChartButton.setOnClickListener {
            val chartRenderIntent = Intent(this, ChartRenderActivity::class.java).apply {
                putExtra(Constants.STABLE_CHART_OPTIONS, Json.encodeToString(chartOptions))
                putExtra(Constants.STABLE_DATASET_OPTIONS, Json.encodeToString(datasetOptions))
            }
            startActivity(chartRenderIntent)
        }
        binding.saveButton.setOnClickListener {
            val fileWriter = FileWriter()
            fileWriter.saveFile(this, chartOptions, datasetOptions)
        }
        binding.loadButton.setOnClickListener {
            val fileReader = FileReader()
            val loadedOptions = fileReader.readFile(this)
            chartOptions = loadedOptions.first
            datasetOptions = loadedOptions.second
            validateOptions()
        }
    }

    private val chartOptionsResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            chartOptions = Json.decodeFromString(result.data?.getStringExtra(Constants.NEW_CHART_OPTIONS)!!)
            validateOptions()
        }
    }

    private val datasetOptionsResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            datasetOptions = Json.decodeFromString(result.data?.getStringExtra(Constants.NEW_DATASET_OPTIONS)!!)
            validateOptions()
        }
    }

    private fun validateOptions() {
        val numbersToCheck = listOf(
            chartOptions.horizontalStep,
            chartOptions.verticalStep,
            chartOptions.xMin,
            chartOptions.xMax,
            chartOptions.yMin,
            chartOptions.yMax,
            chartOptions.strokeSize,
            chartOptions.pointRadius
        )
        binding.renderChartButton.isEnabled = (!numbersToCheck.any { it == 0f }
                && datasetOptions.points.isNotEmpty())
    }
}