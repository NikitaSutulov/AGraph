package com.nickytoolchick.agraph.ui

import android.R
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
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
        return validateInputNotEmpty()
                && validateInputNoZerosWhenLog()
                && validateInputNoNegativeRanges()
    }

    private fun validateInputNotEmpty(): Boolean {
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
        }
        return true
    }

    private fun validateInputNoZerosWhenLog(): Boolean {
        return validateInputNoZerosWhenLogX() && validateInputNoZerosWhenLogY()
    }

    private fun validateInputNoZerosWhenLogX(): Boolean {
        val isLogScaleXEnabled = binding.logScaleXCheckedTV.isChecked
        val isXMinOrXMaxZero = binding.xMinEditText.text.toString().toFloat() == 0f
                || binding.xMaxEditText.text.toString().toFloat() == 0f
        if (isLogScaleXEnabled && isXMinOrXMaxZero) {
            Toast.makeText(
                this,
                "xMin or xMax cannot be zero when scaled logarithmically!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun validateInputNoZerosWhenLogY(): Boolean {
        val isLogScaleYEnabled = binding.logScaleYCheckedTV.isChecked
        val isXMinOrYMaxZero = binding.yMinEditText.text.toString().toFloat() == 0f
                || binding.yMaxEditText.text.toString().toFloat() == 0f
        if (isLogScaleYEnabled && isXMinOrYMaxZero) {
            Toast.makeText(
                this,
                "yMin or yMax cannot be zero when scaled logarithmically!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun validateInputNoNegativeRanges(): Boolean {
        return validateInputNoNegativeRangeX()
                && validateInputNoNegativeRangeY()
    }

    private fun validateInputNoNegativeRangeX(): Boolean {
        val xMax = binding.xMaxEditText.text.toString().toFloat()
        val xMin = binding.xMinEditText.text.toString().toFloat()
        if (xMax - xMin <= 0) {
            Toast.makeText(this, "x range cannot be negative!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateInputNoNegativeRangeY(): Boolean {
        val yMax = binding.yMaxEditText.text.toString().toFloat()
        val yMin = binding.yMinEditText.text.toString().toFloat()
        if (yMax - yMin <= 0) {
            Toast.makeText(this, "y range cannot be negative!", Toast.LENGTH_SHORT).show()
            return false
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