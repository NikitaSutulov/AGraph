package com.nickytoolchick.agraph

import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.data.Constants
import com.nickytoolchick.agraph.data.DatasetOptions
import com.nickytoolchick.agraph.fileio.FileWriter
import org.junit.Test
import org.junit.Assert.*

class FileConversionUnitTest {
    @Test
    fun checkDefaultChart() {
        val defaultChartOptions = ChartOptions()
        val defaultDatasetOptions = DatasetOptions()
        val fileWriter = FileWriter()
        val expectedConvertedFileString = """{}${Constants.SPLITTER}{"points":[]}"""
        assertEquals(expectedConvertedFileString, fileWriter.convertToChartFile(defaultChartOptions, defaultDatasetOptions))
    }

    @Test
    fun checkChartWithChangedParameters() {
        val changedChartOptions = ChartOptions().apply { verticalStep = 7f }
        val changedDatasetOptions = DatasetOptions().apply { isSmooth = true }
        val fileWriter = FileWriter()
        val expectedConvertedFileString = """{"verticalStep":7.0}${Constants.SPLITTER}{"points":[],"isSmooth":true}"""
        assertEquals(expectedConvertedFileString, fileWriter.convertToChartFile(changedChartOptions, changedDatasetOptions))
    }

    @Test
    fun checkChartWithBooleanParameters() {
        val changedChartOptions = ChartOptions().apply {
            isLogScaleX = true
            isLogScaleY = true
            isHorizontalLines = true
            isVerticalLines = true
        }
        val changedDatasetOptions = DatasetOptions().apply { isSmooth = true }
        val fileWriter = FileWriter()
        val expectedConvertedFileString = """{"isLogScaleX":true,"isLogScaleY":true,"isHorizontalLines":true,"isVerticalLines":true}${Constants.SPLITTER}{"points":[],"isSmooth":true}"""
        assertEquals(expectedConvertedFileString, fileWriter.convertToChartFile(changedChartOptions, changedDatasetOptions))
    }

    @Test
    fun checkChartWithPoints() {
        val changedChartOptions = ChartOptions()
        val changedDatasetOptions = DatasetOptions().apply { points = arrayOf(Pair(2.7f, 3.9f)) }
        val fileWriter = FileWriter()
        val expectedConvertedFileString = """{}${Constants.SPLITTER}{"points":[{"first":2.7,"second":3.9}]}"""
        assertEquals(expectedConvertedFileString, fileWriter.convertToChartFile(changedChartOptions, changedDatasetOptions))
    }
}