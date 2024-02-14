package com.example.routetrack.repo

import android.util.Log
import com.example.routetrack.database.Refuel
import com.example.routetrack.database.Route
import com.example.routetrack.utility.SummaryUtility
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

    fun getMonthlyDistance(callback: (Float) -> Unit) {
        routesCollection
            .whereGreaterThanOrEqualTo("timestamp", SummaryUtility.getMonthStart(SummaryUtility.currentMonth))
            .whereLessThanOrEqualTo("timestamp", SummaryUtility.getMonthEnd(SummaryUtility.currentMonth))
            .get()
            .addOnSuccessListener {  query ->
                val routeList = mutableListOf<Route>()
                for (doc in query.documents) {
                    val route = doc.toObject(Route::class.java)
                    route?.let {
                        routeList.add(it)
                    }
                }
                val distance = calcMonthlyDistance(routeList)
                callback(distance)
            }
            .addOnFailureListener {  ex ->
                Log.e(TAG, ex.message, ex)
            }
    }

    private fun calcMonthlyDistance(routes: List<Route>): Float {
        var totalDistance = 0F
        for (i in routes){
            totalDistance += i.distance
        }
        return totalDistance
    }

    fun getMonthlyTime(callback: (Long) -> Unit) {
        routesCollection
            .whereGreaterThanOrEqualTo("timestamp", SummaryUtility.getMonthStart(SummaryUtility.currentMonth))
            .whereLessThanOrEqualTo("timestamp", SummaryUtility.getMonthEnd(SummaryUtility.currentMonth))
            .get()
            .addOnSuccessListener {  query ->
                val routeList = mutableListOf<Route>()
                for (doc in query.documents) {
                    val route = doc.toObject(Route::class.java)
                    route?.let {
                        routeList.add(it)
                    }
                }
                val time = calcMonthlyTime(routeList)
                callback(time)
            }
            .addOnFailureListener {  ex ->
                Log.e(TAG, ex.message, ex)
            }
    }

    private fun calcMonthlyTime(routes: List<Route>): Long {
        var totalTime = 0L
        for (i in routes){
            totalTime += i.duration
        }
        return totalTime
    }

}