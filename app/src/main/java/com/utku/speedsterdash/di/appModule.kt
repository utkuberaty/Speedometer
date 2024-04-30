package com.utku.speedsterdash.di

import com.utku.speedsterdash.speedManager.VehicleSpeedManager
import com.utku.speedsterdash.ui.speedometer.SpeedometerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { VehicleSpeedManager(androidContext()) }
}

val viewModelModule = module {
    viewModel { SpeedometerViewModel(get()) }
}