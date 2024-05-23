package com.nickytoolchick.agraph.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nickytoolchick.agraph.data.DatasetOptions
import com.nickytoolchick.agraph.data.Constants
import com.nickytoolchick.agraph.databinding.ActivityDatasetOptionsBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class DatasetOptionsActivity : AppCompatActivity() {

    private var datasetOptions = DatasetOptions()
    lateinit var mainActivityIntent: Intent
    private lateinit var binding: ActivityDatasetOptionsBinding
    var points: MutableList<Pair<Float, Float>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDatasetOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainActivityIntent = intent
        loadDatasetOptions()

        binding.submitDatasetOptionsButton.setOnClickListener {
            if (validateInput()) {
                updateDatasetOptions()
                datasetOptions.points.sortBy { it.first }
                mainActivityIntent.putExtra(Constants.NEW_DATASET_OPTIONS, Json.encodeToString(datasetOptions))
                setResult(Activity.RESULT_OK, mainActivityIntent)
                finish()
            }
        }

        binding.addPointButton.setOnClickListener {
            if (validateNewPoint()) {
                addPoint()
            }
        }

        binding.deletePointButton.setOnClickListener {
            if (validateNewPoint()) {
                deletePoint()
            }
        }
    }

    private fun updateDatasetOptions() {
        datasetOptions.points = points.toTypedArray()
    }

    private fun loadDatasetOptions() {
        datasetOptions = Json.decodeFromString(mainActivityIntent.getStringExtra(Constants.STABLE_DATASET_OPTIONS)!!)
        loadPoints()
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
            showToast(this, "This point already exists!")
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
            showToast(this, "This point does not exist!")
        }
    }

    private fun updatePointsTextView() {
        var finalText = ""
        for (i in points.indices) {
            finalText += "${points[i].first} ${points[i].second}\n"
        }
        binding.pointsTV.text = finalText
    }

    private fun showToast(context: Context, message: String){
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.show()
    }

    private fun validateInput(): Boolean {
        if (binding.pointsTV.text.toString().isNullOrEmpty()) {
            binding.pointsTV.error = "You must put in some points!"
            return false
        }
        return true
    }

    private fun validateNewPoint(): Boolean {
        if (binding.newPointXEditText.text.toString().isNullOrEmpty()
            || binding.newPointYEditText.text.toString().isNullOrEmpty()) {
            return false
        }
        return true
    }
}