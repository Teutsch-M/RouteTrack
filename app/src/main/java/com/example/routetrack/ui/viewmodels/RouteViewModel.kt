package com.example.routetrack.ui.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routetrack.database.Route
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class RouteViewModel: ViewModel() {

    val TAG = "RouteViewModel"
    val db = Firebase.firestore
    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    fun addRoute(route: Route){
        viewModelScope.launch {
            db.collection("routes").document(userId).set(route)
                .addOnSuccessListener {
                    Log.d(TAG, "Route successfully added!")
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message, it)
                }
        }
    }

}