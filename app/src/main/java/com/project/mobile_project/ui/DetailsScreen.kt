package com.project.mobile_project.ui


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.mobile_project.R
import com.project.mobile_project.data.Activity
import com.project.mobile_project.viewModel.ActivitiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(activitiesViewModel: ActivitiesViewModel) {
    val context = LocalContext.current
    val selectedActivity = activitiesViewModel.activitySelected
    val details = listOf(
        "Tempo totale: " + selectedActivity!!.totalTime + " secondi",
        "Distanza: " + selectedActivity!!.distance + " metri",
        "Passo medio: " + selectedActivity!!.pace + " min/km",
        "Velocità media: " + selectedActivity!!.speed + " km/h",
        "Passi: " + selectedActivity!!.steps,
    )

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = { shareDetails(context, selectedActivity) }) {
                Icon(Icons.Filled.Share, contentDescription = stringResource(id = R.string.share_activity))
            }
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            var activityName by rememberSaveable { mutableStateOf(if (selectedActivity?.name?.isEmpty() == null) "" else selectedActivity?.name) }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                value = activityName!!,
                onValueChange = { newText: String -> activityName = newText },
                label = { Text("Titolo") },
                placeholder = { if (activityName.isNullOrBlank()) Text("Titolo") else selectedActivity?.name }
            )
            
            Spacer(modifier = Modifier.padding(5.dp))

            LazyVerticalGrid(columns = GridCells.Fixed(1),
                content = {
                    items(items = details) { info ->
                        Card(
                            modifier = Modifier
                                .size(width = 100.dp, height = 80.dp)
                                .padding(3.dp),
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
                                Text(
                                    text = info.toString(),
                                    fontSize = 19.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    textAlign = TextAlign.Left,
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.padding(3.dp))

            Button(
                onClick = {
                    activityName?.let {
                        updateActivityTitle(
                            activitiesViewModel = activitiesViewModel,
                            activity = selectedActivity!!,
                            name = it
                        )
                    }
                },
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 12.dp,
                    end = 20.dp,
                    bottom = 12.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Save")
            }
        }
    }
}

private fun updateActivityTitle(
    activitiesViewModel: ActivitiesViewModel,
    activity: Activity,
    name: String
) {
    activitiesViewModel.updateActivityName(activityId = activity.activityId, name = name)
}

private fun shareDetails(context: Context, activity: Activity?){
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, activity?.name?:"No activity")
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}