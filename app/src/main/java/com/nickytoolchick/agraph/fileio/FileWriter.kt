package com.nickytoolchick.agraph.fileio

import android.content.Context
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.data.Constants
import com.nickytoolchick.agraph.data.DatasetOptions
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class FileWriter {
    fun saveFile(ctx: Context, chartOptions: ChartOptions, datasetOptions: DatasetOptions) {
        val path = ctx.filesDir
        val file = File(path, Constants.FILE_NAME)
        file.delete()
        file.appendText(
            convertToChartFile(chartOptions, datasetOptions)
        )
    }

    fun convertToChartFile(chartOptions: ChartOptions, datasetOptions: DatasetOptions): String {
        return Json.encodeToString(chartOptions) + Constants.SPLITTER + Json.encodeToString(datasetOptions)
    }
}