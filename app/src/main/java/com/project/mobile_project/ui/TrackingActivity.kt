package com.project.mobile_project.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.project.mobile_project.databinding.ActivityMapsBinding
import com.project.mobile_project.R
import com.project.mobile_project.utilities.MapPresenter
import com.project.mobile_project.utilities.Ui
import com.project.mobile_project.viewModel.ActivitiesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val presenter = MapPresenter(this)
    private val activitiesViewModel: ActivitiesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DEBUG", "TRACKING ACTIVITY AVVIATA")

        setTheme(R.style.Theme_Mobile_project)
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnStartStop.setOnClickListener {
            if (binding.btnStartStop.text == getString(R.string.start_label)) {
                startTracking()
                binding.btnStartStop.setText(R.string.stop_label)
            } else {
                val context = this
                val sharedPreferences: SharedPreferences =
                    context.getSharedPreferences("usernameLoggedPref", Context.MODE_PRIVATE)

                stopTracking(context, activitiesViewModel, sharedPreferences)
                binding.btnStartStop.setText(R.string.start_label)
            }
        }

        presenter.onViewCreated()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        presenter.ui.observe(this) { ui ->
            updateUi(ui)
        }

        presenter.onMapLoaded()
        map.uiSettings.isZoomControlsEnabled = true
    }

    private fun startTracking() {
        binding.container.txtPace.text = ""
        binding.container.txtDistance.text = ""
        binding.container.txtTime.base = SystemClock.elapsedRealtime()
        binding.container.txtTime.start()
        map.clear()

        presenter.startTracking()
    }

    private fun stopTracking(
        context: Context,
        activitiesViewModel: ActivitiesViewModel,
        sharedPreferences: SharedPreferences
    ) {
        binding.container.txtTime.stop()
        val elapsedTime = (SystemClock.elapsedRealtime() - binding.container.txtTime.base) / 1000
        presenter.stopTracking(context, sharedPreferences, activitiesViewModel, elapsedTime)
    }

    @SuppressLint("MissingPermission")
    private fun updateUi(ui: Ui) {
        if (ui.currentLocation != null && ui.currentLocation != map.cameraPosition.target) {
            map.isMyLocationEnabled = true
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ui.currentLocation, 14f))
        }
        binding.container.txtDistance.text = ui.formattedDistance
        binding.container.txtPace.text = ui.formattedSteps

        drawRoute(ui.userPath)
    }

    private fun drawRoute(locations: List<LatLng>) {
        val polylineOptions = PolylineOptions()

        map.clear()

        val points = polylineOptions.points
        points.addAll(locations)

        map.addPolyline(polylineOptions)
    }
}