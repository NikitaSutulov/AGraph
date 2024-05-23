package com.nickytoolchick.agraph.ui

import android.R
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.view.children
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.data.Constants
import com.nickytoolchick.agraph.databinding.ActivityChartOptionsBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class ChartOptionsActivity : AppCompatActivity() {

    var colors = arrayOf("BLACK", "RED", "GREEN", "BLUE")
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
            if (validateInput()) {
                updateChartOptions()
                mainActivityIntent.putExtra(Constants.NEW_CHART_OPTIONS, Json.encodeToString(chartOptions))
                setResult(Activity.RESULT_OK, mainActivityIntent)
                finish()
            }
        }
    }

    private fun loadChartOptions() {
        chartOptions = Json.decodeFromString(mainActivityIntent.getStringExtra(Constants.STABLE_CHART_OPTIONS)!!)
        loadEditTexts()
        loadSpinner()
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
        binding.strokeSizeEditText.setText(chartOptions.strokeSize.toString())
        binding.pointRadiusEditText.setText(chartOptions.pointRadius.toString())
    }

    private fun loadSpinner() {
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, colors)
        binding.colorSpinner.adapter = arrayAdapter
        binding.colorSpinner.setSelection(chartOptions.color)
    }

    private fun loadCheckedTextViews() {
        binding.logScaleXCheckedTV.isChecked = chartOptions.isLogScaleX
        binding.logScaleYCheckedTV.isChecked = chartOptions.isLogScaleY
        binding.showHorizontalLinesCheckedTV.isChecked = chartOptions.isHorizontalLines
        binding.showVerticalLinesCheckedTV.isChecked = chartOptions.isVerticalLines
        binding.lineSmoothCheckedTV.isChecked = chartOptions.isSmooth
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
        binding.lineSmoothCheckedTV.setOnClickListener {
            binding.lineSmoothCheckedTV.isChecked = !binding.lineSmoothCheckedTV.isChecked
        }
    }

    private fun updateChartOptions() {
        updateFromEditTexts()
        updateFromSpinner()
        updateFromCheckedTextViews()
    }

    private fun validateInput(): Boolean {
        val viewsToCheck = listOf(
            binding.horizontalStepEditText,
            binding.verticalStepEditText,
            binding.xMinEditText,
            binding.xMaxEditText,
            binding.yMinEditText,
            binding.yMaxEditText,
            binding.strokeSizeEditText,
            binding.pointRadiusEditText
        )

        for (i in viewsToCheck.indices) {
            if (viewsToCheck[i].text.toString().isNullOrEmpty()) {
                viewsToCheck[i].error = "This value must not be empty!"
                return false
            }
            if (viewsToCheck[i].text.toString().toFloat() == 0f) {
                viewsToCheck[i].error = "This value must not be zero!"
                return false
            }
        }
        return true
    }

    private fun updateFromEditTexts() {
        chartOptions.horizontalStep = binding.horizontalStepEditText.text.toString().toFloat()
        chartOptions.verticalStep = binding.verticalStepEditText.text.toString().toFloat()
        chartOptions.xMin = binding.xMinEditText.text.toString().toFloat()
        chartOptions.xMax = binding.xMaxEditText.text.toString().toFloat()
        chartOptions.yMin = binding.yMinEditText.text.toString().toFloat()
        chartOptions.yMax = binding.yMaxEditText.text.toString().toFloat()
        chartOptions.strokeSize = binding.strokeSizeEditText.text.toString().toFloat()
        chartOptions.pointRadius = binding.pointRadiusEditText.text.toString().toFloat()
    }

    private fun updateFromSpinner() {
        chartOptions.color = binding.colorSpinner.selectedItemPosition
    }

    private fun updateFromCheckedTextViews() {
        chartOptions.isLogScaleX = binding.logScaleXCheckedTV.isChecked
        chartOptions.isLogScaleY = binding.logScaleYCheckedTV.isChecked
        chartOptions.isHorizontalLines = binding.showHorizontalLinesCheckedTV.isChecked
        chartOptions.isVerticalLines = binding.showVerticalLinesCheckedTV.isChecked
        chartOptions.isSmooth = binding.lineSmoothCheckedTV.isChecked
    }
}