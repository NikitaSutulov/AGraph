package com.nickytoolchick.agraph

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nickytoolchick.agraph.data.DatasetOptions
import com.nickytoolchick.agraph.databinding.ActivityDatasetOptionsBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class DatasetOptionsActivity : AppCompatActivity() {

    private var datasetOptions = DatasetOptions()
    lateinit var mainActivityIntent: Intent
    private lateinit var binding: ActivityDatasetOptionsBinding
    var points: MutableList<Pair<Float, Float>> = mutableListOf()

    var colors = arrayOf(0, 1, 2, 3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDatasetOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainActivityIntent = intent
        loadDatasetOptions()

        binding.submitDatasetOptionsButton.setOnClickListener {
            updateDatasetOptions()
            mainActivityIntent.putExtra("newDatasetOptions", Json.encodeToString(datasetOptions))
            setResult(Activity.RESULT_OK, mainActivityIntent)
            finish()
        }

        binding.addPointButton.setOnClickListener {
            addPoint()
        }

        binding.deletePointButton.setOnClickListener {
            deletePoint()
        }
    }

    private fun updateDatasetOptions() {
        updateFromEditTexts()
        updateFromSpinner()
        updateFromCheckedTV()
        updateFromPointsList()
    }

    private fun updateFromEditTexts() {
        datasetOptions.strokeSize = binding.strokeSizeEditText.text.toString().toFloat()
        datasetOptions.pointRadius = binding.pointRadiusEditText.text.toString().toFloat()
    }

    private fun updateFromSpinner() {
        datasetOptions.color = binding.colorSpinner.selectedItem.toString().toInt()
    }

    private fun updateFromCheckedTV() {
        datasetOptions.isSmooth = binding.lineSmoothCheckedTV.isChecked
    }

    private fun updateFromPointsList() {
        datasetOptions.points = points.toTypedArray()
    }

    private fun loadDatasetOptions() {
        datasetOptions = Json.decodeFromString(mainActivityIntent.getStringExtra("stableDatasetOptions")!!)
        loadSpinner()
        loadEditTexts()
        loadSmoothnessCheckedTV()
        loadPoints()
        handleSmoothnessCheckedTVClick()
        updatePointsTextView()
    }

    private fun handleSmoothnessCheckedTVClick() {
        binding.lineSmoothCheckedTV.setOnClickListener {
            binding.lineSmoothCheckedTV.isChecked = !binding.lineSmoothCheckedTV.isChecked
        }
    }

    private fun loadSpinner() {
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, colors)
        binding.colorSpinner.adapter = arrayAdapter
        binding.colorSpinner.setSelection(datasetOptions.color)
    }

    private fun loadEditTexts() {
        binding.strokeSizeEditText.setText(datasetOptions.strokeSize.toString())
        binding.pointRadiusEditText.setText(datasetOptions.pointRadius.toString())
    }

    private fun loadSmoothnessCheckedTV() {
        binding.lineSmoothCheckedTV.isChecked = datasetOptions.isSmooth
    }

    private fun loadPoints() {
        points = datasetOptions.points.toMutableList()
        updatePointsTextView()
    }

    private fun addPoint() {
        val x = binding.newPointXEditText.text.toString().toFloat()
        val y = binding.newPointYEditText.text.toString().toFloat()
        val newPoint = Pair(x, y)
        if (!(points.filter { it == newPoint }.any())) {
            points.add(newPoint)
            updatePointsTextView()
        } else {
            val toast = Toast.makeText(this, "This point already exists!", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    private fun deletePoint() {
        val x = binding.newPointXEditText.text.toString().toFloat()
        val y = binding.newPointYEditText.text.toString().toFloat()
        val pointToDelete = Pair(x, y)
        if (points.filter { it == pointToDelete }.any()) {
            points.remove(pointToDelete)
            updatePointsTextView()
        } else {
            val toast = Toast.makeText(this, "This point does not exist!", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    private fun updatePointsTextView() {
        var finalText = ""
        for (i in points.indices) {
            finalText += "${points[i].first} ${points[i].second}\n"
        }
        binding.pointsTV.text = finalText
    }
}