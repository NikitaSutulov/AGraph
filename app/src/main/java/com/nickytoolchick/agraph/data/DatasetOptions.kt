package com.nickytoolchick.agraph.data

import kotlinx.serialization.Serializable

@Serializable
class DatasetOptions() {
    var points: Array<Pair<Float, Float>> = arrayOf()
    var color: Int = 0
    var isSmooth: Boolean = false
    var strokeSize: Float = 6f
    var pointRadius: Float = 10f
}