package com.project.mobile_project.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.CalendarContract
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.project.mobile_project.R
import com.project.mobile_project.viewModel.ActivitiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onItemClicked:  () -> Unit,
    activitiesViewModel: ActivitiesViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold { innerPadding ->
        Column (modifier.padding(innerPadding)) {
            ActivitiesList(onItemClicked, activitiesViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesList(onItemClicked: () -> Unit, activitiesViewModel: ActivitiesViewModel) {
    val activities = activitiesViewModel.allActivities.collectAsState(initial = listOf()).value
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("usernameLoggedPref", Context.MODE_PRIVATE)


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val trackingIntent = Intent(context, TrackingActivity::class.java)
                ContextCompat.startActivity(context, trackingIntent, null)
            }) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_activity)
                )
            }
            FloatingActionButton(onClick = { context.startActivity(Intent(Intent.ACTION_INSERT )
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, "Corsa")
                .putExtra(CalendarContract.Events.ALL_DAY, false)
                .putExtra(CalendarContract.Events.DESCRIPTION, "corsa introduttiva")) }) {
                Icon(
                    Icons.Filled.EditCalendar,
                    contentDescription = stringResource(id = R.string.add_calendar_event)
                )
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            content = {
                items(items = activities.reversed()) { activity ->
                    Card(
                        onClick = {
                            activitiesViewModel.selectActivity(activity)
                            onItemClicked()
                        },
                        modifier = Modifier
                            .size(width = 150.dp, height = 150.dp)
                            .padding(8.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(all = 12.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val scroll = rememberScrollState(0)

                            activity.name?.let {
                                Text(
                                    text = it,
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier.verticalScroll(scroll)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
