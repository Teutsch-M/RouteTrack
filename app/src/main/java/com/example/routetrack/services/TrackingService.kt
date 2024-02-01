package com.example.routetrack.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.routetrack.MainActivity
import com.example.routetrack.R
import com.example.routetrack.utility.Constants.ACTION_PAUSE_SERVICE
import com.example.routetrack.utility.Constants.ACTION_SHOW_TRACKING
import com.example.routetrack.utility.Constants.ACTION_START_RESUME_SERVICE
import com.example.routetrack.utility.Constants.ACTION_STOP_SERVICE
import com.example.routetrack.utility.Constants.NOTIFICATION_CHANNEL_ID
import com.example.routetrack.utility.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.routetrack.utility.Constants.NOTIFICATION_ID

class TrackingService: LifecycleService() {

    private val TAG = "TrackingService"
    private var isNewRoute = true

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_START_RESUME_SERVICE -> {
                    if (isNewRoute){
                        startNotification()
                        isNewRoute = false
                        Log.d(TAG, "Service started")
                    }
                    else {
                        Log.d(TAG, "Service resumed")
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Log.d(TAG,"Service paused")
                }
                ACTION_STOP_SERVICE -> {
                    Log.d(TAG,"Service stopped")
                }

                else -> {
                    Log.d(TAG,"No service action")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }

    private fun startNotification(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("RouteTrack")
            .setContentText("00:00:00")
            .setSmallIcon(R.drawable.baseline_directions_car_24)
            .setContentIntent(getTrackingFrag())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getTrackingFrag() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING
        },
        FLAG_UPDATE_CURRENT
    )

}