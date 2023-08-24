package com.project.mobile_project.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.mobile_project.databinding.ActivityMapsBinding
import com.project.mobile_project.R
import com.project.mobile_project.utilities.LocationProvider
import com.project.mobile_project.utilities.PermissionsManager

class TrackingActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val locationProvider = LocationProvider(this)
    private val permissionManager = PermissionsManager(this, locationProvider)


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Mobile_project)

        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        locationProvider.liveLocation.observe(this) { latLng ->
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
        }

        permissionManager.requestUserLocation()

        map.uiSettings.isZoomControlsEnabled = true
    }

}