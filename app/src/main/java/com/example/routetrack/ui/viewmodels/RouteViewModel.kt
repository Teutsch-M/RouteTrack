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
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
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

}