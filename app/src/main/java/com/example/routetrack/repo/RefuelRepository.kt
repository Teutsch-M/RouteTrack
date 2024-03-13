package com.example.routetrack.repo

import android.util.Log
import com.example.routetrack.database.Refuel
import com.example.routetrack.utility.SummaryUtility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class RefuelRepository {

    private val TAG = "RefuelRepository"
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.email!!
    private val refuelCollection = db.collection("routes").document(userId).collection("refuel")

    fun addRefuel(refuel: Refuel) {
        refuelCollection.add(refuel)
            .addOnSuccessListener {
                Log.d(TAG, "Refuel successfully added!")
            }
            .addOnFailureListener {  ex ->
                Log.e(TAG, ex.message, ex)
            }
    }

    fun getRefuels(callback: (List<Refuel>) -> Unit) {
        refuelCollection.orderBy("timestamp", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { query ->
                val refuelList = mutableListOf<Refuel>()
                for (doc in query.documents){
                    val refuel = doc.toObject(Refuel::class.java)
                    refuel?.let {
                        refuelList.add(it)
                    }
                }
                callback(refuelList)
            }
            .addOnFailureListener {  ex ->
                Log.e(TAG, ex.message, ex)
            }
    }

    fun getMonthlyFuel(callback: (Float) -> Unit) {
        refuelCollection
            .whereGreaterThanOrEqualTo("timestamp", SummaryUtility.getMonthStart(SummaryUtility.currentMonth))
            .whereLessThanOrEqualTo("timestamp", SummaryUtility.getMonthEnd(SummaryUtility.currentMonth))
            .get()
            .addOnSuccessListener {  query ->
                val refuelList = mutableListOf<Refuel>()
                for (doc in query.documents) {
                    val refuel = doc.toObject(Refuel::class.java)
                    refuel?.let {
                        refuelList.add(it)
                    }
                }
                val fuel = calcMonthlyFuel(refuelList)
                callback(fuel)
            }
            .addOnFailureListener {  ex ->
                Log.e(TAG, ex.message, ex)
            }
    }

    private fun calcMonthlyFuel(refuels: List<Refuel>): Float {
        var totalFuel = 0F
        for (i in refuels){
            totalFuel += i.liter
        }
        return totalFuel
    }

    fun getMonthlyFuelCost(callback: (Float) -> Unit) {
        refuelCollection
            .whereGreaterThanOrEqualTo("timestamp", SummaryUtility.getMonthStart(SummaryUtility.currentMonth))
            .whereLessThanOrEqualTo("timestamp", SummaryUtility.getMonthEnd(SummaryUtility.currentMonth))
            .get()
            .addOnSuccessListener {  query ->
                val refuelList = mutableListOf<Refuel>()
                for (doc in query.documents) {
                    val refuel = doc.toObject(Refuel::class.java)
                    refuel?.let {
                        refuelList.add(it)
                    }
                }
                val fuelCost = calcMonthlyFuelCost(refuelList)
                callback(fuelCost)
            }
            .addOnFailureListener {  ex ->
                Log.e(TAG, ex.message, ex)
            }
    }

    private fun calcMonthlyFuelCost(refuels: List<Refuel>): Float {
        var totalFuelCost = 0F
        for (i in refuels){
            totalFuelCost += i.price
        }
        return totalFuelCost
    }

}