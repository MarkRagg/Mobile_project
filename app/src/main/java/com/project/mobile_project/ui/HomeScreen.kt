package com.project.mobile_project.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.CalendarContract
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.mobile_project.R
import com.project.mobile_project.viewModel.ActivitiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onItemClicked:  () -> Unit,
    activitiesViewModel: ActivitiesViewModel,
    modifier: Modifier = Modifier,
    activityAdded: Boolean,
    isFlagOn: MutableState<Boolean>
) {
    Scaffold { innerPadding ->
        Column (modifier.padding(innerPadding)) {
            ActivitiesList(onItemClicked, activitiesViewModel, activityAdded, isFlagOn)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesList(
    onItemClicked: () -> Unit,
    activitiesViewModel: ActivitiesViewModel,
    activityAdded: Boolean,
    isFlagOn: MutableState<Boolean>
) {

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_shared_preferences), Context.MODE_PRIVATE)
    val username = sharedPreferences.getString(context.getString(R.string.username_shared_pref), "").toString()
    val activities = activitiesViewModel.getActivitiesFromUsername(username).collectAsState(initial = listOf()).value
    val favouriteActivities = activitiesViewModel.getFavouriteActivitiesFromUser(username).collectAsState(initial = listOf()).value

    Scaffold(
        floatingActionButton = {
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
        },
        snackbarHost = {
            if (!activityAdded) {
                Snackbar(
                    modifier = Modifier
                        .padding(12.dp)
                )
                {
                    Text("Errore nel salvataggio dell'attività")
                }
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            content = {
                items(
                    items = if(isFlagOn.value) { favouriteActivities.reversed() }
                            else { activities.reversed() }
                ) { activity ->
                    Card(
                        onClick = {
                            activitiesViewModel.selectActivity(activity)
                            onItemClicked()
                        },
                        modifier = Modifier
                            .size(width = 150.dp, height = 120.dp)
                            .padding(8.dp, 4.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(all = 12.dp)
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(all = 12.dp)
                                    .fillMaxSize(0.9f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                activity.name?.let {
                                    Text(
                                        text = it,
                                        fontSize = 17.sp,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight(10f),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                Text(
                                    text = activity.date,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(8f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            FloatingActionButton(
                                onClick = {
                                    activitiesViewModel.updateActivityFavourite(activity.activityId, !(activity.favourite))
                                },
                                modifier = Modifier.weight(1f)//.align(alignment = Alignment.Bottom)
                            ) {
                                Icon(
                                    if(activity.favourite) {
                                        Icons.Filled.Star
                                    } else {
                                        Icons.Filled.StarBorder
                                    },
                                    contentDescription = stringResource(id = R.string.update_favourite)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
