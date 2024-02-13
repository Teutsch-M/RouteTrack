package com.example.routetrack.database

data class Refuel(
    var img: String?,
    var liter: Float,
    var price: Float,
    var timestamp: Long
) {
    constructor() : this(null, 0F, 0F, 0L)
}
