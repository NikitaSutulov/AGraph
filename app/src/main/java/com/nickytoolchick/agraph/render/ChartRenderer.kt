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

        canvas.drawColor(Color.WHITE)
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

    private fun drawHorizontalLines(canvas: Canvas) {
        val startY = chartOptions.yMin
        val endY = chartOptions.yMax
        val contentHeight = height - padding * 2
        val yRange = chartOptions.yMax - chartOptions.yMin

        var y = startY
        while (y <= endY) {
            val scaledY = (height - padding) - (y - chartOptions.yMin) * contentHeight / yRange
            canvas.drawLine(padding, scaledY, width.toFloat() - padding, scaledY, coordinatePaint)
            y += chartOptions.verticalStep
        }
    }

    private fun drawVerticalLines(canvas: Canvas) {
        val startX = chartOptions.xMin
        val endX = chartOptions.xMax
        val contentWidth = width - padding * 2
        val xRange = chartOptions.xMax - chartOptions.xMin

        var x = startX
        while (x <= endX) {
            val scaledX = padding + (x - chartOptions.xMin) * contentWidth / xRange
            canvas.drawLine(scaledX, padding, scaledX, height.toFloat() - padding, coordinatePaint)
            x += chartOptions.horizontalStep
        }
    }

    private fun drawCoordinates(canvas: Canvas) {
        val contentWidth = width - padding * 2
        val contentHeight = height - padding * 2
        val xRange = chartOptions.xMax - chartOptions.xMin
        val yRange = chartOptions.yMax - chartOptions.yMin

        // Draw x-axis coordinate values
        val xStep = chartOptions.horizontalStep
        var x = chartOptions.xMin
        while (x <= chartOptions.xMax) {
            val scaledX = padding + (x - chartOptions.xMin) * contentWidth / xRange
            val yPos = height - 20f
            val text = x.toString()
            val textWidth = coordinatePaint.measureText(text)
            canvas.drawText(text, scaledX - textWidth / 2, yPos, coordinatePaint)
            x += xStep
        }

        // Draw y-axis coordinate values
        val yStep = chartOptions.verticalStep
        var y = chartOptions.yMin
        while (y <= chartOptions.yMax) {
            val scaledY = (height - padding) - (y - chartOptions.yMin) * contentHeight / yRange
            val xPos = 20f
            val text = y.toString()
            canvas.drawText(text, xPos, scaledY, coordinatePaint)
            y += yStep
        }
    }

    private fun drawLineChart(canvas: Canvas) {
        val dataset = datasetOptions.points

        // Set paint properties for drawing the line chart
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = datasetOptions.strokeSize

        // Calculate the scaling factors for x and y coordinates
        val scaleX = (width - 2 * padding) / (chartOptions.xMax - chartOptions.xMin)
        val scaleY = (height - 2 * padding) / (chartOptions.yMax - chartOptions.yMin)

        // Move to the first data point
        val firstPoint = dataset[0]
        val startX = padding + (firstPoint.first - chartOptions.xMin) * scaleX
        val startY = height - padding - (firstPoint.second - chartOptions.yMin) * scaleY
        drawPoint(startX, startY, canvas)
        path.moveTo(startX, startY)

        // Draw the line chart
        for (i in 1 until dataset.size) {
            val dataPoint = dataset[i]
            val x = padding + (dataPoint.first - chartOptions.xMin) * scaleX
            val y = height - padding - (dataPoint.second - chartOptions.yMin) * scaleY
            drawPoint(x, y, canvas)
            path.lineTo(x, y)
            Log.d("chart", "drawing point $x;$y")
        }

        // Draw the path representing the line chart
        canvas.drawPath(path, paint)

        // Reset the path for future use
        path.reset()
        Log.d("chart", "drawing the chart")
        invalidate()
    }

    private fun drawSmoothLineChart(canvas: Canvas) {
        val dataset = datasetOptions.points

        // Set paint properties for drawing the line chart
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = datasetOptions.strokeSize

        // Calculate the scaling factors for x and y coordinates
        val scaleX = (width - 2 * padding) / (chartOptions.xMax - chartOptions.xMin)
        val scaleY = (height - 2 * padding) / (chartOptions.yMax - chartOptions.yMin)

        // Move to the first data point
        val firstPoint = dataset[0]
        val startX = padding + (firstPoint.first - chartOptions.xMin) * scaleX
        val startY = height - padding - (firstPoint.second - chartOptions.yMin) * scaleY
        drawPoint(startX, startY, canvas)
        path.moveTo(startX, startY)

        // Draw the line chart
        for (i in 1 until dataset.size) {
            val currentPoint = dataset[i]
            val previousPoint = dataset[i - 1]

            // Calculate the control points for the cubic Bezier curve
            val prevX = padding + (previousPoint.first - chartOptions.xMin) * scaleX
            val prevY = height - padding - (previousPoint.second - chartOptions.yMin) * scaleY
            val currX = padding + (currentPoint.first - chartOptions.xMin) * scaleX
            val currY = height - padding - (currentPoint.second - chartOptions.yMin) * scaleY
            val controlX1 = prevX + (currX - prevX) / 2
            val controlX2 = prevX + (currX - prevX) / 2

            path.cubicTo(controlX1, prevY, controlX2, currY, currX, currY)

            drawPoint(currX, currY, canvas)
            Log.d("chart", "drawing point $currX;$currY")
        }

        // Draw the path representing the line chart
        canvas.drawPath(path, paint)

        // Reset the path for future use
        path.reset()
        Log.d("chart", "drawing the chart")
    }

    private fun drawPoint(x: Float, y: Float, canvas: Canvas) {
        paint.style = Paint.Style.FILL
        canvas.drawCircle(x, y, datasetOptions.pointRadius, paint)
        paint.style = Paint.Style.STROKE
    }


    fun setChartOptions(chartOptions: ChartOptions) {
        this.chartOptions = chartOptions
        Log.d("chart", "setting chart options")
    }

    fun setDatasetOptions(datasetOptions: DatasetOptions) {
        this.datasetOptions = datasetOptions
        Log.d("chart", "setting dataset options")
    }
}