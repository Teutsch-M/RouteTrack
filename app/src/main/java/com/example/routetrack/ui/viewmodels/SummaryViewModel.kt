package com.example.routetrack.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.example.routetrack.repo.RefuelRepository
import com.example.routetrack.repo.RouteRepository
import kotlinx.coroutines.launch

class SummaryViewModel: ViewModel() {

    private val TAG = "SummaryViewModel"
    private val refuelRepo = RefuelRepository()
    private val routeRepo = RouteRepository()
    private val _monthlyDistance = MutableLiveData<Float>()
    private val _monthlyTime = MutableLiveData<Long>()
    private val _monthlyFuel = MutableLiveData<Float>()
    private val _monthlyFuelCost = MutableLiveData<Float>()
    private val _tripCount = MutableLiveData<Int>()
    val monthlyDistance: LiveData<Float> = _monthlyDistance.distinctUntilChanged()
    val monthlyTime: LiveData<Long> = _monthlyTime.distinctUntilChanged()
    val monthlyFuel: LiveData<Float> = _monthlyFuel.distinctUntilChanged()
    val monthlyFuelCost: LiveData<Float> = _monthlyFuelCost.distinctUntilChanged()
    val tripCount: LiveData<Int> = _tripCount.distinctUntilChanged()


    fun getMonthlyDistance(){
        routeRepo.getMonthlyDistance {
            _monthlyDistance.postValue(it)
        }
    }

    fun getMonthlyDistanceVehicle(vehicleType: Int){
        routeRepo.getMonthlyDistanceVehicle(vehicleType) {
            _monthlyDistance.postValue(it)
        }
    }

    fun getMonthlyTime(){
        routeRepo.getMonthlyTime {
            _monthlyTime.postValue(it)
        }
    }

    fun getMonthlyTimeVehicle(vehicleType: Int){
        routeRepo.getMonthlyTimeVehicle(vehicleType) {
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


    fun getMonthlyTripCount(vehicleType: Int) {
        viewModelScope.launch {
            try {
                _tripCount.value = routeRepo.getMonthlyTripCount(vehicleType)
            }
            catch (ex: Exception) {
                Log.e(TAG, ex.message, ex)
            }
        }
    }

}