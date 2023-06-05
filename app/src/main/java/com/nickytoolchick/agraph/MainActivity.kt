package com.nickytoolchick.agraph

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.data.DatasetOptions
import com.nickytoolchick.agraph.databinding.ActivityMainBinding
import com.nickytoolchick.agraph.ui.ChartOptionsActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {

    private var chartOptions = ChartOptions()
    private var datasetOptions = DatasetOptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.configureChartButton.setOnClickListener {
            val charOptionsIntent = Intent(this, ChartOptionsActivity::class.java)
            charOptionsIntent.putExtra("stableChartOptions", Json.encodeToString(chartOptions))
            chartOptionsResultLauncher.launch(charOptionsIntent)
        }
        binding.configureDatasetButton.setOnClickListener {
            val datasetOptionsIntent = Intent(this, DatasetOptionsActivity::class.java)
            datasetOptionsIntent.putExtra("stableDatasetOptions", Json.encodeToString(datasetOptions))
            datasetOptionsResultLauncher.launch(datasetOptionsIntent)
        }
    }

    private val chartOptionsResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            chartOptions = Json.decodeFromString(result.data?.getStringExtra("newChartOptions")!!)
        }
    }

    private val datasetOptionsResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            datasetOptions = Json.decodeFromString(result.data?.getStringExtra("newDatasetOptions")!!)
        }
    }
}