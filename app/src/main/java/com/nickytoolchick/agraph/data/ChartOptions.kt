package com.nickytoolchick.agraph.data

import kotlinx.serialization.Serializable

@Serializable
class ChartOptions() {
    var verticalStep: Float = 0f
    var horizontalStep: Float = 0f
    var isLogScaleX: Boolean = false
    var isLogScaleY: Boolean = false
    var isHorizontalLines: Boolean = false
    var isVerticalLines: Boolean = false
    var xMin: Float = 0f
    var xMax: Float = 0f
    var yMin: Float = 0f
    var yMax: Float = 0f
    var color: Int = 0
    var isSmooth: Boolean = false
    var strokeSize: Float = 6f
    var pointRadius: Float = 10f
}
