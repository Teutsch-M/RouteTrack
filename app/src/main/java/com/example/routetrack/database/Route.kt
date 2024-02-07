package com.example.routetrack.database

import android.graphics.Bitmap

data class Route(
    var img: ByteArray?,
    var distance: Float,
    var duration: Long,
    var avgSpeed: Float,
    var timestamp: Long
)
