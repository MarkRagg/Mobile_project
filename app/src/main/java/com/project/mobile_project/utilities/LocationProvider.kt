package com.project.mobile_project.utilities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

@SuppressLint("MissingPermission")
class LocationProvider(private val activity: AppCompatActivity) {

    private val client
            by lazy { LocationServices.getFusedLocationProviderClient(activity) }

    private val locations = mutableListOf<LatLng>()

    val liveLocation = MutableLiveData<LatLng>()

    fun getUserLocation() {
        client.lastLocation.addOnSuccessListener { location ->
            val latLng = LatLng(location.latitude, location.longitude)
            locations.add(latLng)
            liveLocation.value = latLng
        }
    }
}
