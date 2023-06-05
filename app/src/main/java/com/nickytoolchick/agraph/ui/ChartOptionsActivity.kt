package com.nickytoolchick.agraph.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.databinding.ActivityChartOptionsBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class ChartOptionsActivity : AppCompatActivity() {

    var chartOptions = ChartOptions()
    lateinit var mainActivityIntent: Intent
    private lateinit var binding: ActivityChartOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainActivityIntent = intent
        loadChartOptions()

        binding.submitChartOptionsButton.setOnClickListener {
            updateChartOptions()
            mainActivityIntent.putExtra("result", Json.encodeToString(chartOptions))
            setResult(Activity.RESULT_OK, mainActivityIntent)
            finish()
        }
    }

    private fun loadChartOptions() {
        chartOptions = Json.decodeFromString(mainActivityIntent.getStringExtra("stableChartOptions")!!)
        loadEditTexts()
        loadCheckedTextViews()
        handleCheckedTextViewsClick()

    }

    private fun loadEditTexts() {
        binding.horizontalStepEditText.setText(chartOptions.horizontalStep.toString())
        binding.verticalStepEditText.setText(chartOptions.verticalStep.toString())
        binding.xMinEditText.setText(chartOptions.xMin.toString())
        binding.xMaxEditText.setText(chartOptions.xMax.toString())
        binding.yMinEditText.setText(chartOptions.yMin.toString())
        binding.yMaxEditText.setText(chartOptions.yMax.toString())
    }

    private fun loadCheckedTextViews() {
        binding.logScaleXCheckedTV.isChecked = chartOptions.isLogScaleX
        binding.logScaleYCheckedTV.isChecked = chartOptions.isLogScaleY
        binding.showHorizontalLinesCheckedTV.isChecked = chartOptions.isHorizontalLines
        binding.showVerticalLinesCheckedTV.isChecked = chartOptions.isVerticalLines
    }

    private fun handleCheckedTextViewsClick() {
        binding.logScaleXCheckedTV.setOnClickListener {
            binding.logScaleXCheckedTV.isChecked = !binding.logScaleXCheckedTV.isChecked
        }

        binding.logScaleYCheckedTV.setOnClickListener {
            binding.logScaleYCheckedTV.isChecked = !binding.logScaleYCheckedTV.isChecked
        }

        binding.showHorizontalLinesCheckedTV.setOnClickListener {
            binding.showHorizontalLinesCheckedTV.isChecked = !binding.showHorizontalLinesCheckedTV.isChecked
        }

        binding.showVerticalLinesCheckedTV.setOnClickListener {
            binding.showVerticalLinesCheckedTV.isChecked = !binding.showVerticalLinesCheckedTV.isChecked
        }
    }

    fun updateChartOptions() {
        updateFromEditTexts()
        updateFromCheckedTextViews()
    }

    private fun updateFromEditTexts() {
        chartOptions.horizontalStep = binding.horizontalStepEditText.text.toString().toFloat()
        chartOptions.verticalStep = binding.verticalStepEditText.text.toString().toFloat()
        chartOptions.xMin = binding.xMinEditText.text.toString().toFloat()
        chartOptions.xMax = binding.xMaxEditText.text.toString().toFloat()
        chartOptions.yMin = binding.yMinEditText.text.toString().toFloat()
        chartOptions.yMax = binding.yMaxEditText.text.toString().toFloat()

    }

    private fun updateFromCheckedTextViews() {
        chartOptions.isLogScaleX = binding.logScaleXCheckedTV.isChecked
        chartOptions.isLogScaleY = binding.logScaleYCheckedTV.isChecked
        chartOptions.isHorizontalLines = binding.showHorizontalLinesCheckedTV.isChecked
        chartOptions.isVerticalLines = binding.showVerticalLinesCheckedTV.isChecked
    }
}