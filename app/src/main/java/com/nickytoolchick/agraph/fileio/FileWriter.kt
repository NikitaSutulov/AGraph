package com.nickytoolchick.agraph.fileio

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
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
        val file = File(ctx.filesDir, Constants.FILE_NAME)
        file.writeText(convertToChartFile(chartOptions, datasetOptions))
    }

    fun convertToChartFile(
        chartOptions: ChartOptions,
        datasetOptions: DatasetOptions
    ): String {
        return Json.encodeToString(chartOptions) + Constants.SPLITTER + Json.encodeToString(
            datasetOptions
        )
    }

    fun exportChartAsPng(chartRenderer: ChartRenderer): File? {
        val bitmap = createBitmapFromChart(chartRenderer)
        val galleryDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "Charts"
        )

        if (!galleryDir.exists()) {
            galleryDir.mkdirs()
        }

        val file = File(galleryDir, generateImageFileName())
        return try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            MediaScannerConnection.scanFile(
                chartRenderer.context,
                arrayOf(file.absolutePath),
                null,
                null
            )
            Toast.makeText(chartRenderer.context, "Chart exported to gallery", Toast.LENGTH_SHORT)
                .show()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(chartRenderer.context, "Export failed", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun createBitmapFromChart(chartRenderer: ChartRenderer): Bitmap {
        return Bitmap.createBitmap(
            chartRenderer.width,
            chartRenderer.height,
            Bitmap.Config.ARGB_8888
        ).apply {
            val canvas = Canvas(this)
            chartRenderer.draw(canvas)
        }
    }

    private fun generateImageFileName(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS")
        return "chart${LocalDateTime.now().format(formatter)}.png"
    }

    fun shareChart(ctx: Context, chartRenderer: ChartRenderer) {
        exportChartAsPng(chartRenderer)?.let { file ->
            val uri: Uri =
                FileProvider.getUriForFile(ctx, "com.nickytoolchick.agraph.fileprovider", file)
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/png"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            ctx.startActivity(Intent.createChooser(shareIntent, "Share chart via"))
        }
    }
}
