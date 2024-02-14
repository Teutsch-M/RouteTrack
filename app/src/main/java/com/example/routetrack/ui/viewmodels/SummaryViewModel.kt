package com.example.routetrack.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.example.routetrack.repo.RefuelRepository
import com.example.routetrack.repo.RouteRepository

class SummaryViewModel: ViewModel() {

    private val TAG = "SummaryViewModel"
    private val refuelRepo = RefuelRepository()
    private val routeRepo = RouteRepository()
    private val _monthlyDistance = MutableLiveData<Float>()
    private val _monthlyTime = MutableLiveData<Long>()
    private val _monthlyFuel = MutableLiveData<Float>()
    private val _monthlyFuelCost = MutableLiveData<Float>()
    val monthlyDistance: LiveData<Float> = _monthlyDistance.distinctUntilChanged()
    val monthlyTime: LiveData<Long> = _monthlyTime.distinctUntilChanged()
    val monthlyFuel: LiveData<Float> = _monthlyFuel.distinctUntilChanged()
    val monthlyFuelCost: LiveData<Float> = _monthlyFuelCost.distinctUntilChanged()


    fun getMonthlyDistance(){
        routeRepo.getMonthlyDistance {
            _monthlyDistance.postValue(it)
        }
    }

    fun getMonthlyTime(){
        routeRepo.getMonthlyTime {
            _monthlyTime.postValue(it)
        }
    }

    fun getMonthlyFuel() {
        refuelRepo.getMonthlyFuel {
            _monthlyFuel.postValue(it)
        }
    }

    fun getMonthlyFuelCost() {
        refuelRepo.getMonthlyFuelCost {
            _monthlyFuelCost.postValue(it)
        }
    }


}