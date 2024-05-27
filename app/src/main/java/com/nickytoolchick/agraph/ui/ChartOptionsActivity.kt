package com.nickytoolchick.agraph.ui

import android.R
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.data.Constants
import com.nickytoolchick.agraph.databinding.ActivityChartOptionsBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChartOptionsActivity : AppCompatActivity() {

    private val colors = arrayOf("BLACK", "RED", "GREEN", "BLUE")
    private var chartOptions = ChartOptions()
    private lateinit var mainActivityIntent: Intent
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
                mainActivityIntent.putExtra(
                    Constants.NEW_CHART_OPTIONS,
                    Json.encodeToString(chartOptions)
                )
                setResult(Activity.RESULT_OK, mainActivityIntent)
                finish()
            }
        }
    }

    private fun loadChartOptions() {
        chartOptions =
            Json.decodeFromString(mainActivityIntent.getStringExtra(Constants.STABLE_CHART_OPTIONS)!!)
        loadInputs()
        setupCheckedTextViewsClickHandlers()
    }

    private fun loadInputs() {
        loadEditTexts()
        loadSpinner()
        loadCheckedTextViews()
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

    private fun setupCheckedTextViewsClickHandlers() {
        val checkedTextViews = listOf(
            binding.logScaleXCheckedTV,
            binding.logScaleYCheckedTV,
            binding.showHorizontalLinesCheckedTV,
            binding.showVerticalLinesCheckedTV,
            binding.lineSmoothCheckedTV
        )

        for (checkedTextView in checkedTextViews) {
            checkedTextView.setOnClickListener {
                checkedTextView.isChecked = !checkedTextView.isChecked
            }
        }
    }

    private fun updateChartOptions() {
        updateFromEditTexts()
        updateFromSpinner()
        updateFromCheckedTextViews()
    }

    private fun validateInput(): Boolean {
        return validateInputNotEmpty() &&
                validateInputNoZerosWhenLog() &&
                validateInputNoNegativeRanges()
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

        for (view in viewsToCheck) {
            if (view.text.toString().isEmpty()) {
                view.error = "This value must not be empty!"
                return false
            }
        }
        return true
    }

    private fun validateInputNoZerosWhenLog(): Boolean {
        return validateInputNoZerosWhenLog(
            binding.logScaleXCheckedTV.isChecked,
            binding.xMinEditText,
            binding.xMaxEditText,
            "x"
        ) &&
                validateInputNoZerosWhenLog(
                    binding.logScaleYCheckedTV.isChecked,
                    binding.yMinEditText,
                    binding.yMaxEditText,
                    "y"
                )
    }

    private fun validateInputNoZerosWhenLog(
        isLogScaleEnabled: Boolean,
        minEditText: EditText,
        maxEditText: EditText,
        axis: String
    ): Boolean {
        if (isLogScaleEnabled && (minEditText.text.toString()
                .toFloat() == 0f || maxEditText.text.toString().toFloat() == 0f)
        ) {
            Toast.makeText(
                this,
                "$axis Min or $axis Max cannot be zero when scaled logarithmically!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun validateInputNoNegativeRanges(): Boolean {
        return validateInputNoNegativeRange(binding.xMinEditText, binding.xMaxEditText, "x") &&
                validateInputNoNegativeRange(binding.yMinEditText, binding.yMaxEditText, "y")
    }

    private fun validateInputNoNegativeRange(
        minEditText: EditText,
        maxEditText: EditText,
        axis: String
    ): Boolean {
        val min = minEditText.text.toString().toFloat()
        val max = maxEditText.text.toString().toFloat()
        if (max - min <= 0) {
            Toast.makeText(this, "$axis range cannot be negative!", Toast.LENGTH_SHORT).show()
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
