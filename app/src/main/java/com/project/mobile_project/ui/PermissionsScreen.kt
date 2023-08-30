package com.project.mobile_project.ui

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

@Composable
fun PermissionsScreen(
    context: Context,
    //warningViewModel: WarningViewModel
) {
    //if (!checkGPS(context)) {
        Column(
            modifier = Modifier
                .padding(all = 12.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "GPS is turned off but is needed to get the coordinates",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }
            ) {
                Text("Turn on the GPS")
            }
/*
            Spacer(modifier = Modifier.size(15.dp))

            Text(
                text = "Intenet connection is needed",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {
                    val intent = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }
            ) {
                Text("Turn on internet connection")
            }
*/
            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {
                    val trackingIntent = Intent(context, TrackingActivity::class.java)
                    ContextCompat.startActivity(context, trackingIntent, null)
                },
                enabled = checkGPS(context) // && checkInternet(context) TODO: live update
            ) {
                Text("Start Tracking")
            }
        }
   /* } else {
        val trackingIntent = Intent(LocalContext.current, TrackingActivity::class.java)
        ContextCompat.startActivity(LocalContext.current, trackingIntent, null)
    }*/
}

fun checkGPS(context: Context): Boolean {
    val mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun checkInternet(context: Context): Boolean {
    val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = mConnectivityManager.activeNetwork ?: return false
        val actNw = mConnectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        val nwInfo = mConnectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}