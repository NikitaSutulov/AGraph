package com.nickytoolchick.agraph.fileio

import android.content.Context
import android.util.Log
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.data.Constants
import com.nickytoolchick.agraph.data.DatasetOptions
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

class FileReader {
    companion object {
        private const val EXPECTED_LINES = 3
        private const val TAG = "FileReader"
    }

    fun readFile(ctx: Context): Pair<ChartOptions, DatasetOptions> {
        val file = File(ctx.filesDir, Constants.FILE_NAME)

        if (!file.exists()) {
            return createDefaultOptions()
        }

        return try {
            val lines = file.readLines()

            if (lines.size != EXPECTED_LINES) {
                createDefaultOptions()
            } else {
                val chartOptions = Json.decodeFromString<ChartOptions>(lines[0])
                val datasetOptions = Json.decodeFromString<DatasetOptions>(lines[2])
                Pair(chartOptions, datasetOptions)
            }
        } catch (e: IOException) {
            Log.e(TAG, "File read error", e)
            createDefaultOptions()
        } catch (e: Exception) {
            Log.e(TAG, "Deserialization error", e)
            createDefaultOptions()
        }
    }

    private fun createDefaultOptions(): Pair<ChartOptions, DatasetOptions> {
        return Pair(ChartOptions(), DatasetOptions())
    }
}
