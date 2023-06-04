package com.nickytoolchick.agraph

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.databinding.ActivityMainBinding
import com.nickytoolchick.agraph.ui.ChartOptionsActivity

class MainActivity : AppCompatActivity() {

    private var chartOptions = ChartOptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.configureChartButton.setOnClickListener {
            val configIntent = Intent(this, ChartOptionsActivity::class.java)
            configIntent.putExtra("stableChartOptions", chartOptions)
            resultLauncher.launch(configIntent)
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            chartOptions = result.data?.getSerializableExtra("result") as ChartOptions
            Log.d("chart", chartOptions.horizontalStep.toString())
        }
    }
}