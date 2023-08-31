package com.project.mobile_project.utilities

import android.annotation.SuppressLint
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlin.math.roundToInt

@SuppressLint("MissingPermission")
class LocationProvider(private val activity: AppCompatActivity, private val isStarted: MutableLiveData<Boolean>) {

    private val client
            by lazy { LocationServices.getFusedLocationProviderClient(activity) }

    private val locations = mutableListOf<LatLng>()
    private var distance = 0

    val liveLocation = MutableLiveData<LatLng>()
    val liveLocations = MutableLiveData<List<LatLng>>()
    val liveDistance = MutableLiveData<Int>()


    fun getUserLocation() {
        client.lastLocation.addOnSuccessListener { location ->
            if( location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                if(isStarted.value != null && isStarted.value == true) locations.add(latLng)
                liveLocation.value = latLng
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val currentLocation = result.lastLocation
            val latLng = currentLocation?.let { LatLng(it.latitude, currentLocation.longitude) }

            val lastLocation = locations.lastOrNull()

            if (lastLocation != null) {
                distance +=
                    SphericalUtil.computeDistanceBetween(lastLocation, latLng).roundToInt()
                liveDistance.value = distance
            }

            if (latLng != null) {
                locations.add(latLng)
            }
            liveLocations.value = locations
        }
    }

    fun trackUser() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).apply {
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        }.build()

        client.requestLocationUpdates(locationRequest, locationCallback,
            Looper.getMainLooper())
    }

    fun stopTracking() {
        client.removeLocationUpdates(locationCallback)
        locations.clear()
        distance = 0
    }

}
