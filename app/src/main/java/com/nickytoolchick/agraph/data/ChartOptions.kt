package com.nickytoolchick.agraph.data

import com.nickytoolchick.agraph.data.Constants.DEFAULT_COLOR
import com.nickytoolchick.agraph.data.Constants.DEFAULT_MAX_COORDINATE
import com.nickytoolchick.agraph.data.Constants.DEFAULT_MIN_COORDINATE
import com.nickytoolchick.agraph.data.Constants.DEFAULT_POINT_RADIUS
import com.nickytoolchick.agraph.data.Constants.DEFAULT_STEP
import com.nickytoolchick.agraph.data.Constants.DEFAULT_STROKE_SIZE
import kotlinx.serialization.Serializable

@Serializable
class ChartOptions {
    var verticalStep: Float = DEFAULT_STEP
    var horizontalStep: Float = DEFAULT_STEP
    var isLogScaleX: Boolean = false
    var isLogScaleY: Boolean = false
    var isHorizontalLines: Boolean = false
    var isVerticalLines: Boolean = false
    var xMin: Float = DEFAULT_MIN_COORDINATE
    var xMax: Float = DEFAULT_MAX_COORDINATE
    var yMin: Float = DEFAULT_MIN_COORDINATE
    var yMax: Float = DEFAULT_MAX_COORDINATE
    var color: Int = DEFAULT_COLOR
    var isSmooth: Boolean = false
    var strokeSize: Float = DEFAULT_STROKE_SIZE
    var pointRadius: Float = DEFAULT_POINT_RADIUS
}
