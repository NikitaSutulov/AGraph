package com.nickytoolchick.agraph.data

import kotlinx.serialization.Serializable

@Serializable
class DatasetOptions() {
    var points: Array<Pair<Float, Float>> = arrayOf()
}