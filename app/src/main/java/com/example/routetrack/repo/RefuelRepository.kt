package com.example.routetrack.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RefuelRepository {

    private val TAG = "RefuelRepository"
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private val refuelCollection = db.collection("routes").document(userId).collection("refuel")

    

}