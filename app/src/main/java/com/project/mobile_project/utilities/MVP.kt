package com.project.mobile_project.utilities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.project.mobile_project.R
import com.project.mobile_project.data.Activity
import com.project.mobile_project.viewModel.ActivitiesViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.round

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
            ui.value = current?.copy(formattedDistance = formattedDistance, distance = distance)
        }

        stepCounter.liveSteps.observe(activity) { steps ->
            val current = ui.value
            ui.value = current?.copy(formattedSteps = "$steps")
        }
    }

    fun onMapLoaded() {
        permissionsManager.requestUserLocation()
    }

    fun startTracking() {
        locationProvider.trackUser()
        permissionsManager.requestActivityRecognition()
    }

    fun stopTracking(
        context: Context,
        sharedPreferences: SharedPreferences,
        activitiesViewModel: ActivitiesViewModel,
        elapsedTime: Long
    ) {
        locationProvider.stopTracking()
        stepCounter.unloadStepCounter()
        insertNewActivity(context, sharedPreferences, activitiesViewModel, elapsedTime)
    }
    private fun insertNewActivity(
        context: Context,
        sharedPreferences: SharedPreferences,
        activitiesViewModel: ActivitiesViewModel,
        elapsedTime: Long
    ) {
        val time = elapsedTime.toDouble()
        val distance = ui.value?.distance
        val speed = (round(distance!! / time * 36) / 10)
        val pace = (round(time / distance * 166.667) / 10)
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        sharedPreferences.getString(context.getString(R.string.username_shared_pref), "")?.let {
            activitiesViewModel.insertActivity(
                Activity(
                    userCreatorUsername = it,
                    name = "Nuova attività",
                    description = "Inserisci una descrizione",
                    totalTime = elapsedTime,
                    distance = distance,
                    speed = speed,
                    pace = pace,
                    steps = ui.value?.formattedSteps?.toInt(),
                    onFoot = null,
                    favourite = false,
                    date = LocalDateTime.now().format(dateFormatter)
                )
            )
        }
    }
}

data class Ui(
    val formattedSteps: String,
    val distance: Int,
    val formattedDistance: String,
    val currentLocation: LatLng?,
    val userPath: List<LatLng>
) {

    companion object {

        val EMPTY = Ui(
            formattedSteps = "",
            distance = 0,
            formattedDistance = "",
            currentLocation = null,
            userPath = emptyList()
        )
    }
}

