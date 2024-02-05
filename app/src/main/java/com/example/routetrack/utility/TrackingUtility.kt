package com.example.routetrack.utility

import android.Manifest
import android.content.Context
import android.os.Build
import pub.devrel.easypermissions.EasyPermissions

object TrackingUtility {

    fun hasLocationPermission(context: Context) = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
    else{
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }

    fun formatTime(ms: Long, includeMillis: Boolean): String {
        var milliSec = ms
        val hours = milliSec / (1000*60*24)
        milliSec -= hours * (1000*60*24)
        val minutes = milliSec / (1000*60)
        milliSec -= minutes * (1000*60)
        val seconds = milliSec / (1000)
        milliSec -= seconds * (1000)
        milliSec /= 10
        return "${if(hours < 10) "0" else ""}$hours:" +
                "${if(minutes < 10) "0" else ""}$minutes:" +
                "${if(seconds < 10) "0" else ""}$seconds:" +
                if(includeMillis) "${if(milliSec < 10) "0" else ""}$milliSec" else ""
    }

}