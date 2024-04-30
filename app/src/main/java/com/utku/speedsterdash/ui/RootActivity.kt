package com.utku.speedsterdash.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.utku.speedsterdash.ui.speedometer.Speedometer
import com.utku.speedsterdash.ui.theme.SpeedsterDashTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI

class RootActivity : ComponentActivity() {

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinAndroidContext {
                SpeedsterDashTheme {
                    Speedometer()
                }
            }
        }
    }
}