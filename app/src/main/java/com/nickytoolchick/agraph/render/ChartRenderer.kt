package com.nickytoolchick.agraph.render

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
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
        if (chartOptions.isHorizontalLines) drawHorizontalLines(canvas)
        if (chartOptions.isVerticalLines) drawVerticalLines(canvas)
        drawCoordinates(canvas)
        if (chartOptions.isSmooth) drawSmoothLineChart(canvas) else drawLineChart(canvas)
        drawPoints(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        val contentHeight = height - padding * 2

        if (chartOptions.isLogScaleY) {
            drawLogScaleHorizontalLines(canvas, contentHeight)
        } else {
            drawLinearScaleHorizontalLines(canvas, contentHeight)
        }
    }

    private fun drawLogScaleHorizontalLines(canvas: Canvas, contentHeight: Float) {
        val yMinLog = log10(chartOptions.yMin)
        val yMaxLog = log10(chartOptions.yMax)
        val yRangeLog = yMaxLog - yMinLog

        for (i in yMinLog.toInt()..yMaxLog.toInt()) {
            val y = 10.0.pow(i.toDouble()).toFloat()
            val scaledY = height - padding - ((log10(y) - yMinLog) * contentHeight) / yRangeLog
            canvas.drawLine(padding, scaledY, width - padding, scaledY, coordinatePaint)
        }
    }

    private fun drawLinearScaleHorizontalLines(canvas: Canvas, contentHeight: Float) {
        val yRange = chartOptions.yMax - chartOptions.yMin

        var y = chartOptions.yMin
        while (y <= chartOptions.yMax) {
            val scaledY = height - padding - (y - chartOptions.yMin) * contentHeight / yRange
            canvas.drawLine(padding, scaledY, width - padding, scaledY, coordinatePaint)
            y += chartOptions.verticalStep
        }
    }

    private fun drawVerticalLines(canvas: Canvas) {
        val contentWidth = width - padding * 2

        if (chartOptions.isLogScaleX) {
            drawLogScaleVerticalLines(canvas, contentWidth)
        } else {
            drawLinearScaleVerticalLines(canvas, contentWidth)
        }
    }

    private fun drawLogScaleVerticalLines(canvas: Canvas, contentWidth: Float) {
        val xMinLog = log10(chartOptions.xMin)
        val xMaxLog = log10(chartOptions.xMax)
        val xRangeLog = xMaxLog - xMinLog

        for (i in xMinLog.toInt()..xMaxLog.toInt()) {
            val x = 10.0.pow(i.toDouble()).toFloat()
            val scaledX = padding + ((log10(x) - xMinLog) * contentWidth) / xRangeLog
            canvas.drawLine(scaledX, padding, scaledX, height - padding, coordinatePaint)
        }
    }

    private fun drawLinearScaleVerticalLines(canvas: Canvas, contentWidth: Float) {
        val xRange = chartOptions.xMax - chartOptions.xMin

        var x = chartOptions.xMin
        while (x <= chartOptions.xMax) {
            val scaledX = padding + (x - chartOptions.xMin) * contentWidth / xRange
            canvas.drawLine(scaledX, padding, scaledX, height - padding, coordinatePaint)
            x += chartOptions.horizontalStep
        }
    }

    private fun drawCoordinates(canvas: Canvas) {
        val contentWidth = width - padding * 2
        val contentHeight = height - padding * 2

        if (chartOptions.isLogScaleX) {
            drawLogScaleXCoordinates(canvas, contentWidth)
        } else {
            drawLinearScaleXCoordinates(canvas, contentWidth)
        }

        if (chartOptions.isLogScaleY) {
            drawLogScaleYCoordinates(canvas, contentHeight)
        } else {
            drawLinearScaleYCoordinates(canvas, contentHeight)
        }
    }

    private fun drawLogScaleXCoordinates(canvas: Canvas, contentWidth: Float) {
        val xMinLog = log10(chartOptions.xMin)
        val xMaxLog = log10(chartOptions.xMax)
        val xRangeLog = xMaxLog - xMinLog

        for (i in xMinLog.toInt()..xRangeLog.toInt()) {
            val x = 10.0.pow(i.toDouble()).toFloat()
            val scaledX = padding + ((log10(x) - xMinLog) * contentWidth) / xRangeLog
            drawXCoordinateText(canvas, x.toBigDecimal().toPlainString(), scaledX)
        }
    }

    private fun drawLinearScaleXCoordinates(canvas: Canvas, contentWidth: Float) {
        val xRange = chartOptions.xMax - chartOptions.xMin

        var x = chartOptions.xMin
        while (x <= chartOptions.xMax) {
            val scaledX = padding + (x - chartOptions.xMin) * contentWidth / xRange
            drawXCoordinateText(canvas, x.toString(), scaledX)
            x += chartOptions.horizontalStep
        }
    }

    private fun drawXCoordinateText(canvas: Canvas, text: String, xPos: Float) {
        val yPos = height - 20f
        val textWidth = coordinatePaint.measureText(text)
        canvas.drawText(text, xPos - textWidth / 2, yPos, coordinatePaint)
    }

    private fun drawLogScaleYCoordinates(canvas: Canvas, contentHeight: Float) {
        val yMinLog = log10(chartOptions.yMin)
        val yMaxLog = log10(chartOptions.yMax)
        val yRangeLog = yMaxLog - yMinLog

        for (i in yMinLog.toInt()..yRangeLog.toInt()) {
            val y = 10.0.pow(i.toDouble()).toFloat()
            val scaledY = height - padding - ((log10(y) - yMinLog) * contentHeight) / yRangeLog
            drawYCoordinateText(canvas, y.toBigDecimal().toPlainString(), scaledY)
        }
    }

    private fun drawLinearScaleYCoordinates(canvas: Canvas, contentHeight: Float) {
        val yRange = chartOptions.yMax - chartOptions.yMin

        var y = chartOptions.yMin
        while (y <= chartOptions.yMax) {
            val scaledY = height - padding - (y - chartOptions.yMin) * contentHeight / yRange
            drawYCoordinateText(canvas, y.toString(), scaledY)
            y += chartOptions.verticalStep
        }
    }

    private fun drawYCoordinateText(canvas: Canvas, text: String, yPos: Float) {
        val xPos = 20f
        canvas.drawText(text, xPos, yPos, coordinatePaint)
    }

    private fun drawLineChart(canvas: Canvas) {
        setupPaintForLineChart()

        val scaleX = calculateScaleX()
        val scaleY = calculateScaleY()

        if (datasetOptions.points.isEmpty()) return

        val points = datasetOptions.points.map {
            PointF(calculateXCoordinate(it.first, scaleX), calculateYCoordinate(it.second, scaleY))
        }

        val firstPoint = points[0]
        val startX = calculateXCoordinate(firstPoint.x, scaleX)
        val startY = calculateYCoordinate(firstPoint.y, scaleY)
        path.moveTo(startX, startY)

        points.forEach { point ->
            val x = calculateXCoordinate(point.x, scaleX)
            val y = calculateYCoordinate(point.y, scaleY)
            path.lineTo(x, y)
        }

        canvas.drawPath(path, paint)
        resetPath()
    }

    private fun drawSmoothLineChart(canvas: Canvas) {
        setupPaintForLineChart()

        val scaleX = calculateScaleX()
        val scaleY = calculateScaleY()

        if (datasetOptions.points.size < 2) return

        val points = datasetOptions.points.map {
            PointF(calculateXCoordinate(it.first, scaleX), calculateYCoordinate(it.second, scaleY))
        }

        val firstPoint = points[0]
        path.moveTo(firstPoint.x, firstPoint.y)

        for (i in 1 until points.size - 2) {
            val p0 = points[i - 1]
            val p1 = points[i]
            val p2 = points[i + 1]
            val p3 = points[i + 2]

            for (t in 0..100) {
                val tScaled = t / 100f
                val x = catmullRom(p0.x, p1.x, p2.x, p3.x, tScaled)
                val y = catmullRom(p0.y, p1.y, p2.y, p3.y, tScaled)
                path.lineTo(x, y)
            }
        }

        val lastPoint = points.last()
        path.lineTo(lastPoint.x, lastPoint.y)

        canvas.drawPath(path, paint)
        resetPath()
    }

    private fun catmullRom(p0: Float, p1: Float, p2: Float, p3: Float, t: Float): Float {
        val t2 = t * t
        val t3 = t2 * t
        return 0.5f * (2 * p1 + (-p0 + p2) * t + (2 * p0 - 5 * p1 + 4 * p2 - p3) * t2 + (-p0 + 3 * p1 - 3 * p2 + p3) * t3)
    }

    private fun drawPoints(canvas: Canvas) {
        setupPaintForPoints()

        val scaleX = calculateScaleX()
        val scaleY = calculateScaleY()

        datasetOptions.points.forEach { point ->
            val x = calculateXCoordinate(point.first, scaleX)
            val y = calculateYCoordinate(point.second, scaleY)
            drawPoint(canvas, x, y)
        }
    }

    private fun drawPoint(canvas: Canvas, x: Float, y: Float) {
        canvas.drawCircle(x, y, chartOptions.pointRadius, paint)
    }

    private fun setupPaintForPoints() {
        paint.color = getColorFromColorCode()
        paint.style = Paint.Style.FILL
    }

    private fun setupPaintForLineChart() {
        paint.color = getColorFromColorCode()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = chartOptions.strokeSize
    }

    private fun calculateScaleX(): Float {
        return if (chartOptions.isLogScaleX) {
            val logXMin = log10(chartOptions.xMin)
            val logXMax = log10(chartOptions.xMax)
            (width - 2 * padding) / (logXMax - logXMin)
        } else {
            (width - 2 * padding) / (chartOptions.xMax - chartOptions.xMin)
        }
    }

    private fun calculateScaleY(): Float {
        return if (chartOptions.isLogScaleY) {
            val logYMin = log10(chartOptions.yMin)
            val logYMax = log10(chartOptions.yMax)
            (height - 2 * padding) / (logYMax - logYMin)
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
        return when (chartOptions.color) {
            1 -> Color.RED
            2 -> Color.GREEN
            3 -> Color.BLUE
            else -> Color.BLACK
        }
    }
}
