package com.nickytoolchick.agraph.fileio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.os.Environment
import android.widget.Toast
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.data.Constants
import com.nickytoolchick.agraph.data.DatasetOptions
import com.nickytoolchick.agraph.render.ChartRenderer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    fun exportChartAsPng(chartRenderer: ChartRenderer) {
        val bitmap = Bitmap.createBitmap(chartRenderer.width, chartRenderer.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        chartRenderer.draw(canvas)

        val galleryDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(galleryDir, generateImageFileName())
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            MediaScannerConnection.scanFile(
                chartRenderer.context,
                arrayOf(file.absolutePath),
                null,
                null
            )
            Toast.makeText(chartRenderer.context, "Chart exported to gallery", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(chartRenderer.context, "Export failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateImageFileName(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS")
        return "chart${LocalDateTime.now().format(formatter)}.png"
    }
}