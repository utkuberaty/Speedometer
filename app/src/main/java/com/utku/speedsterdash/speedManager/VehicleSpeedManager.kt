package com.utku.speedsterdash.speedManager

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.car.hardware.property.CarPropertyManager.SENSOR_RATE_FASTEST
import android.content.Context
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class VehicleSpeedManager(context: Context) {
    private val car: Car = Car.createCar(context)
    private var carPropertyManager: CarPropertyManager? = null

    private var mockSpeedJob: Job? = null

    init {
        carPropertyManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
    }

    fun getVehicleSpeedContinuous() = callbackFlow {
        val callback = object : CarPropertyManager.CarPropertyEventCallback {

            override fun onChangeEvent(event: CarPropertyValue<*>?) {
                if (event?.propertyId == VehiclePropertyIds.PERF_VEHICLE_SPEED) {
                    val speedValue = event.value as Float
                    trySend(speedValue)
                }
            }

            override fun onErrorEvent(p0: Int, p1: Int) {
                Timber.e("Error in getting vehicle speed $p0 $p1")
            }
        }
        carPropertyManager?.registerCallback(
            callback,
            VehiclePropertyIds.PERF_VEHICLE_SPEED,
            SENSOR_RATE_FASTEST
        )

        awaitClose {
            carPropertyManager?.unregisterCallback(callback)
            release()
        }
    }

    fun getMockSpeedData() = flow {
        var speed = 0f
        var increasing = true
        while (true) {
            emit(speed)
            if (increasing) {
                speed += 10f
                if (speed >= 140f) increasing = false
            } else {
                speed -= 10f
                if (speed <= 0f) increasing = true
            }
            delay(1000)  // Adjust the delay to control the speed of change
        }
    }

    private fun release() {
        car.disconnect()
    }
}