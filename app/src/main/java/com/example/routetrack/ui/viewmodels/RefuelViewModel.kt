package com.example.routetrack.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.routetrack.database.Refuel
import com.example.routetrack.repo.RefuelRepository

class RefuelViewModel: ViewModel() {

    private val TAG = "RefuelViewModel"
    private val refuelRepo = RefuelRepository()

    fun addRefuel(refuel: Refuel) {
        refuelRepo.addRefuel(refuel)
    }

}