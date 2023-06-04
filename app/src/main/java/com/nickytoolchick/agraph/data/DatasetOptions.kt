package com.nickytoolchick.agraph.data

import android.graphics.Color

data class DatasetOptions(
    val points: MutableList<Point>,
    val color: Color,
    val isSmooth: Boolean,
    val strokeSize: Int
)