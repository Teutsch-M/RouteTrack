package com.example.routetrack.database



data class Route(
    var vehicle: Int,
    var img: String?,
    var distance: Float,
    var duration: Long,
    var avgSpeed: Float,
    var timestamp: Long
) {
    constructor() : this(0,null, 0F, 0L, 0F, 0L)
}
