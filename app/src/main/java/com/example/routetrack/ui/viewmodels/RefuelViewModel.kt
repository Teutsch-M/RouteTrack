package com.example.routetrack.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.example.routetrack.database.Refuel
import com.example.routetrack.repo.RefuelRepository

class RefuelViewModel: ViewModel() {

    private val TAG = "RefuelViewModel"
    private val refuelRepo = RefuelRepository()
    private val _refuelList = MutableLiveData<List<Refuel>>()
    val refuelList: LiveData<List<Refuel>> = _refuelList.distinctUntilChanged()

    init {
        getRefuels()
    }

    fun addRefuel(refuel: Refuel) {
        refuelRepo.addRefuel(refuel)
    }

    fun getRefuels() {
        refuelRepo.getRefuels {
            _refuelList.postValue(it)
        }
    }


}