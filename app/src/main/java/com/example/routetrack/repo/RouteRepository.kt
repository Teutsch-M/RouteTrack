package com.example.routetrack.repo

import android.util.Log
import com.example.routetrack.database.Route
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class RouteRepository {

    private val TAG = "RouteRepository"
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private val routesCollection = db.collection("routes").document(userId).collection("routes")

    fun getSortedRoutes(sortBy: String, callback: (List<Route>) -> Unit) {
        routesCollection.orderBy(sortBy, Query.Direction.DESCENDING).get()
            .addOnSuccessListener { query ->
                val routeList = mutableListOf<Route>()
                for (doc in query.documents){
                    val route = doc.toObject(Route::class.java)
                    route?.let {
                        routeList.add(it)
                    }
                }
                callback(routeList)
            }
            .addOnFailureListener { ex ->
                Log.e(TAG, ex.message, ex)
            }
    }

}