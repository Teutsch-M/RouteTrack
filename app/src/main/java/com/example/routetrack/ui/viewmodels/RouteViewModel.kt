package com.example.routetrack.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routetrack.database.Route
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class RouteViewModel: ViewModel() {

    private val TAG = "RouteViewModel"
    private val db = Firebase.firestore
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

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

    fun getRoutes(onSuccess: (List<Route>) -> Unit, onFailure: (Exception) -> Unit){
        viewModelScope.launch {
            try {
                db.collection("routes").document(userId).collection("routes").get()
                    .addOnSuccessListener {
                        val routeList = mutableListOf<Route>()
                        it?.let{
                            for (i in it.documents){
                                val img = i.getString("img")
                                val distance = i.getDouble("distance")?.toFloat() ?: 0f
                                val duration = i.getLong("duration") ?: 0
                                val avgSpeed = i.getDouble("avgSpeed")?.toFloat() ?: 0f
                                val timestamp = i.getLong("timestamp") ?: 0

                                val route = Route(img, distance, duration, avgSpeed, timestamp)
                                routeList.add(route)
                            }
                            Log.d(TAG, "Route successfully get!")
                            onSuccess(routeList)
                        }
                    }
                    .addOnFailureListener {
                        Log.e(TAG, it.message, it)
                        onFailure(it)
                    }
            } catch (ex: Exception){
                Log.e(TAG, ex.message, ex)
                onFailure(ex)
            }
        }
    }

}