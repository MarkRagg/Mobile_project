package com.project.mobile_project.utilities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.project.mobile_project.R
import com.project.mobile_project.data.Activity
import com.project.mobile_project.viewModel.ActivitiesViewModel

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

    fun stopTracking(context: Context, activitiesViewModel: ActivitiesViewModel, sharedPreferences: SharedPreferences, time: Long) {
        locationProvider.stopTracking()
        stepCounter.unloadStepCounter()

        val distance = ui.value?.formattedDistance?.toInt()
        val speed = distance?.div(time.toInt())

        sharedPreferences.getString(context.getString(R.string.username_shared_pref), "")?.let {
            activitiesViewModel.insertActivity(
                Activity(
                    userCreatorUsername = it,
                    name = "Attività SERIA",
                    description = "LELEelelle",
                    totalTime = time,
                    distance = distance!!,
                    speed = speed!!,
                    pace = null,
                    steps = ui.value?.formattedPace?.toInt(),
                    onFoot = null
                )
            )
        }
    }
}

fun insertNewActivity(sharedPreferences: SharedPreferences, context: Context, activitiesViewModel: ActivitiesViewModel) {
    val userCreator = sharedPreferences.getString(context.getString(R.string.username_shared_pref), "")?.let {
        activitiesViewModel.insertActivity(
            Activity(
                userCreatorUsername = it,
                name = "Attività di prova",
                description = "La descriptiones es mas importante" ,
                totalTime = 2,
                distance = 50,
                speed = 20 ,
                pace = null,
                steps = null,
                onFoot = null
            )
        )
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

