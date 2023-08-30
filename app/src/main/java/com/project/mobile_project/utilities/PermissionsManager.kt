package com.project.mobile_project.utilities

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.os.Build

class PermissionsManager(
    activity: AppCompatActivity,
    private val locationProvider: LocationProvider,
    private val stepCounter: StepCounter
) {

    private val locationPermissionProvider = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            locationProvider.getUserLocation()
        }
    }

    private val activityRecognitionPermissionProvider =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                stepCounter.setupStepCounter()
            }
        }


    fun requestUserLocation() {
        locationPermissionProvider.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun requestActivityRecognition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityRecognitionPermissionProvider.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        } else {
            stepCounter.setupStepCounter()
        }
    }
}
