package com.nickytoolchick.agraph.fileio

import android.content.Context
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.data.Constants
import com.nickytoolchick.agraph.data.DatasetOptions
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class FileReader {
    fun readFile(ctx: Context): Pair<ChartOptions, DatasetOptions>? {
        val file = File(ctx.filesDir, Constants.FILE_NAME)

        if (!file.exists()) {
            return null
        }

        try {
            val lines = file.readLines()

            if (lines.size != 3) {
                return null
            }

            val chartOptions = Json.decodeFromString<ChartOptions>(lines[0])
            val datasetOptions = Json.decodeFromString<DatasetOptions>(lines[2])

            return Pair(chartOptions, datasetOptions)
        } catch (e: Exception) {
            return null
        }
    }
}