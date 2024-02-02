package com.example.routetrack.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.routetrack.MainActivity
import com.example.routetrack.R
import com.example.routetrack.utility.Constants.ACTION_PAUSE_SERVICE
import com.example.routetrack.utility.Constants.ACTION_SHOW_TRACKING
import com.example.routetrack.utility.Constants.ACTION_START_RESUME_SERVICE
import com.example.routetrack.utility.Constants.ACTION_STOP_SERVICE
import com.example.routetrack.utility.Constants.NOTIFICATION_CHANNEL_ID
import com.example.routetrack.utility.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.routetrack.utility.Constants.NOTIFICATION_ID
import com.example.routetrack.utility.TrackingUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng

class TrackingService: LifecycleService() {

    private val TAG = "TrackingService"
    private var isNewRoute = true
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object{
        val coordinates = MutableLiveData<MutableList<MutableList<LatLng>>>()
        val isTracking = MutableLiveData<Boolean>()
    }

    override fun onCreate() {
        super.onCreate()
        initializeValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        isTracking.observe(this, Observer {
            updateLocation(it)
        })
    }

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
                        startNotification()
                        Log.d(TAG, "Service resumed")
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    isTracking.postValue(false)
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
        addEmptyCoordList()
        isTracking.postValue(true)

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

    private fun initializeValues(){
        coordinates.postValue(mutableListOf())
        isTracking.postValue(false)
    }

    private fun addEmptyCoordList() = coordinates.value?.apply {
        add(mutableListOf())
        coordinates.postValue(this)
    } ?: coordinates.postValue(mutableListOf(mutableListOf()))

    private fun addCoordinates(location: Location?){
        location?.let {
            val position = LatLng(it.latitude, it.longitude)
            coordinates.value?.apply {
                last().add(position)
                coordinates.postValue(this)
            }
        }
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            if (isTracking.value!!){
                p0.locations.let {
                    for (location in it) {
                        addCoordinates(location)
                        Log.d(TAG, "New coordinate: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocation(isTracking: Boolean){
        if (isTracking){
            if (TrackingUtility.hasLocationPermission(this)){
                val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
                    .setWaitForAccurateLocation(false)
                    .setMinUpdateIntervalMillis(2000L)
                    .build()
                fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
            }
        }
        else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

}