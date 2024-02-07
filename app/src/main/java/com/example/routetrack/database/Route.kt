package com.example.routetrack.database



data class Route(
    var img: String?,
    var distance: Float,
    var duration: Long,
    var avgSpeed: Float,
    var timestamp: Long
)
