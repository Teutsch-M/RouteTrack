package com.example.routetrack.services

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.example.routetrack.utility.Constants.ACTION_PAUSE_SERVICE
import com.example.routetrack.utility.Constants.ACTION_START_RESUME_SERVICE
import com.example.routetrack.utility.Constants.ACTION_STOP_SERVICE

class TrackingService: LifecycleService() {

    private val TAG = "TrackingService"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_START_RESUME_SERVICE -> {
                    Log.d(TAG,"Service started or resumed")
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


}