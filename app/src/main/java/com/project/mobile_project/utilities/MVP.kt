package com.project.mobile_project.utilities

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.project.mobile_project.R

class MapPresenter(private val activity: AppCompatActivity) {

    val ui = MutableLiveData(Ui.EMPTY)
    private val locationProvider = LocationProvider(activity)
    private val stepCounter = StepCounter(activity)
    private val permissionsManager = PermissionsManager(activity, locationProvider, stepCounter)

    fun onViewCreated() {
        locationProvider.liveLocations.observe(activity) { locations ->
            val current = ui.value
            ui.value = current?.copy(userPath = locations)
        }

        locationProvider.liveLocation.observe(activity) { currentLocation ->
            val current = ui.value
            ui.value = current?.copy(currentLocation = currentLocation)
        }

        locationProvider.liveDistance.observe(activity) { distance ->
            val current = ui.value
            val formattedDistance = activity.getString(R.string.distance_value, distance)
            ui.value = current?.copy(formattedDistance = formattedDistance)
        }

        stepCounter.liveSteps.observe(activity) { steps ->
            val current = ui.value
            ui.value = current?.copy(formattedPace = "$steps")
        }
    }

    fun onMapLoaded() {
        permissionsManager.requestUserLocation()
    }

    fun startTracking() {
        locationProvider.trackUser()
        permissionsManager.requestActivityRecognition()
    }

    fun stopTracking() {
        locationProvider.stopTracking()
        stepCounter.unloadStepCounter()
    }
}

data class Ui(
    val formattedPace: String,
    val formattedDistance: String,
    val currentLocation: LatLng?,
    val userPath: List<LatLng>
) {

    companion object {

        val EMPTY = Ui(
            formattedPace = "",
            formattedDistance = "",
            currentLocation = null,
            userPath = emptyList()
        )
    }
}

