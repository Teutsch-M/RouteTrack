package com.example.routetrack.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.example.routetrack.database.Route
import com.example.routetrack.repo.RouteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class RouteViewModel: ViewModel() {

    private val TAG = "RouteViewModel"
    private val db = Firebase.firestore
    private val userId = FirebaseAuth.getInstance().currentUser!!.email!!
    private val routeRepo = RouteRepository()
    private val _routeList = MutableLiveData<List<Route>>()
    val routeList: LiveData<List<Route>> = _routeList.distinctUntilChanged()

    init {
        getDataForSpinner("timestamp")
    }

    fun addRoute(route: Route){
        viewModelScope.launch {
            try {
                db.collection("routes").document(userId).collection("routes").add(route)
                    .addOnSuccessListener {
                        Log.d(TAG, "Route successfully added!")
                    }
                    .addOnFailureListener {
                        Log.e(TAG, it.message, it)
                    }
            }
            catch (ex: Exception){
                Log.e(TAG, ex.message, ex)
            }
        }
    }


    fun getDataForSpinner(sortBy: String) {
        routeRepo.getSortedRoutes(sortBy) {
            _routeList.postValue(it)
        }
    }


    fun getVehicleData(vehicleType: Int) {
        routeRepo.getVehicleRoutes(vehicleType) {
            _routeList.postValue(it)
        }
    }


    fun getSortedRoutes(vehicleType: Int, sortBy: Int) {
        Log.d(TAG, "$vehicleType $sortBy")
        viewModelScope.launch {
            try {
                val routes: List<Route> = if (vehicleType == 0) {
                    routeRepo.getRoutes()
                } else {
                    routeRepo.getVehicleRoutes2(vehicleType - 1)
                }
                Log.d(TAG, "Routes: $routes")
                val sortedRoutes = when (sortBy) {
                    0 -> routes.sortedByDescending { it.timestamp }
                    1 -> routes.sortedByDescending { it.distance }
                    2 -> routes.sortedByDescending { it.duration }
                    3 -> routes.sortedByDescending { it.avgSpeed }
                    else -> routes
                }
                Log.d(TAG, "SortedRoutes: $sortedRoutes")
                _routeList.value = sortedRoutes
            }
            catch (ex: Exception) {
                Log.e(TAG, ex.message, ex)
            }
        }
        Log.d(TAG, "${_routeList.value}")
    }


}