package com.example.routetrack.repo

import android.util.Log
import com.example.routetrack.database.Refuel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RefuelRepository {

    private val TAG = "RefuelRepository"
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
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

}