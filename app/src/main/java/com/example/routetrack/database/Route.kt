package com.example.routetrack.database

import android.graphics.Bitmap

data class Route(
    var img: Bitmap?,
    var timestamp: Long,
    var distance: Float,
    var duration: Long,
    var avgSpeed: Float,
    var userId: Int?
)
