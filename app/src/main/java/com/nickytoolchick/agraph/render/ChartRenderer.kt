package com.nickytoolchick.agraph.render

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.nickytoolchick.agraph.data.ChartOptions
import com.nickytoolchick.agraph.data.DatasetOptions
import kotlin.math.log10
import kotlin.math.pow

class ChartRenderer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private var chartOptions: ChartOptions = ChartOptions(),
    private var datasetOptions: DatasetOptions = DatasetOptions()
) : View(context, attrs, defStyleAttr) {

    private val path: Path = Path()
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val padding = 100f
    private val coordinatePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
        textSize = 28f
        strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        if (chartOptions.isHorizontalLines) {
            drawHorizontalLines(canvas)
        }
        if (chartOptions.isVerticalLines) {
            drawVerticalLines(canvas)
        }
        drawCoordinates(canvas)
        if (datasetOptions.isSmooth) {
            drawSmoothLineChart(canvas)
        } else {
            drawLineChart(canvas)
        }
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        val startY = chartOptions.yMin
        val endY = chartOptions.yMax
        val contentHeight = height - padding * 2

        if (chartOptions.isLogScaleY) {
            val yMinLog = log10(chartOptions.yMin)
            val yMaxLog = log10(chartOptions.yMax)
            val yRangeLog = yMaxLog - yMinLog

            var i = yMinLog.toInt()
            while (i <= yMaxLog.toInt()) {
                val y = 10.0.pow(i.toDouble()).toFloat()
                val scaledY = height - padding - ((log10(y) - yMinLog) * contentHeight) / yRangeLog
                canvas.drawLine(padding, scaledY, width.toFloat() - padding, scaledY, coordinatePaint)
                i++
            }
        } else {
            val yRange = chartOptions.yMax - chartOptions.yMin

            var y = startY
            while (y <= endY) {
                val scaledY = height - padding - (y - chartOptions.yMin) * contentHeight / yRange
                canvas.drawLine(padding, scaledY, width.toFloat() - padding, scaledY, coordinatePaint)
                y += chartOptions.verticalStep
            }
        }
    }

    private fun drawVerticalLines(canvas: Canvas) {
        val startX = chartOptions.xMin
        val endX = chartOptions.xMax
        val contentWidth = width - padding * 2

        if (chartOptions.isLogScaleX) {
            val xMinLog = log10(chartOptions.xMin)
            val xMaxLog = log10(chartOptions.xMax)
            val xRangeLog = xMaxLog - xMinLog

            var i = xMinLog.toInt()
            while (i <= xMaxLog.toInt()) {
                val x = 10.0.pow(i.toDouble()).toFloat()
                val scaledX = padding + ((log10(x) - xMinLog) * contentWidth) / xRangeLog
                canvas.drawLine(scaledX, padding, scaledX, height.toFloat() - padding, coordinatePaint)
                i++
            }
        } else {
            val xRange = chartOptions.xMax - chartOptions.xMin

            var x = startX
            while (x <= endX) {
                val scaledX = padding + (x - chartOptions.xMin) * contentWidth / xRange
                canvas.drawLine(scaledX, padding, scaledX, height.toFloat() - padding, coordinatePaint)
                x += chartOptions.horizontalStep
            }
        }
    }

    private fun drawCoordinates(canvas: Canvas) {
        val contentWidth = width - padding * 2
        val contentHeight = height - padding * 2

        if (chartOptions.isLogScaleX) {
            val xMinLog = log10(chartOptions.xMin)
            val xMaxLog = log10(chartOptions.xMax)
            val xRangeLog = xMaxLog - xMinLog

            drawXCoordinatesLog(canvas, contentWidth, xRangeLog, xMinLog)
        } else {
            val xRange = chartOptions.xMax - chartOptions.xMin

            drawXCoordinates(canvas, contentWidth, xRange)
        }

        if (chartOptions.isLogScaleY) {
            val yMinLog = log10(chartOptions.yMin)
            val yMaxLog = log10(chartOptions.yMax)
            val yRangeLog = yMaxLog - yMinLog

            drawYCoordinatesLog(canvas, contentHeight, yRangeLog, yMinLog)
        } else {
            val yRange = chartOptions.yMax - chartOptions.yMin

            drawYCoordinates(canvas, contentHeight, yRange)
        }
    }

    private fun drawXCoordinatesLog(canvas: Canvas, contentWidth: Float, xRangeLog: Float, xMinLog: Float) {
        val xStepLog = chartOptions.horizontalStep

        var i = xMinLog.toInt()
        while (i <= xRangeLog.toInt()) {
            val x = 10.0.pow(i.toDouble()).toFloat()
            val scaledX = padding + ((log10(x) - xMinLog) * contentWidth) / xRangeLog
            val yPos = height - 20f
            val text = x.toBigDecimal().toPlainString() // Convert to BigDecimal to avoid scientific notation
            val textWidth = coordinatePaint.measureText(text)
            canvas.drawText(text, scaledX - textWidth / 2, yPos, coordinatePaint)
            i++
        }
    }

    private fun drawYCoordinatesLog(canvas: Canvas, contentHeight: Float, yRangeLog: Float, yMinLog: Float) {
        val yStepLog = chartOptions.verticalStep

        var i = yMinLog.toInt()
        while (i <= yRangeLog.toInt()) {
            val y = 10.0.pow(i.toDouble()).toFloat()
            val scaledY = height - padding - ((log10(y) - yMinLog) * contentHeight) / yRangeLog
            val xPos = 20f
            val text = y.toBigDecimal().toPlainString() // Convert to BigDecimal to avoid scientific notation
            canvas.drawText(text, xPos, scaledY, coordinatePaint)
            i++
        }
    }

    private fun drawXCoordinates(canvas: Canvas, contentWidth: Float, xRange: Float) {
        val xStep = chartOptions.horizontalStep
        var x = chartOptions.xMin
        while (x <= chartOptions.xMax) {
            val scaledX = if (chartOptions.isLogScaleX) {
                padding + ((log10(x) - log10(chartOptions.xMin)) * contentWidth) /
                        (log10(chartOptions.xMax) - log10(chartOptions.xMin))
            } else {
                padding + (x - chartOptions.xMin) * contentWidth / xRange
            }
            val yPos = height - 20f
            val text = x.toString()
            val textWidth = coordinatePaint.measureText(text)
            canvas.drawText(text, scaledX - textWidth / 2, yPos, coordinatePaint)
            x += xStep
        }
    }

    private fun drawYCoordinates(canvas: Canvas, contentHeight: Float, yRange: Float) {
        val yStep = chartOptions.verticalStep
        var y = chartOptions.yMin
        while (y <= chartOptions.yMax) {
            val scaledY = if (chartOptions.isLogScaleY) {
                height - padding - ((log10(y) - log10(chartOptions.yMin)) * contentHeight) /
                        (log10(chartOptions.yMax) - log10(chartOptions.yMin))
            } else {
                height - padding - (y - chartOptions.yMin) * contentHeight / yRange
            }
            val xPos = 20f
            val text = y.toString()
            canvas.drawText(text, xPos, scaledY, coordinatePaint)
            y += yStep
        }
    }

    private fun drawLineChart(canvas: Canvas) {
        val dataset = datasetOptions.points
        setupPaintForLineChart()

        val scaleX = calculateScaleX()
        val scaleY = calculateScaleY()

        val firstPoint = dataset[0]
        val startX = calculateXCoordinate(firstPoint.first, scaleX)
        val startY = calculateYCoordinate(firstPoint.second, scaleY)
        drawPoint(startX, startY, canvas)
        path.moveTo(startX, startY)

        for (i in 1 until dataset.size) {
            val dataPoint = dataset[i]
            val x = calculateXCoordinate(dataPoint.first, scaleX)
            val y = calculateYCoordinate(dataPoint.second, scaleY)
            drawPoint(x, y, canvas)
            path.lineTo(x, y)
            Log.d("chart", "drawing point $x;$y")
        }

        canvas.drawPath(path, paint)
        resetPath()
        Log.d("chart", "drawing the chart")
        invalidate()
    }

    private fun drawSmoothLineChart(canvas: Canvas) {
        val dataset = datasetOptions.points
        setupPaintForLineChart()

        val scaleX = calculateScaleX()
        val scaleY = calculateScaleY()

        val firstPoint = dataset[0]
        val startX = calculateXCoordinate(firstPoint.first, scaleX)
        val startY = calculateYCoordinate(firstPoint.second, scaleY)
        drawPoint(startX, startY, canvas)
        path.moveTo(startX, startY)

        for (i in 1 until dataset.size) {
            val currentPoint = dataset[i]
            val previousPoint = dataset[i - 1]

            val prevX = calculateXCoordinate(previousPoint.first, scaleX)
            val prevY = calculateYCoordinate(previousPoint.second, scaleY)
            val currX = calculateXCoordinate(currentPoint.first, scaleX)
            val currY = calculateYCoordinate(currentPoint.second, scaleY)
            val controlX1 = prevX + (currX - prevX) / 2
            val controlX2 = prevX + (currX - prevX) / 2

            path.cubicTo(controlX1, prevY, controlX2, currY, currX, currY)

            drawPoint(currX, currY, canvas)
            Log.d("chart", "drawing point $currX;$currY")
        }

        canvas.drawPath(path, paint)
        resetPath()
        Log.d("chart", "drawing the chart")
        invalidate()
    }

    private fun setupPaintForLineChart() {
        paint.color = getColorFromColorCode()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = datasetOptions.strokeSize
    }

    private fun calculateScaleX(): Float {
        return if (chartOptions.isLogScaleX) {
            val logXMin = log10(chartOptions.xMin)
            val logXMax = log10(chartOptions.xMax)
            (width - 2 * padding) / (logXMax - logXMin).toFloat()
        } else {
            (width - 2 * padding) / (chartOptions.xMax - chartOptions.xMin)
        }
    }

    private fun calculateScaleY(): Float {
        return if (chartOptions.isLogScaleY) {
            val logYMin = log10(chartOptions.yMin)
            val logYMax = log10(chartOptions.yMax)
            (height - 2 * padding) / (logYMax - logYMin).toFloat()
        } else {
            (height - 2 * padding) / (chartOptions.yMax - chartOptions.yMin)
        }
    }

    private fun calculateXCoordinate(x: Float, scaleX: Float): Float {
        return if (chartOptions.isLogScaleX) {
            val logX = log10(x)
            padding + (logX - log10(chartOptions.xMin)) * scaleX
        } else {
            padding + (x - chartOptions.xMin) * scaleX
        }
    }

    private fun calculateYCoordinate(y: Float, scaleY: Float): Float {
        return if (chartOptions.isLogScaleY) {
            val logY = log10(y)
            height - padding - (logY - log10(chartOptions.yMin)) * scaleY
        } else {
            height - padding - (y - chartOptions.yMin) * scaleY
        }
    }

    private fun drawPoint(x: Float, y: Float, canvas: Canvas) {
        paint.style = Paint.Style.FILL
        canvas.drawCircle(x, y, datasetOptions.pointRadius, paint)
        paint.style = Paint.Style.STROKE
    }

    private fun resetPath() {
        path.reset()
    }

    fun setChartOptions(chartOptions: ChartOptions) {
        this.chartOptions = chartOptions
        Log.d("chart", "setting chart options")
    }

    fun setDatasetOptions(datasetOptions: DatasetOptions) {
        this.datasetOptions = datasetOptions
        Log.d("chart", "setting dataset options")
    }

    private fun getColorFromColorCode(): Int {
        return when (datasetOptions.color) {
            1 -> Color.RED
            2 -> Color.GREEN
            3 -> Color.BLUE
            else -> Color.BLACK
        }
    }
}
