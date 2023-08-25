package com.project.mobile_project.utilities

import android.annotation.SuppressLint
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlin.math.roundToInt

@SuppressLint("MissingPermission")
class LocationProvider(private val activity: AppCompatActivity) {

    private val client
            by lazy { LocationServices.getFusedLocationProviderClient(activity) }

    private val locations = mutableListOf<LatLng>()
    private var distance = 0

    val liveLocation = MutableLiveData<LatLng>()
    val liveLocations = MutableLiveData<List<LatLng>>()
    val liveDistance = MutableLiveData<Int>()


    fun getUserLocation() {
        client.lastLocation.addOnSuccessListener { location ->
            val latLng = LatLng(location.latitude, location.longitude)
            locations.add(latLng)
            liveLocation.value = latLng
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
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000

        client.requestLocationUpdates(locationRequest, locationCallback,
            Looper.getMainLooper())
    }

    fun stopTracking() {
        client.removeLocationUpdates(locationCallback)
        locations.clear()
        distance = 0
    }

}
