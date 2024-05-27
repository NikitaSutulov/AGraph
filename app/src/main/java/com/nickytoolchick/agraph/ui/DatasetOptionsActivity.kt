package com.nickytoolchick.agraph.ui

import android.app.Activity
import android.app.AlertDialog
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

    private lateinit var datasetOptions: DatasetOptions
    private lateinit var mainActivityIntent: Intent
    private lateinit var binding: ActivityDatasetOptionsBinding
    private val points: MutableList<Pair<Float, Float>> = mutableListOf()

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
                mainActivityIntent.putExtra(
                    Constants.NEW_DATASET_OPTIONS,
                    Json.encodeToString(datasetOptions)
                )
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

        binding.deletePointButton.setOnLongClickListener {
            showDeleteAllPointsDialog()
            true
        }
    }

    private fun updateDatasetOptions() {
        datasetOptions.points = points.toTypedArray()
    }

    private fun loadDatasetOptions() {
        datasetOptions =
            Json.decodeFromString(mainActivityIntent.getStringExtra(Constants.STABLE_DATASET_OPTIONS)!!)
        loadPoints()
    }

    private fun loadPoints() {
        points.addAll(datasetOptions.points)
        updatePointsTextView()
    }

    private fun addPoint() {
        val x = binding.newPointXEditText.text.toString().toFloat()
        val y = binding.newPointYEditText.text.toString().toFloat()
        val newPoint = Pair(x, y)
        if (newPoint !in points) {
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
        if (pointToDelete in points) {
            points.remove(pointToDelete)
            updatePointsTextView()
        } else {
            showToast(this, "This point does not exist!")
        }
    }

    private fun updatePointsTextView() {
        val finalText = points.joinToString("\n") { "${it.first} ${it.second}" }
        binding.pointsTV.text = finalText
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun validateInput(): Boolean {
        return if (binding.pointsTV.text.toString().isNullOrEmpty()) {
            binding.pointsTV.error = "You must put in some points!"
            false
        } else {
            true
        }
    }

    private fun validateNewPoint(): Boolean {
        return !(binding.newPointXEditText.text.toString().isNullOrEmpty()
                || binding.newPointYEditText.text.toString().isNullOrEmpty())
    }

    private fun showDeleteAllPointsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete all points")
            .setMessage("Do you really want to delete all points?")
            .setPositiveButton("Yes") { dialog, _ ->
                points.clear()
                updatePointsTextView()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
