package com.project.mobile_project.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.project.mobile_project.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen(
    context: Context,
    activity: Activity
) {
    var gpsChecker by rememberSaveable { mutableStateOf(checkGPS(context)) }
    var internetConnChecker by rememberSaveable { mutableStateOf(checkInternet(context)) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    gpsChecker = checkGPS(context)
                    internetConnChecker = checkInternet(context)
                }
            ) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = stringResource(id = R.string.refresh_gps)
                )
            }
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .padding(all = 12.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Controlla che il GPS sia acceso! Il bottone per iniziare l'attività non sarà disponibile altrimenti!",
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
                Text("Attiva il GPS")
            }

            Spacer(modifier = Modifier.size(15.dp))

            Text(
                text = "La connessione ad internet è necessaria, attivala per iniziare l'attività",
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
                Text("Attiva la connessione ad internet")
            }

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {
                    val trackingIntent = Intent(context, TrackingActivity::class.java)
                    ContextCompat.startActivity(context, trackingIntent, null)
                    activity.finish()
                },
                enabled = gpsChecker && internetConnChecker
            ) {
                Text("Inizia a correre")
            }
        }
    }
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