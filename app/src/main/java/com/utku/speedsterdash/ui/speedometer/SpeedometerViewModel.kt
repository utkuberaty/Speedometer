package com.utku.speedsterdash.ui.speedometer

import androidx.lifecycle.ViewModel
import com.utku.speedsterdash.speedManager.VehicleSpeedManager
import kotlinx.coroutines.flow.map

class SpeedometerViewModel(vehicleSpeedManager: VehicleSpeedManager) : ViewModel() {
    private val mockVehicleSpeed = vehicleSpeedManager.getMockSpeedData().map {
        it.toInt()
    }
    private val realVehicleSpeed = vehicleSpeedManager.getVehicleSpeedContinuous().map {
        it.toInt()
    }

    fun vehicleSpeed(useReal: Boolean = false) = if (!useReal) {
        mockVehicleSpeed
    } else {
        realVehicleSpeed
    }
}