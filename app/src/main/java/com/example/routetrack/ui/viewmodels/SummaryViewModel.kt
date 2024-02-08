package com.example.routetrack.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.example.routetrack.database.Route
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class SummaryViewModel: ViewModel() {

    private val TAG = "SummaryViewModel"
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    private val _monthlyDistance = MutableLiveData<Float>()
    private val _monthlyTime = MutableLiveData<Long>()
    val monthlyDistance: LiveData<Float> = _monthlyDistance.distinctUntilChanged()
    val monthlyTime: LiveData<Long> = _monthlyTime.distinctUntilChanged()


    fun getMonthlyDistance(){
        viewModelScope.launch {
            try {
                val query = db.collection("routes").document(userId).collection("routes")
                    .whereGreaterThanOrEqualTo("timestamp", getMonthStart(currentMonth))
                    .whereLessThanOrEqualTo("timestamp", getMonthEnd(currentMonth))
                    .get()
                    .await()
                val routes = mutableListOf<Route>()
                for (i in query.documents){
                    val route = i.toObject(Route::class.java)
                    route?.let{
                        routes.add(it)
                    }
                }
                calcMonthlyDistance(routes)
            }
            catch (ex: Exception){
                Log.e(TAG, ex.message, ex)
            }
        }
    }

    fun getMonthlyTime(){
        viewModelScope.launch {
            try {
                val query = db.collection("routes").document(userId).collection("routes")
                    .whereGreaterThanOrEqualTo("timestamp", getMonthStart(currentMonth))
                    .whereLessThanOrEqualTo("timestamp", getMonthEnd(currentMonth))
                    .get()
                    .await()
                val routes = mutableListOf<Route>()
                for (i in query.documents){
                    val route = i.toObject(Route::class.java)
                    route?.let{
                        routes.add(it)
                    }
                }
                calcMonthlyTime(routes)
            }
            catch (ex: Exception){
                Log.e(TAG, ex.message, ex)
            }
        }
    }

    private fun calcMonthlyDistance(routes: List<Route>){
        var totalDistance = 0F
        for (i in routes){
            totalDistance += i.distance
        }
        _monthlyDistance.value = totalDistance
    }

    private fun calcMonthlyTime(routes: List<Route>){
        var totalTime = 0L
        for (i in routes){
            totalTime += i.duration
        }
        _monthlyTime.value = totalTime
    }

    private fun getMonthStart(month: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        Log.d(TAG, calendar.timeInMillis.toString())
        return calendar.timeInMillis
    }

    private fun getMonthEnd(month: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        Log.d(TAG, calendar.timeInMillis.toString())
        return calendar.timeInMillis
    }


}